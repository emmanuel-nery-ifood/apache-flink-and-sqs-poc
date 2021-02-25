//import com.amazon.sqs.javamessaging.ProviderConfiguration
//import com.amazon.sqs.javamessaging.SQSConnectionFactory
//import com.amazonaws.services.sqs.AmazonSQSClientBuilder
//import org.apache.activemq.ActiveMQConnectionFactory
//import org.apache.flink.configuration.Configuration
//import org.apache.flink.streaming.api.functions.source.RichSourceFunction
//import org.apache.flink.streaming.api.functions.source.SourceFunction
//import org.slf4j.LoggerFactory
//import java.lang.Exception
//import javax.jms.*
//import javax.print.attribute.standard.Destination
//import javax.jms.TextMessage
//
//
//
//
///**
// */
//class FlinkSqsConsumer :
//    RichSourceFunction<String?>() {
//    @Volatile
//    @Transient
//    private var running = false
//
//    @Transient
//    private var consumer: MessageConsumer? = null
//
//    @Transient
//    private var connection: Connection? = null
//
//
//    @Throws(JMSException::class)
//    private fun init() {
//        // Create a ConnectionFactory
//        val connectionFactory = SQSConnectionFactory(
//            ProviderConfiguration(),
//            AmazonSQSClientBuilder.defaultClient()
//        )
//
//        // Create a Connection
//        val connection = connectionFactory.createConnection()
//
//        // Create a Session
//        val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
//
//        // Create the destination (Topic or Queue)
//        val destination = session.createQueue("digester")
//
//        // Create a MessageConsumer from the Session to the Topic or Queue
//        val consumer = session.createConsumer(destination)
//
//        connection.start()
//
//        val receivedMessage = consumer.receive(1000)?.let {
//            println("Received: " + (it as TextMessage).text)
//        }
//
//        connection.close()
//
//    }
//
//    @Throws(Exception::class)
//    override fun open(parameters: Configuration) {
//        super.open(parameters)
//        running = true
//        init()
//    }
//
//    override fun run(ctx: SourceFunction.SourceContext<String>) {
//        // this source never completes
//        while (running) {
//            try {
//                // Wait for a message
//                val message: Message = consumer.receive(1000)
//                if (message is TextMessage) {
//                    val textMessage: TextMessage = message as TextMessage
//                    val text: String = textMessage.getText()
//                    ctx.collect(text)
//                } else {
//                    LOG.error("Don't know what to do .. or no message")
//                }
//            } catch (e: JMSException) {
//                LOG.error(e.getLocalizedMessage())
//                running = false
//            }
//        }
//        try {
//            close()
//        } catch (e: Exception) {
//            LOG.error(e.message, e)
//        }
//    }
//
//    override fun cancel() {
//        running = false
//    }
//
//    @Throws(Exception::class)
//    override fun close() {
//        LOG.info("Closing")
//        try {
//            connection.close()
//        } catch (e: JMSException) {
//            throw RuntimeException("Error while closing ActiveMQ connection ", e)
//        }
//    }
//
//    companion object {
//        private const val serialVersionUID = 1L
//        private val LOG = LoggerFactory.getLogger(FlinkJMSStreamSource::class.java)
//    }
//}