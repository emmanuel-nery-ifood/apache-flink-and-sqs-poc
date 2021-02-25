import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.jms.Session
import javax.jms.TextMessage

fun main(){
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
    val connection = connectionFactory.createConnection()

    connection.start()

    // Create a Session
    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)

    // Create the destination (Topic or Queue)
    val destination = session.createQueue("digester")

    // Create a MessageConsumer from the Session to the Topic or Queue
    val consumer = session.createConsumer(destination)



    while (true){
        consumer.receive(1000)?.let {
            val msg = (it as TextMessage).text!!

            val person = Json.decodeFromString<Person>(msg)
            println("Received: $person")
        }
    }


    connection.close()
}