import scala.collection.mutable.ListBuffer
import scala.actors.Actor
import Actor._

trait Observable[E] {
  private val observers = ListBuffer.empty[Actor]
	
  def addObserver(observer:Actor) {
    observers += observer
  }
	
  def notifyObservers(event:E) {
    observers.foreach { observer => observer ! event }
  }
}

case class User(name:String, id:Long)

class UserService extends Observable[User] {
  def getUser(id:Long){
    notifyObservers(new User("foo",id))	
  }
}

object FancyApplication extends Actor {
  def main(args:Array[String]) {
    val userService = new UserService
    userService.addObserver(this)
    args.foreach(s => userService.getUser(s.toLong))
  }

  override def act = loop {
    react {
      case u:User => println(u)
      case _ =>
    }
  }
  start()
}