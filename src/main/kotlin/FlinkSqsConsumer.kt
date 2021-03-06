import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQSClient
import javax.jms.Connection
import javax.jms.JMSException
import javax.jms.MessageConsumer
import javax.jms.Session.AUTO_ACKNOWLEDGE
import javax.jms.TextMessage
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.RichSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.slf4j.LoggerFactory

class FlinkSqsConsumer(
    private val queueName: String,
    private val endpointUrl: String,
    private val region: String
) : RichSourceFunction<String>() {
    private var running = false
    private lateinit var consumer: MessageConsumer
    private lateinit var connection: Connection
    private fun init() {
        logger.info("Initializing...")
        val sqsClient = AmazonSQSClient.builder()
            .withCredentials(ProfileCredentialsProvider())
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    endpointUrl,
                    region
                )
            )
            .build()
        val connectionFactory = SQSConnectionFactory(
            ProviderConfiguration(),
            sqsClient
        )
        connection = connectionFactory.createConnection()
        connection.start()
        val session = connection.createSession(false, AUTO_ACKNOWLEDGE)
        val destination = session.createQueue(queueName)
        consumer = session.createConsumer(destination)
    }

    override fun open(parameters: Configuration) {
        super.open(parameters)
        running = true
        init()
    }

    override fun run(ctx: SourceFunction.SourceContext<String>) {
        while (running) {
            try {
                consumer.receive(1000)?.let {
                    val msg = (it as TextMessage).text!!
                    ctx.collect(msg)
                }
            } catch (e: JMSException) {
                logger.error(e.localizedMessage)
                running = false
            }
        }
        try {
            close()
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
    }

    override fun cancel() {
        running = false
    }

    override fun close() {
        try {
            logger.info("Closing...")
            connection.close()
        } catch (e: JMSException) {
            throw RuntimeException("Error while closing connection", e)
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private val logger = LoggerFactory.getLogger(FlinkSqsConsumer::class.java)
    }
}
