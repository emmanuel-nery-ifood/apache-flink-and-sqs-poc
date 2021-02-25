import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

class DataStreamExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment()

            val flintstones: DataStream<Person> = env.fromElements(
                Person(1, "Fred", 35),
                Person(2, "Wilma", 35),
                Person(3, "Pebbles", 2)
            )

            val adults: DataStream<Person> = flintstones.filter { it.age >= 18 }

            adults.print()

            env.execute("DataStreamExample")

        }
    }
}

