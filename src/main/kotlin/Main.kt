import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

fun main() {
    val queueName = "digester"
    val endpointUrl = "http://localhost:4566"
    val region = "US_EAST_2"
    val windowSize = 75L

    val env = StreamExecutionEnvironment.getExecutionEnvironment()
        .apply {
            addSource(
                FlinkSqsConsumer(
                    queueName,
                    endpointUrl,
                    region
                )
            ).map {
                Json.decodeFromString<Person>(it!!)
            }
                .keyBy { it.type }
                .countWindow(windowSize)
                .process(ProcessPersonsWindow())
//                .writeUsingOutputFormat(BrazeWriter())
                .addSink(BrazeSink())
        }


    env.execute()
}
