import scala.collection.mutable.ListBuffer

trait Observer[E] {
  def receiveUpdate(event: E);
}

trait Observable[E] { 
  private var observers = ListBuffer.empty[Observer[E]]

  def addObserver(observer: Observer[E]) {
    observers += observer
  }

  def notifyObservers(event:E) {
    observers.foreach{
      observer => observer.receiveUpdate(event)
     }
  }
}

class User(var name:String, var id:Long) {
  override def toString = name + "(" + id + ")"
}

class UserService extends Observable[User] {
  def getUser(id:Long) {
    var user = new User("foo",id)
    notifyObservers(user)
  }
}

object FancyApplication extends Observer[User] {
  override def receiveUpdate(user:User) {
    println(user)
  }

  def main(args:Array[String]) {
    var userService = new UserService
    userService.addObserver(this)
    args.foreach(s => userService.getUser(s.toLong))
  }
}