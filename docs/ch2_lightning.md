# Functional Programming (FP) in Scala

## Chapter 2 - Getting Started w/ FP in Scala

## Agenda

* Basic Elements of Scala/FP
* Higher-Ordered Functions (HOF) 
* Tail Recursive Functions
* Following types to implementations

### Basic Elements of Scala 

```scala
object MyModule {
	def abs(n: Int): Int = 
		if (n < 0) -n
		else n

	private def formatAbs(x: Int) = {
		val msg = "The absolute value of %d is %d"
		msg.format(x, abs(x))
	}

	def main(args: Array[String]): Unit = 
		println(formatAbs(-42))
}
```

source - FP in Scala, ch2

#### Purity?

* Recall from ch1's "Referential transparency and purity" note

> An expression e is referentially transparent if, for all programs p,
> all occurrences of e in p can be replaced by the result of evaluating 
> e without affecting the meaning of p.

Example: 

```
factorial(3) = 3 * factorial(2)
             = 3 * 2 * factorial(1)
             = 3 * 2 * 1
             = 6
```

* `abs` and `formatAbs` are pure
* `main` is not

#### Comments

* `val` is an immutable reference
* "Usually a return type of Unit is a hint that the 
   method has a side effect" (FP in Scala, ch2).
* "An object whose primary purpose is giving its members 
   a namespace is sometimes called a module" (FP in Scala, ch2).


### Higher-Order Functions (HOF)

* "functions are values. And just like values of other types—such as 
   integers, strings, and lists—function can be assigned to variables, 
   stored in data structures, and passed as arguments to functions" (FP in Scala, ch2)   
