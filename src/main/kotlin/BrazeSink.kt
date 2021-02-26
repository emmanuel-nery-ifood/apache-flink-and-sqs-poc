import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.slf4j.LoggerFactory

class BrazeSink : SinkFunction<List<Person>>{
    override fun invoke(value: List<Person>, context: SinkFunction.Context<*>) {
        logger.info(value.toString())
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BrazeWriter::class.java)
    }
}