import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object WordCount {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("work-count").setMaster("local")
    val sc = new SparkContext(conf)

    val assetsPath = "file:///Users/zhoudong/Downloads/assets_2014-01-20_00_domU-12-31-39-01-A1-34"
    val lines = sc.textFile(assetsPath)
    val lineLengths = lines.map(s => s.length)
    val totalLength = lineLengths.reduce((a, b) => a + b)

    println(lineLengths)
    println(totalLength)

    val numAs = lines.filter(line => line.contains("a")).count()
    val numBs = lines.filter(line => line.contains("z")).count()
    println(numAs)
    println(numBs)
  }

}