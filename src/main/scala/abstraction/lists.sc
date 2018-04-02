import java.util.NoSuchElementException

trait List[T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
}

class Nil[T] extends List[T] {
  override def isEmpty = true
  override def head: Nothing = throw new NoSuchElementException("Nil.head")
  override def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  override def isEmpty = false
}

def singleton[T](elem: T) = new Cons[T](elem, new Nil[T])

def nth[T](n: Int, xs: List[T]): T =
  if(xs.isEmpty) throw new IndexOutOfBoundsException
  else if (n == 0) xs.head else nth(n-1, xs.tail)

val list = new Cons(1, new Cons(2, new Cons(3, new Nil)))

nth(2, list)
nth(-1, list)
nth(5, list)