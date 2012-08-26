import scala.collection.mutable.HashMap

case class SearchResult(title:String, link:String, content:String) {
	def toHtml = "<a href='" + link + "'>" + title + "</a><div>"+content+"</div>"
}

object SearchEngine {
	def searchResultToHtml(result:SearchResult) =
		"<a href='" + result.link + "'>" + result.title + "</a><div>" + result.content + "</div>"

	def doSearch(keyword:String, makeUrl:String=>String, parser:String=>List[SearchResult]) = 
		parser(Http.doGetRequest(makeUrl(keyword)))
}

object WebSearch {
	import java.net.URLEncoder._
	import scala.util.parsing.json._

	def makeUrl(keyword:String) = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q="+encode(keyword, "UTF-8")
	def parser(response:String) = {
		val resp = JSON.parseFull(response).get.asInstanceOf[Map[String,Any]]
		resp("responseStatus") match {
			case 200.0 => resp.get("responseData") match {
					case m:Map[String,Any] => m.get("results") match {
							case results:List[Map[String,String]] =>
								results.map( result => SearchResult(result("title"), result("url"), result("content")))
							case _ => List.empty[SearchResult]
						}
					case _ => List.empty[SearchResult]
				}
			case _ => List.empty[SearchResult]
		}
	}

	def search(keyword:String) = SearchEngine.doSearch(keyword, makeUrl, parser)
}

object Http {
	import java.net._
	import java.io._

	def doGetRequest(url:String, headers:HashMap[String,String]=new HashMap[String,String]) = {
		val con = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
		headers.foreach((entry) => con.setRequestProperty(entry._1,entry._2))
		con.setDoOutput(true)
    	con.setUseCaches(false)
    	val sb = new StringBuffer
    	try{
	    	val reader = new BufferedReader(new InputStreamReader(con.getInputStream))
	    	var line = reader.readLine
	    	while (line != null){
	    		sb.append(line)
	    		line = reader.readLine
	    	}
    	} finally {
    		con.disconnect
    	}
    	sb.toString		
	}
}

object TwitterSearch{
	import scala.util.parsing.json._

	private sealed abstract object TrendType;

	private sealed object Daily extends TrendType;
	private sealed object Weekly extends TrendType;

	private def getUrlForType(type:TrendType) = type match {
		case Daily => "https://api.twitter.com/1/trends/daily.json"
		case Weekly => "https://api.twitter.com/1/trends/weekly.json"
	}

	def dailyTrends = getTrends(getUrlForType(Daily))

	def weeklyTrends = getTrends(getUrlForType(Weekly))

	private def getTrends(url:String) = 
		JSON.parseFull(Http.doGetRequest(url)).get.asInstanceOf[Map[String, Any]]("trends")
			.asInstanceOf[Map[String,Any]].head._2.asInstanceOf[List[Map[String,Any]]]
			.map( m => (m("name"), m("query")) ).asInstanceOf[List[Tuple2[String,String]]]
}

object TrendFacade{
	def getSearchResultsForTodaysTrends = getSearchResults(TwitterSearch.dailyTrends _)
	def getSearchResultsForThisWeeksTrends = getSearchResults(TwitterSearch.weeklyTrends _)
	private def getSearchResults(trends:()=>List[Tuple2[String,String]]) = 
		Map(trends().map(t => (t._1, WebSearch.search(t._2))))	
}

