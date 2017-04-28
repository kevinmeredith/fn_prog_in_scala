package exercises

object ch6 {
	
	trait RNG {
		def nextInt: (Int, RNG)
		def nonNegativeInt: (Int, RNG) = {
			val (newInt, newRng) 		= nextInt
			if (newInt == Int.MinValue) nonNegativeInt
			else 						{
				val negative = -(Math.abs(newInt))
				(negative, newRng)
			}
		}
	}

}