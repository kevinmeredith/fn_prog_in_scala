package exercises

object Ch2 {

import scala.annotation.tailrec

	// tail recursive fibonacci to get n-th element
	// 0 1 1 2 3 5 8 13 ... 
	def fib(n: Int): Option[Int] = {
		@tailrec
		def go(count: Int, prev: Int, acc: Int): Option[Int] = {
			if   (count == n) Some( acc )
			else 			  go(count+1, acc, acc + prev)
		}
		if      ( n < 0)   None
		else if ( n == 0 ) Some( 0 )
		else if ( n == 1)  Some ( 1 )
		else    		   go(2, 1, 1)
	}

	// assuming ascending order
	def isSorted[A](as: List[A], ordered: (A,A) => Boolean): Boolean = as match { 
		case _ :: Nil => true
		case _ :: xs => {
			val zipped: List[(A, A)] = as.zip(xs) 
			zipped.forall{ x: (A, A) => ordered( x._1, x._2 ) }
		}
		case Nil    => true
	}

	// using `tailOption` instead
	def isSorted2[A](as: List[A], ordered: (A,A) => Boolean): Boolean = tailOption( as ) match {
		case None 	   => true
		case Some (xs) => {
			val zipped: List[(A, A)] = as.zip(xs)
			zipped.forall{ x: (A, A) => ordered( x._1, x._2 ) }
		}
	}

	// isn't this standard?
	def tailOption[A](xs: List[A]): Option[List[A]] = xs match {
		case Nil      => None
		case _ :: Nil => None
		case _ :: xs  => Some( xs ) 
	}

	def curry[A,B,C](f: (A, B) => C): A => (B => C) = 
		(x: A) => f(x, _)

	def uncurry[A,B,C](f: A => B => C): (A, B) => C = 
		(x: A, y: B) => f(x)(y)

	def compose[A,B,C](f: B => C, g: A => B): A => C = 
		(x: A) => f( g( x ) )



}