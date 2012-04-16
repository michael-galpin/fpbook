class Point(var x:Double, var y:Double){
	import java.lang.Math.sqrt
	def distanceTo(p:Point) = {
		var dx = x - p.x
		var dy = y - p.y
		sqrt(dx*dx + dy*dy)
	}
}

trait Polygon{
	def numSides:Int
	def perimeter:Double
}

class Triangle(var a:Double, var b:Double, var c:Double) extends Polygon{
	def this(x:Point, y:Point, z:Point) = 
		this(x.distanceTo(y), y.distanceTo(z), z.distanceTo(x))
	override def numSides = 3
	override def perimeter = a + b + c
}

class Quadrilateral(var a:Double, var b:Double, var c:Double, var d:Double) extends Polygon{
	def this(x:Point, y:Point, z:Point, w:Point) = 
		this(x.distanceTo(y), y.distanceTo(z), z.distanceTo(w), w.distanceTo(x))
	override def numSides = 4
	override def perimeter = a + b + c + d
}

class PolygonFactory{
	def createPolygon(v:List[Point]):Polygon = {
		var p:Polygon = null
		if (v.size == 3){
			p = new Triangle(v(0), v(1), v(2))
		} else if (v.size == 4){
			p = new Quadrilateral(v(0), v(1), v(2), v(3))
		}
		p
	}
}

object App{
	def main(args:Array[String]){
		var points = List(new Point(0.0, 0.0), 
			new Point(3.0, 0.0), new Point(0.0, 4.0))
		var p = PolygonFactory.createPolygon(points)
		println("Number of sides = " + p.numSides)
		println("Perimeter = " + p.perimeter)
		points = List(new Point(0.0, 0.0), new Point(3.0, 0.0), 
			new Point(3.0, 4.0), new Point(0.0, 4.0))
		p = PolygonFactory.createPolygon(points)
		println("Number of sides = " + p.numSides)
		println("Perimeter = " + p.perimeter)
	}
}