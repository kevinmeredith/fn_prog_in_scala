package exercises

object List {
	sealed trait List[+A]
	case object Nil extends List[Nothing]
	case class Cons[A](x: A, xs: List[A]) extends List[A]

	def apply[A](xs: A*): List[A] = 
		if(xs.isEmpty) Nil
		else Cons(xs.head, apply(xs.tail: _*))
}