import Config.EsType
import org.elasticsearch.search.SearchHit

/** Created by dozhou on 6/10/17. */
object Analyze {

  def main(args: Array[String]): Unit = {
    evaluateType(Config.property)
    evaluateType(Config.packages)
  }

  private def evaluateType(esType: EsType): Unit = {
    (for (i <- 1 to 4) yield  i).map(minTermFreq => {
      evaluate(esType, minTermFreq)
    })
  }

  private def evaluate(esType: EsType, minTermFreq: Int): Double = {
    val startTime = System.currentTimeMillis()
    def standDeviation(numbers: Seq[Float]): Double = {
      val numSeq = numbers.filterNot(_.isNaN)
      val mean: Float = numSeq.sum / numSeq.length
      val variance = numSeq.map(num => math.pow(num-mean, 2)).sum / numSeq.length
      math.sqrt(variance)
    }

    var page = 0
    var searchHits: Seq[SearchHit] = esType.matchAll(page)
    val averages =  scala.collection.mutable.ArrayBuffer.empty[Float]
    while(searchHits.nonEmpty){
      searchHits.map(searchHit => averages += esType.similarItems(searchHit.getId, minTermFreq).average)
      searchHits = esType.matchAll({page+=1; page})
    }

    val duration = (System.currentTimeMillis() - startTime ) / 1000
    val result = standDeviation(averages)
    println(s"${esType.name}, $minTermFreq, $duration-seconds, $result")
    result
  }

}
