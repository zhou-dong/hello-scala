import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object WordCount {

  def main(args: Array[String]) {
    val path = "file:////Users/zhoudong/Downloadsassets_2014-01-20_00_domU-12-31-39-01-A1-34"
    val lines = sc.textFile(path)
    val lineLengths = lines.map(s => s.length)
    val totalLength = lineLengths.reduce((a, b) => a + b)

    val conf = new SparkConf().setAppName("work-count")
    val sc = new SparkContext(conf)

    val data = sc.textFile(path, 5).cache();
    val numAs = data.filter(line => line.contains("a")).count()
    val numBs = data.filter(line => line.contains("b")).count()
    println(numAs)
    println(numBs)
  }

}