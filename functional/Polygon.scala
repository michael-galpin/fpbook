case class Point(x:Double, y:Double)

case class Triangle(a:Double, b:Double, c:Double)

case class Quadrilateral(a:Double, b:Double, 
	c:Double, d:Double)

object Polygon{
	import java.lang.Math.sqrt

	def dist(a:Point, b:Point) = {
		val dx = a.x - b.x
		val dy = a.y - b.y
		sqrt(dx * dx + dy * dy)
	}
	
	def createPolygon(xs:List[Point]) =
		xs match {
			case x::y::z::Nil => Triangle(dist(x,y), dist(y,z), dist(z,x))
			case x::y::z::w::Nil => Quadrilateral(dist(x,y), dist(y,z), dist(z,w), dist(w,x))
			case _ => xs
		}

	def numSides(p:Any) = 
		p match {
			case t:Triangle => 3
			case q:Quadrilateral => 4
			case _ => 0
		}

	def perimeter(p:Any) =
		p match {
			case t:Triangle => t.a + t.b + t.c
			case q:Quadrilateral => q.a + q.b + q.c + q.d
			case _ => 0
		}
}

object App{
	def main(args:Array[String]){
		import Polygon._
		val t = createPolygon(Point(0.0,0.0) :: Point(3.0, 0.0) :: Point(0.0, 4.0) :: Nil)
		println("Number of sides = " + numSides(t))
		println("Perimeter = " + perimeter(t))

		val q = createPolygon(Point(0.0,0.0) :: Point(3.0, 0.0) :: Point(3.0, 4.0) :: Point(0.0, 4.0) :: Nil)
		println("Number of sides = " + numSides(q))
		println("Perimeter = " + perimeter(q))
	}
}