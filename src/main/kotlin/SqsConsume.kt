import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.net.URI
import java.util.function.Consumer

object SqsConsume {
    @JvmStatic
    fun main(args: Array<String>) {
        val client = SqsClient.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create("http://localhost:4566"))
            .build()
        val queueUrl = "http://localhost:4566/000000000000/teste"
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .waitTimeSeconds(5)
            .maxNumberOfMessages(5)
            .build()
        while (true) {
            client.receiveMessage(receiveMessageRequest)
                .messages()
                .forEach(Consumer { message: Message ->
                    println(message.body())
                })


        }
    }
}