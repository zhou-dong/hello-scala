// persistent data structure
abstract class IntSet {
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean
  def union(other: IntSet): IntSet
}

object Empty extends IntSet {
  override def incl(x: Int) = new NonEmpty(x, Empty, Empty)
  override def contains(x: Int) = false
  override def toString: String = "{}"
  override def union(other: IntSet) = other
}

class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  override def incl(x: Int) =
    if (x < elem) new NonEmpty(elem, left incl x, right)
    else if (x > elem) new NonEmpty(elem, left, right incl x)
    else this

  override def contains(x: Int) =
    if (x < elem) left contains x
    else if ( x > elem) right contains x
    else true

  override def toString: String = s"{$left <- $elem -> $right}"

  override def union(other: IntSet) = ((left union right) union other ) incl elem
}

val t1 = new NonEmpty(3, Empty, Empty)
val t2 = t1 incl 4
val t3 = new NonEmpty(5, Empty, Empty)

t1 union t2 union t3
t3 union t2 union t1
t2 union t3 union t1

t3.contains(5)
t3.contains(4)