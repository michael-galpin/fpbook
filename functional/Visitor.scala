case class TextMessage(text:String, sender:String, recipient:String, timestamp:Long)
case class MultimediaMessage(attachment:Array[Byte], mime:String, sender:String, recipient:String, 
	timestamp:Long)

class MessageHistory(val messages:List[Any], val owner:String)

object Messages{
  def size(history:MessageHistory) = history.messages.foldLeft(0){(sum,message) => message match {
    case tm:TextMessage => sum + tm.text.length
    case mm:MultimediaMessage => sum + mm.attachment.length
    case _ => sum
  }}

  def sentSize(history:MessageHistory) = history.messages.foldLeft(0){
	(sum,message) => message match {
      case tm:TextMessage => if (history.owner == tm.sender) sum + tm.text.length else sum
      case mm:MultimediaMessage => 
        if (history.owner == mm.sender) sum + mm.attachment.length else sum
      case _ => sum
  }}

}