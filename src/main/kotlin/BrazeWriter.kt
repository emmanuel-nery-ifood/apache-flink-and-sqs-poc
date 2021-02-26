import org.apache.flink.api.common.io.OutputFormat
import org.apache.flink.configuration.Configuration
import org.slf4j.LoggerFactory

class BrazeWriter : OutputFormat<List<Person>> {
    override fun configure(parameters: Configuration) { }

    override fun open(taskNumber: Int, numTasks: Int) { }

    override fun writeRecord(record: List<Person>) {
        logger.info(record.toString())
    }

    override fun close() { }

    companion object {
        private val logger = LoggerFactory.getLogger(BrazeWriter::class.java)
    }
}
