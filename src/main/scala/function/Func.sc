// all functions in scala are object with apply method

trait Func[A, B] {
  def apply(x: A): B
}

val squareFunc = new Func[Int, Int] {
  override def apply(x: Int) = x * x
}

squareFunc(10)