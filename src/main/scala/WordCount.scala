import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object WordCount {

  def main(args: Array[String]) {

    var minPartitions = 5;

    val conf = new SparkConf().setAppName("work-count").setMaster("local")
    val sc = new SparkContext(conf)

    val assetsPath = "file:///Users/zhoudong/Downloads/assets_2014-01-20_00_domU-12-31-39-01-A1-34"
    val adEventsPath = "file:///Users/zhoudong/Downloads/ad-events_2014-01-20_00_domU-12-31-39-01-A1-34"

    val assetLines = sc.textFile(assetsPath, minPartitions)
    val adLines = sc.textFile(adEventsPath, minPartitions)

    SQLContext
    
    val lineLengths = assetLines.map(s => s.length)
    val totalLength = lineLengths.reduce((a, b) => a + b)

    println(lineLengths)
    println(totalLength)

    val numAs = assetLines.filter(line => line.contains("a")).count()
    val numBs = assetLines.filter(line => line.contains("z")).count()
    println(numAs)
    println(numBs)
    
    assetLines.filter(line => line.contains("a"));
  }

}