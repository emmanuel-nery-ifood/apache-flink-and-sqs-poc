import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

fun main(){
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment()
    val stream = env.addSource(FlinkSqsConsumer(
        queueName = "digester",
        endpointUrl = "http://localhost:4566",
        region = Regions.US_EAST_2.toString()
    ))


    val personFiltered = stream
        .map { Json.decodeFromString<Person>(it!!)}



    personFiltered.print()

    env.execute()
}