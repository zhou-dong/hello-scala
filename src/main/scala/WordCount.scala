import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonAST.JValue
import org.json4s.jackson.Json
import org.json4s.JsonAST.JString
import java.io.File
import scala.io.Source

/**
 * @author DONG ZHOU
 */
object WordCount {

  def main(args: Array[String]) {
    val (assetsPath, adEventsPath, outputPath) = pathConfig()

    val conf = new SparkConf().setAppName("word-count").setMaster("local")
    val sc = new SparkContext(conf)

    val assets = sc.textFile(assetsPath)
    val ads = sc.textFile(adEventsPath)

    val mapped1 = assets.map(line => extractAsset(line))
    val mapped2 = ads.map(line => extractAd(line))
    val mapped = mapped1 ++ mapped2

    val reduced = mapped.reduceByKey((a, b) => reduce(a, b))
      .filter({ case (pv, states) => states.asset > 0 })
      .map({ case (pv, states) => s"$pv $states" })

    reduced.collect.foreach(println)

    delete(new File(outputPath))
    reduced.saveAsTextFile(outputPath)
  }

  class States(val asset: Int, val view: Int, val click: Int) extends Serializable {
    def merge(other: States): States = {
      return new States(asset + other.asset, view + other.view, click + other.click)
    }
    override def toString: String = asset + " " + view + " " + click
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

  def delete(file: File) {
    if (file.isDirectory) {
      file.listFiles.foreach(delete)
    }
    if (file.exists && !file.delete) {
      throw new Exception(s"Unable to delete ${file.getAbsolutePath}")
    }
  }

  def getPv(json: JValue): String = (json \ "pv").extract[String]
  def getEvent(json: JValue): String = (json \ "e").extract[String]
  def getJson(line: String): JValue = parse("[" + getLogContent(line) + "]")
  def getLogContent(line: String): String = line.substring(31)

  def pathConfig(): (String, String, String) = {
    val config = Source.fromFile("config.json").getLines().mkString
    val json = parse(config)
    val assetsPath = (json \ "asset_path").extract[String]
    val adPath = (json \ "ad_path").extract[String]
    val outputPath = (json \ "output_path").extract[String]
    return ("file://" + assetsPath, "file://" + adPath, outputPath);
  }

  def reduce(a: States, b: States): States = {
    if (a == null && b == null)
      return new States(0, 0, 0)
    if (a == null)
      return b;
    if (b == null)
      return a;
    return a.merge(b)
  }

}