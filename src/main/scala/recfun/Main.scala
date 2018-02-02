package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int =
    if (c == 0 || c == r) 1 else pascal(c - 1, r - 1) + pascal(c, r - 1)

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    def helper(chars: List[Char], numOpens: Int):Boolean = {
      if(chars.isEmpty) numOpens == 0 else {
        val curr = chars.head match {
          case '(' => numOpens + 1
          case ')' => numOpens - 1
          case _ => numOpens
        }
        if(curr < 0) false else helper(chars.tail, curr)
      }
    }
    helper(chars, 0)
  }

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    money match {
      case 0  => 1
      case x if x < 0 => 0
      case x if x >= 1 && coins.isEmpty => 0
      case _ => countChange(money, coins.tail) + countChange(money - coins.head, coins)
    }
  }

}
