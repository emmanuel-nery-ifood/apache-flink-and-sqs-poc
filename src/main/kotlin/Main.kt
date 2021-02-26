import com.amazonaws.regions.Regions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows.of
import org.apache.flink.streaming.api.windowing.time.Time.seconds

fun main() {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment()
    val stream = env.addSource(
        FlinkSqsConsumer(
            queueName = "digester",
            endpointUrl = "http://localhost:4566",
            region = Regions.US_EAST_2.toString()
        )
    )

    val personFiltered = stream
        .map {
            Json.decodeFromString<Person>(it!!)
        }
        .windowAll(of(seconds(5)))
        .process(ProcessPersonsWindow())
        .print()

    env.execute()
}

