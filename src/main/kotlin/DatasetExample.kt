import org.apache.flink.api.java.DataSet
import org.apache.flink.api.java.ExecutionEnvironment

class DatasetExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>){
            val env = ExecutionEnvironment.getExecutionEnvironment();

            val message : DataSet<String> = env.fromElements("Mas","que","Xablau","mano")
            val blackList = listOf<String>("Xablau")

            val messageFriendly = message.filter{
                it !in blackList
            }

            messageFriendly.print()

        }
    }
}