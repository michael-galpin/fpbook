import scala.collection.mutable.HashMap
import scala.io.Source

object SysConfig{
	lazy val configValues = Source.fromFile("sys.config").getLines()
		.map{ line => line.split("=") }
		.map{ array => array(0)->array(1) }
		.foldLeft(new HashMap[String,String]){ (map, mapping) => map += mapping }

	def apply(key:String) = configValues(key)	
}

object App{
	def main(args:Array[String]){
		println(SysConfig("foo"))
	}
}