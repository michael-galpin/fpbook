import scala.collection.mutable.HashMap
import scala.io.Source

object SysConfig{
	private val instance = new SysConfig("sys.config")
	
	def getInstance = instance
}

class SysConfig private(val path:String){
	private var configValues = new HashMap[String,String]

	Source.fromFile(path).getLines().foreach(line => {
		var index = line.indexOf("=")
		var key = line.slice(0,index)
		var value = line.takeRight(line.length - index - 1)
		configValues.put(key,value)
	})

	def getValue(key:String) = configValues(key)	
}

object App{
	def main(args:Array[String]){
		println(SysConfig.getInstance.getValue("foo"))
	}
}