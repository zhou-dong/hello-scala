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

    val maps = assetLines.map(line => extractAsset(line))
    maps.union(adLines.map(line => extractAd(line)))

    val result = maps.reduceByKey((a, b) => a.merge(b))

    result.collect.foreach(println);
  }

  class States(val asset: Int, val view: Int, val click: Int) {
    def merge(other: States): States = {
      return new States(asset + other.asset, view + other.view, click + other.click)
    }
  }

  implicit val formats = DefaultFormats

  def extractAsset(line: String): (String, States) = {
    val json = (getJson(line))(1)
    return (getPv(json), new States(1, 0, 0));
  }

  def extractAd(line: String): (String, States) = {
    val json = (getJson(line))(1)
    getEvent(json) match {
      case "view" => return (getPv(json), new States(0, 1, 0))
      case "click" => return (getPv(json), new States(0, 0, 1))
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