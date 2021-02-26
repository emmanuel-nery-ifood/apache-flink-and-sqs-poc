import org.apache.flink.api.common.io.OutputFormat
import org.apache.flink.configuration.Configuration
import org.slf4j.LoggerFactory

class BrazeWriter : OutputFormat<Person> {
    override fun configure(parameters: Configuration) {

    }

    override fun open(taskNumber: Int, numTasks: Int) {
        logger.info("Opening connection...")
    }

    override fun writeRecord(record: Person) {
        logger.info(record.toString())
    }

    override fun close() {
        logger.info("Closing connection...")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BrazeWriter::class.java)
    }
}
