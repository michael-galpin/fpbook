import scala.collection.mutable.ListBuffer

trait Message{
  var sender:String = null
  var recipient:String = null
  var timestamp:Long = 0L
  def accept(visitor:MessageVisitor)
}

trait MessageVisitor{
  def visit(textMessage:TextMessage)
  def visit(multimediaMessage:MultimediaMessage)
}

class TextMessage extends Message{
  var text:String = ""

  override def accept(visitor:MessageVisitor){
    visitor.visit(this)
  }
}

class MultimediaMessage extends Message{
  var attachment:Array[Byte] = Array()
  var mime:String = ""

  override def accept(visitor:MessageVisitor){
    visitor.visit(this)
  }
}

class SizeVisitor extends MessageVisitor{
  var size:Int = 0

  override def visit(textMessage:TextMessage){
    size += textMessage.text.length
  }

  override def visit(multimediaMessage:MultimediaMessage){
    size += multimediaMessage.attachment.length
  }
}

class SentSizeVisitor(var owner:String) extends MessageVisitor{
  var size:Int = 0
	
  override def visit(textMessage:TextMessage){
    if (owner == textMessage.sender){
      size += textMessage.text.length
    }
  }

  override def visit(multimediaMessage:MultimediaMessage){
    if (owner == textMessage.sender){		
      size += multimediaMessage.attachment.length
    }
  }	
}

class MessageHistory{
  var messages:ListBuffer[Message] = ListBuffer.empty[Message]
  var owner:String = ""
}
def size = {
  var sizeVisitor = new SizeVisitor
  var i = 0
  while (i < messages.size){
    messages(i).accept(sizeVisitor)
    i += 1
  }
  sizeVisitor.size
}

	def sizeSent = {

	}
}