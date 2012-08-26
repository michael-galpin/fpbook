import scala.collection.mutable.ListBuffer

trait Observable[E] { 
  private val observers = ListBuffer.empty[ E=> Unit]

  def addObserver(observer: E => Unit) {
    observers += observer
  }

  def notifyObservers(event:E) {
    observers.foreach{
      observer => observer(event)
    }
  }
}

case class User(name:String, id:Long) {
  override def toString = name + "(" + id + ")"
}

class UserService extends Observable[User] {
  def getUser(id:Long) {
    var user = new User("foo",id)
    notifyObservers(user)
  }
}

object FancyApplication {
	
  private def showUser(user:User){
    println(user)
  }

  def main(args:Array[String]) {
    var userService = new UserService
    userService.addObserver(showUser)	
    args.foreach(s => userService.getUser(s.toLong))
  }
}
