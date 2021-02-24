import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.datastream.DataStream

class DataStreamExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>){
            val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment()

            val flintstones: DataStream<Person> = env.fromElements(
                Person("Fred", 35),
                Person("Wilma", 35),
                Person("Pebbles", 2))

            val adults: DataStream<Person> = flintstones.filter { it.age >= 18 }

            adults.print()

            env.execute("DataStreamExample")

        }
    }
}

