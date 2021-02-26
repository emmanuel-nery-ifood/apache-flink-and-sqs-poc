import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector

class ProcessPersonsWindow : ProcessWindowFunction<Person, List<Person>, Type, GlobalWindow>() {
    override fun process(key: Type, context: Context, elements: MutableIterable<Person>, out: Collector<List<Person>>) {
        out.collect(elements.toList())
    }
}
