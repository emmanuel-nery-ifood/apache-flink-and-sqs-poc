import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQSClient
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.RichSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.slf4j.LoggerFactory
import java.lang.Exception
import javax.jms.*
import javax.jms.TextMessage


class FlinkSqsConsumer :
    RichSourceFunction<String>() {
    @Volatile
    @Transient
    private var running = false

    @Transient
    private lateinit var consumer: MessageConsumer

    @Transient
    private lateinit var connection: Connection


    @Throws(JMSException::class)
    private fun init() {
        val sqsClient = AmazonSQSClient.builder()
            .withCredentials(ProfileCredentialsProvider())
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    "http://localhost:4566",
                    Regions.US_EAST_2.toString()))
            .build()

        // Create a ConnectionFactory
        val connectionFactory = SQSConnectionFactory(ProviderConfiguration(),
            sqsClient)

        // Create a Connection
        connection = connectionFactory.createConnection()

        connection.start()

        // Create a Session
        val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)

        // Create the destination (Topic or Queue)
        val destination = session.createQueue("digester")

        // Create a MessageConsumer from the Session to the Topic or Queue
        consumer = session.createConsumer(destination)

    }

    @Throws(Exception::class)
    override fun open(parameters: Configuration) {
        super.open(parameters)
        running = true
        init()
    }

    override fun run(ctx: SourceFunction.SourceContext<String>) {
        // this source never completes
        while (running) {
            try {
                // Wait for a message
                 consumer.receive(1000)?.let {

                     val msg = (it as TextMessage).text!!
//                     it.acknowledge()
//                    val person = Json.decodeFromString<Person>(msg)
//                    println("Received: $person")
                     ctx.collect(msg)
                 }



            } catch (e: JMSException) {
                LOG.error(e.localizedMessage)
                running = false
            }
        }
        try {
            close()
        } catch (e: Exception) {
            LOG.error(e.message, e)
        }
    }

    override fun cancel() {
        running = false
    }

    @Throws(Exception::class)
    override fun close() {
        LOG.info("Closing")
        try {
            connection.close()
        } catch (e: JMSException) {
            throw RuntimeException("Error while closing ActiveMQ connection ", e)
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private val LOG = LoggerFactory.getLogger(FlinkSqsConsumer::class.java)
    }
}