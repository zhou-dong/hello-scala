import scala.annotation.tailrec

// Higher-Order Functions with Anonymous and tail-recursion
{
  def sum(f: Int => Int, a: Int, b: Int): Int = {
    @tailrec
    def loop(a: Int, acc: Int): Int =
      if (a > b) acc
      else loop(a + 1, f(a) + acc)
    loop(a, 0)
  }

  def sumInts(a: Int, b: Int): Int = sum(x => x, a, b)
  def sumCubes(a: Int, b: Int): Int = sum(x => x * x * x, a, b)
  def sumFactorials(a: Int, b: Int): Int = sum(
    {
      def fact(x :Int): Int = if(x == 0) 1 else x * fact(x - 1)
      fact
    }, a, b)

  println(sumInts(1, 100))
  println(sumCubes(1, 4))
  println(sumFactorials(1, 4))
}

// Rewrite sum
{
  def sum(f: Int => Int): (Int, Int) => Int = {
    def sumF(a: Int, b: Int): Int = if (a > b) 0 else f(a) + sumF(a + 1, b)
    sumF
  }

  def fact(x: Int): Int  = if(x == 0) 1 else x * fact(x - 1)
  def sumInts: (Int, Int) => Int = sum(x => x)
  def sumCubes = sum(x => x * x * x)
  def sumFactorials = sum(fact)

  println(sumInts(1, 100))
  println(sumCubes(1, 4))
  println(sumFactorials(1, 4))
}

// Consecutive Stepwise Applications
{
  def sum(f: Int => Int): (Int, Int) => Int = {
    def sumF(a: Int, b: Int): Int = if (a > b) 0 else f(a) + sumF(a + 1, b)
    sumF
  }

  def fact(x: Int): Int  = if(x == 0) 1 else x * fact(x - 1)
  println(sum(x => x)(1, 100))
  println(sum(x => x * x * x)(1, 4))
  println(sum(fact)(1, 4))
}

// Multiple Parameter Lists
{
  def sum(f: Int => Int)(a: Int, b: Int): Int = {
    if (a > b) 0 else f(a) + sum(f)(a + 1, b)
  }

  def fact(x: Int): Int  = if(x == 0) 1 else x * fact(x - 1)
  println(sum(x => x)(1, 100))
  println(sum(x => x * x * x)(1, 4))
  println(sum(fact)(1, 4))
}