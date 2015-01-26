package exercises

object Ch3 {

	import exercises.List._

	// @throws NoSuchElementException
	def head[A](l: List[A]): A = l match {
		case Cons(x, _) => x
		case _          => throw new NoSuchElementException("no head!")
	}

	def tail[A](l: List[A]): List[A] = l match {
		case Cons(_, xs) => xs
		case _           => throw new NoSuchElementException("calling tail on empty list!")
	}

	def setHead[A](new_head: A, l: exercises.List.List[A]): exercises.List.List[A] = l match {
		case Nil 		 => throw new Exception("empty list!")
		case Cons(x, xs) => Cons(new_head, xs)
	}

	def drop[A](n: Int)(l: exercises.List.List[A]): exercises.List.List[A] = l match { 
		case Nil         		    => exercises.List.Nil
		case Cons(x, xs) if (n > 0) => this.drop(n-1)(xs)
		case Cons(_, xs) if (n < 0) => xs
		case cons if (n == 0)       => cons
	}

	def dropWhile[A](f: A => Boolean)(l: exercises.List.List[A]): exercises.List.List[A] = l match {
		case Nil 				   => Nil
		case Cons(x, xs) if (f(x)) => exercises.Ch3.dropWhile(f)(xs)
		case cons				   => cons
	}

	// all but last element
	def init[A](l: exercises.List.List[A]): exercises.List.List[A] = l match {
		case Nil      	  => throw new Exception("empty list!")
		case Cons(_, Nil) => exercises.List.Nil
		case Cons(x, xs)  => Cons(x, exercises.Ch3.init(xs))
	}
}