package exercises 

import scala.collection.immutable.{Stream => _}

object ch5 {
	
	sealed trait Stream[+A]{
		def toList: List[A] 		
		def takeWhile(p: A => Boolean): Stream[A]
		def takeN(n: Int): Stream[A]
		def forAll(p: A => Boolean): Boolean
		def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
			case Cons(h,t) => f(h(), t().foldRight(z)(f))
			case _ 		   => z
		}
	}
	case object Empty extends Stream[Nothing] {
		def toList: List[Nothing] 				  			  = Nil
		def takeWhile(p: Nothing => Boolean): Stream[Nothing] = Empty
		def takeN(n: Int): Stream[Nothing]					  = Empty
		def forAll(p: Nothing => Boolean): Boolean			  = true
	}
	case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A] {
		def toList: List[A] 					  = h() :: t().toList
		def takeWhile(p: A => Boolean): Stream[A] = {
			if( p(h()) ) {
				Cons(h, () => t().takeWhile(p))
			}
			else {
				Empty
			}
		}
		def takeN(n: Int): Stream[A]	          = {
			if(n <= 0) {
				Empty
			}
			else {
				Cons(h, () => t().takeN(n - 1))
			}
		}		
		def forAll(p: A => Boolean): Boolean = {
			if( p( h() ) ) {
				t().forAll(p)
			}
			else {
				false
			}
		}		
	}

	object Stream {
		def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
			lazy val head = hd
			lazy val tail = tl
			Cons(() => head, () => tail)
		}
		def empty[A]: Stream[A] 		= Empty
		def apply[A](as: A*): Stream[A] =
			if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))			
	}	

}