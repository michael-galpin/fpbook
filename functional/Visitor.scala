case class TextMessage(text:String, sender:String)
case class MultimediaMessage(attachment:Array[Byte], mime:String, sender:String)

class MessageHistory(val messages:List[Any])

object Messages{
	def size(history:MessageHistory) = history.messages.foldLeft(0){(sum,message) => message match {
			case tm:TextMessage => sum + tm.text.length
			case mm:MultimediaMessage => sum + mm.attachment.length
			case _ => sum
		}}
}