* function that accepts or returns a function (https://en.wikipedia.org/wiki/Higher-order_function)

#### Writing `factorial` Functionally

##### Java Implementation

``` java
public static int factorial(int x) {
  int start = 1;

  if(x <= 0) {
    return start;
  }
  else {
    while(x > 0) {
      start = x * start;
      x--;
    }
    return start;
  }
}
```

* Pure, but cannot reason about above code through evaluation by substitution
	* Must keep track of state

##### Pure, but Vulnerable to StackOverflowError

```scala
def factorial(n: Int): Int = {
	if(n <= 0) 1
	else       n * factorial(n - 1)
}
```

* StackOverflow Error?

```
scala> factorial(3)
res0: Int = 6

scala> factorial(5)
res1: Int = 120

scala> factorial(17)
res4: Int = -288522240

scala> factorial(35)
res3: Int = 0
```

* Let's switch to [BigInt](http://www.scala-lang.org/api/2.12.1/scala/math/BigInt.html)
	* "backed by the Java BigInteger ... classes"
	* Stored on heap
  * Can demonstrate the StackOverflowError

```scala
def factorial(n: Int): BigInt = {
	if(n <= 0) 1
	else       n * factorial(n - 1)
}
```

* StackOverflowError example

```
scala> factorial(10000)
res4: BigInt = 28462596809170545189064132121198688901480514017027992307941799942744113400037644437729907867577847758158840621423175288300423399401535187390524211613827161748198241998275924182892597878981242531205946599625986706560161572036032397926328736717055741975962099479720346153698119897092611277500484198845410475544642442136573303076703628825803548967461117097369578603670191071512730587281041158640561281165385325968425825995584688146430425589836649317059251717204276597407446133400054194052462303436869154059404066227828248371512038322178644627183822923899638992827221879702459387693803094627332292570555459690027875282242544348021127559019169425429028916907219097083690539873747452483372899521802363282741217040268086769210451555840567172555372015852132829034279989818449313610640381489...

scala> factorial(15000)
res5: BigInt = 27465990334851682664825581502626675377669983302658241873984787525304521079312253274085307332114446509830257090495822324265168899760335148841707118823471594703909649496852027862912498376364295133481754655242088033694620521332356125292359326450384834973251969823390805973539311117766718952501872218681514188914725225008861836664392451036562908524038308641493353542810103879177194430457674271422765085426230807494669286396585130025873185951192020065544185625900634280245705849133571146993828309595683803342876821167688693638388691778104923744721937476761715219150981882574774381258969952175424406514183115009998130714045583340472206988765168258445207802185528854876479150831624630237900310965318491312774416575518682559585376163480207071633944867107997601247843741689670671908325927839...

scala> factorial(20000)
java.lang.StackOverflowError
  at scala.math.BigInt$.int2bigInt(BigInt.scala:97)
  ...
```

##### Pure and Tail Recursive (no StackOverflowError's)

```scala
def factorial(n: Int): BigInt = {
  def go(n: Int, acc: BigInt): BigInt =
    if (n <= 0) acc
	else go(n-1, n*acc)
  go(n, 1)
}
```

* Pure and can reason about code through evaluation by substitution

```
factorial(3) = go(3, 1)
               go(2, 3*1)
               go(2, 3)
               go(1, 2*3)
               go(1, 6)
               go(0, 1*6)
               go(0, 6)
               6
```

* StackOverflowError?

```
scala> factorial(50000)
res5: BigInt = 33473205095971448369154760940714864779127732238104548077301003219901680221443656416973812310719169308798480438190208299893616384743066693742630572845363784038325756282123359987268244078235972356040853854441373383753568565536371168327405166076155165921406156075461294201790567479665498629242220022541553510718159801615476451810616674970217996537474972541139338191638823500630307644256874857271394651081909874909643486268589229807870031031008962861154553979911612940652327396971497211031261142860733793509687837355811830609551728906603833592532851635961730885279811957399495299450306354442478492641028990069559634883529900557676550929175475920788044807622562415165130459046318068517406766360012329556454065724225175473428183121029195715593787423641117194513838593038006413132976312508...
```

* "A call is said to be in tail position if the caller does nothing other than 
  return the value of the recursive call" (ch 2).
* "If all recursive calls made by a function are in tail position, Scala automatically compiles
  the recursion to iterative loops that don’t consume call stack frames for each
  iteration." (ch 2)

* `tailrec`

```
scala> :paste
// Entering paste mode (ctrl-D to finish)

@annotation.tailrec
def factorial(n: Int): Int = {
   if(n <= 0) 1
   else       n * factorial(n-1)
}

// Exiting paste mode, now interpreting.

<pastie>:15: error: could not optimize @tailrec annotated method factorial: it contains a recursive call not in tail position
          else       n * factorial(n-1)
                       ^
```  

### Polymorphic Functions

* Monomorphic function - "functions that operate on only one type of data" (ch2)
* Example: `def add2(x: Int): Int = x + 2`

```scala
def map[A, B](list: List[A], f: A => B): List[B] = {
  @annotation.tailrec
  def go(l: List[A], acc: List[B]): List[B] = l match {
    case h :: t => go(t, f(h) :: acc)
    case Nil    => acc
  }
  go(list, Nil).reverse
}
```
* `A` and `B` are type parameters.
* `f` is a HOF
* Since it compiled with the annotation, it's tail recursive


```
scala> map[String, Int](List("foo", "hi"), {s: String => s.length })
res6: List[Int] = List(3, 2)

scala> map[String, Int](List("foo", "hi", "world!"), {s: String => s.length })
res7: List[Int] = List(3, 2, 6)
```

### Following Types to Implementation

* "Follow the types!"

```scala
def compose[A, B, C](x: A, f: A => B, g: B => C): C = 
  g( f(x) )
```

```scala
scala> compose[String, Int, Boolean]("foo", {x: String => x.length}, {len: Int => if(len == 4) true else false} )
res9: Boolean = false

scala> compose[String, Int, Boolean]("bipp", {x: String => x.length}, {len: Int => if(len == 4) true else false} )
res10: Boolean = true
```

* [May Your Data be Coherent](https://www.youtube.com/watch?v=gVXt1RG_yN0)
