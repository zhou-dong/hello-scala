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

    val conf = new SparkConf().setAppName("word-count").setMaster("local")
    val sc = new SparkContext(conf)

    val assetsPath = "file:///Users/zhoudong/Downloads/assets_2014-01-20_00_domU-12-31-39-01-A1-34"
    val adEventsPath = "file:///Users/zhoudong/Downloads/ad-events_2014-01-20_00_domU-12-31-39-01-A1-34"

    val assetLines = sc.textFile(assetsPath)
    val adLines = sc.textFile(adEventsPath)

    val result = adLines.map(line => (extractAd(line), 1)).reduceByKey((a, b) => a + b)
    val assetResult = assetLines.map(line => extractAsset(line)).reduceByKey(_ + _);
    result.collect.foreach(println);
    assetResult.collect.foreach(println);

  }

  implicit val formats = DefaultFormats

  def extractAsset(line: String): (String, Int) = {
    val json = (getJson(line))(1)
    return (getPv(json: JValue), 1);
  }

  def extractAd(line: String): (String, String) = {
    val json = (getJson(line))(1)
    getEvent(json) match {
      case "view" => return (getPv(json), "view")
      case "click" => return (getPv(json), "click")
      case _ => return return (null, null)
    }
  }

  def getPv(json: JValue): String = {
    return (json \ "pv").extract[String]
  }

  def getEvent(json: JValue): String = {
    return (json \ "e").extract[String]
  }

  def getJson(line: String): JValue = {
    return parse("[" + getLogContent(line) + "]")
  }

  def getLogContent(line: String): String = {
    return line.substring(31)
  }

}