object HelloWorld {

  def main(args: Array[String]): Unit = {
    println("Hello, world!")

    val str = Source.fromFile("config.json").getLines().mkString
    println(str)
  }

}