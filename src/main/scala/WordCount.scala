import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonAST.JValue
import org.json4s.jackson.Json
import org.json4s.JsonAST.JString

/**
 * @author DONG ZHOU
 *
 * Should not use spark SQLContext, because both files are log format not JSON format.
 */
object WordCount {

  def main(args: Array[String]) {

    var minPartitions = 5;

    val conf = new SparkConf().setAppName("work-count").setMaster("local")
    val sc = new SparkContext(conf)

    val assetsPath = "file:///Users/zhoudong/Downloads/assets_2014-01-20_00_domU-12-31-39-01-A1-34"
    val adEventsPath = "file:///Users/zhoudong/Downloads/ad-events_2014-01-20_00_domU-12-31-39-01-A1-34"

    val assetLines = sc.textFile(assetsPath, minPartitions)
    val adLines = sc.textFile(adEventsPath, minPartitions)

    val lineLengths = assetLines.map(s => s.length)
    val totalLength = lineLengths.reduce((a, b) => a + b)

    println(lineLengths)
    println(totalLength)

    val numAs = assetLines.filter(line => line.contains("a")).count()
    val numBs = assetLines.filter(line => line.contains("z")).count()
    println(numAs)
    println(numBs)

    val r = adLines.map(line => execute(line)).reduce((a, b) => a + b);
    println(r)

  }

  def execute(line: String): String = {
    val json = getJson(line)
    println(json(1))
    println(isInterestedEvents(json(1)))
    return "p"
  }

  def isInterestedEvents(json: JValue): Boolean = {
    implicit val formats = DefaultFormats
    val event = (json \ "e").extract[String]
    return (event == "view" || event == "click")
  }

  def getJson(line: String): JValue = {
    return parse("[" + getLogContent(line) + "]")
  }

  def getLogContent(line: String): String = {
    return line.substring(31)
  }

}