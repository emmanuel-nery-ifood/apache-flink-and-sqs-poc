import javax.jms.JMSException
import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.TextMessage

class MyListener : MessageListener {
    override fun onMessage(message: Message) {
        try {
            val msg = (message as TextMessage).text
            println("Received: $msg")
        }
        catch (exp: JMSException){
            exp.printStackTrace()
        }
    }

}