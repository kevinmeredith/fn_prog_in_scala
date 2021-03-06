package exercises

import scala.{Option => _}

object ch4 {

	sealed abstract class Option[+A] {

		def map[B](f: A => B): Option[B] = this match {
			case Some(a) => Some( f(a) )
			case None    => None
		}

		def flatMap[B](f: A => Option[B]): Option[B] = this match {
			case Some(a) => f(a)
			case None    => None
		}

		def getOrElse[B >: A](default: => B): B = this match {
			case Some(a) => a
			case None 	 => default
		}

		def orElse[B >: A](ob: => Option[B]): Option[B] = this match {
			case s @ Some(_) => s
			case None 	     => ob
		}

		def filter(f: A => Boolean): Option[A] = this match {
			case s @ Some(a) => if( f(a) ) s else None
			case None 	     => None
		}

	}

	case class Some[+A](get: A) extends Option[A]
	case object None extends Option[Nothing]


	def variance(xs: Seq[Double]): Option[Double] = mean(xs).flatMap { m => 
		mean(varianceHelper(m, xs))
	}

	import java.lang.Math.pow

	private def varianceHelper(m: Double, xs: Seq[Double]): Seq[Double] = 
		xs.map(x => pow(x - m, 2) )

	private def mean(xs: Seq[Double]): Option[Double] = xs match {
		case Seq() => None
		case _     => Some( xs.sum / xs.length )
	}

	def map2[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = 
		a.flatMap(aa => b.map(bb => f(aa, bb) ))

    def sequence[A](a: List[Option[A]]): Option[List[A]] = a match {
        case Nil    => Some(Nil)
        case h :: t => h.flatMap { value =>
        	sequence(t).map { list => 
        		value :: list
        	}
        }
    }

    def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = 
    	a.foldRight[Option[List[B]]](Some(Nil)){ (elem, acc) =>
			for {
				a <- acc
				e <- f(elem)
			} yield e :: a
		}

}