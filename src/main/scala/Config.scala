import java.net.InetAddress

import org.elasticsearch.action.search.{SearchRequestBuilder, SearchType}
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder.Item
import org.elasticsearch.index.query.{MoreLikeThisQueryBuilder, QueryBuilder, QueryBuilders}
import org.elasticsearch.search.SearchHit
import org.elasticsearch.transport.client.PreBuiltTransportClient

/** Created by dozhou on 6/10/17. */
object Config {

  final val pageSize: Int = 100
  final val similarItemsSize = 20
  final val transportPort: Int = 9300
  final val httpPort: Int = 9200
  final val indexName: String = "discovery"
  final val clusterName: String = "elasticsearch_dozhou"
  final val host: String = "localhost"
  lazy val property: EsType = PropertyType()
  lazy val packages: EsType = PackageType()
  lazy val transportClient: TransportClient = createTransportClient()
  final val stopWords: Array[String] = Array("http", "https", "www", "com")

  trait EsType {
    def name: String
    def fields: Array[String]
    def similarItems(id: String, minTermFreq: Int): SimilarItems = {
      val searchHits: Seq[SearchHit] = query(0, similarItemsSize, mltQuery(id, minTermFreq))
      val scoreItems: Seq[ScoreItem] = searchHits.map(searchHit => ScoreItem(searchHit.getId, searchHit.getScore))
      val average: Float = searchHits.map(searchHit => searchHit.score).sum / searchHits.length
      SimilarItems(id, average, scoreItems)
    }
    def matchAll(page: Int): Seq[SearchHit] = {
      val from = page * pageSize
      // Elastic Search limit
      if(from  + pageSize >= 10000) List.empty else {
        query(page * pageSize, pageSize, QueryBuilders.matchAllQuery())
      }
    }
    def query(from: Int, size: Int, queryBuilder: QueryBuilder): Seq[SearchHit] = {
      requestBuilder(from, size).setQuery(queryBuilder).get().getHits.getHits
    }
    private def mltQuery(id: String, minTermFreq: Int): MoreLikeThisQueryBuilder = {
      QueryBuilders.moreLikeThisQuery(fields, Array.empty, Array(new Item(indexName, name, id))).minTermFreq(minTermFreq)
    }
    private def requestBuilder(from: Int = 0, size: Int): SearchRequestBuilder = {
      transportClient.prepareSearch(Config.indexName).setTypes(name)
        .setSearchType(SearchType.DEFAULT).setFrom(from).setSize(size)
    }
  }

  case class ScoreItem(id: String, score: Float)
  case class SimilarItems(id: String, average: Float, scoreItems: Seq[ScoreItem])

  private case class PropertyType() extends EsType {
    override def name: String = "property"
    override def fields: Array[String] = {
      Array(
        "name", "property.name", "property.name.raw", "property.name.camel_case",
        "description", "display_urls", "content_types.name",
        "publisher.name", "publisher.name.raw", "publisher.name.camel_case"
      )
    }
  }

  private case class PackageType() extends EsType {
    override def name: String = "package"
    override def fields: Array[String] = {
      Array(
        "name", "name.raw", "name.camel_case",
        "description", "display_urls", "content_types.name",
        "publisher.name", "publisher.name.raw", "publisher.name.camel_case"
      )
    }
  }

  private def createTransportClient(): TransportClient = {
    new PreBuiltTransportClient(
      Settings.builder().put("cluster.name", Config.clusterName).build()
    ).addTransportAddress(
      new InetSocketTransportAddress(InetAddress.getByName(Config.host), Config.transportPort)
    )
  }

}
