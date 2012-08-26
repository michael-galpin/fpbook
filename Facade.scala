import scala.collection.mutable.HashMap

class SearchResult(val title:String, val link:String, val content:String){
	def toHtml = "<a href='" + link + "'>" + title + "</a><div>"+content+"</div>"
}

class GoogleSearch{
	val apiKey = "AIzaSyCdoBNO3VC3q3c8DmzkAKk8s6z_9wf1-ws"
	def search(keyword:String):List[SearchResult] = {
		import java.net.URLEncoder._
		import scala.util.parsing.json._

		val req = new HttpGetRequest("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q="+encode(keyword, "UTF-8"))
		// println("req=" + req)
		val resp = JSON.parseFull(req.response).get.asInstanceOf[Map[String,Any]]
		// println("resp=" + resp)
		if (resp("responseStatus")  != 200.0) return List.empty[SearchResult]
		val data = resp.getOrElse("responseData", Map.empty[String, Any]).asInstanceOf[Map[String,Any]]
		// println("data=" + data)
		val results = data.getOrElse("results", List.empty[Map[String,String]]).asInstanceOf[List[Map[String,String]]]
		// println("results=" + results)
		results.map( result => new SearchResult(result("title"), result("url"), result("content")))
	}
}

class HttpGetRequest(url:String, headers:HashMap[String,String]=new HashMap[String,String]){
	import java.net._
	import java.io._

	def response = {
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

class TwitterSearch{
	import scala.util.parsing.json._

	def dailyTrends = getTrends("https://api.twitter.com/1/trends/daily.json")

	def weeklyTrends = getTrends("https://api.twitter.com/1/trends/daily.json")

	private def getTrends(url:String) = {
		val req = new HttpGetRequest(url)
		val obj = JSON.parseFull(req.response).get.asInstanceOf[Map[String,Any]]
		val trends = obj("trends").asInstanceOf[Map[String,Any]].head._2.asInstanceOf[List[Map[String,Any]]]
		trends.map( m => (m("name"), m("query")) ).asInstanceOf[List[Tuple2[String,String]]]
	}
}

class TrendFacade{
	private[this] val twitter = new TwitterSearch
	private[this] val google = new GoogleSearch
	def getSearchResultsForTodaysTrends = {
		val results = new HashMap[String, List[SearchResult]]
		val trends = twitter.dailyTrends
		trends.foreach(t => results(t._1) = google.search(t._2))
		results
	}
	def getSearchResultsForThisWeeksTrends = {
		val results = new HashMap[String, List[SearchResult]]
		val trends = twitter.weeklyTrends
		trends.foreach(t => results(t._1) = google.search(t._2))
		results
	}
}

