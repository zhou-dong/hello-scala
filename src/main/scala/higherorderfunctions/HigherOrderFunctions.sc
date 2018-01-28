import scala.annotation.tailrec

// Basic version of sums
{
  def cube(x: Int): Int = x * x * x
  def fact(x :Int): Int = if(x == 0) 1 else x * fact(x - 1)

  // Take the sum of integers between a and b
  def sumInts(a: Int, b: Int): Int =
    if (a > b) 0 else a + sumInts(a + 1, b)

  def sumCubes(a: Int, b: Int): Int =
    if (a > b) 0 else cube(a) + sumCubes(a + 1, b)

  def sumFactorials(a: Int, b: Int): Int =
    if (a > b) 0 else fact(a) + sumFactorials(a + 1, b)

  println(sumInts(1, 100))
  println(sumCubes(1, 4))
  println(sumFactorials(1, 4))
}

// Higher-Order Functions
{
  def sum(f: Int => Int, a: Int, b: Int): Int =
    if (a > b) 0 else f(a) + sum(f, a + 1, b)

  def id(x: Int): Int = x
  def cube(x: Int): Int = x * x * x
  def fact(x: Int): Int = if (x == 0) 1 else x * fact(x - 1)

  def sumInts(a: Int, b: Int): Int = sum(id, a, b)
  def sumCubes(a: Int, b: Int): Int = sum(cube, a, b)
  def sumFactorials(a: Int, b: Int): Int = sum(fact, a, b)

  println(sumInts(1, 100))
  println(sumCubes(1, 4))
  println(sumFactorials(1, 4))
}

// Higher-Order Functions with Anonymous
{
  def sum(f: Int => Int, a: Int, b: Int): Int =
    if (a > b) 0 else f(a) + sum(f, a + 1, b)

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

// Higher-Order Functions implement tail-recursion
{
  def sum(f: Int => Int, a: Int, b: Int): Int = {
    @tailrec
    def loop(a: Int, acc: Int): Int =
      if (a > b) acc
      else loop(a + 1, f(a) + acc)
    loop(a, 0)
  }

  println(sum(x => x * x, 3, 5))
}
