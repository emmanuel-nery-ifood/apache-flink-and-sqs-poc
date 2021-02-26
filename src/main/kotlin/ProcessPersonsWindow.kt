import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class ProcessPersonsWindow : ProcessAllWindowFunction<Person, Person, TimeWindow>() {
    override fun process(context: Context, elements: MutableIterable<Person>, out: Collector<Person>) {
        elements.forEach(out::collect)
    }
}
