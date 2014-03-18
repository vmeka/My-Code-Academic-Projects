
import com.amazon.advertising.api.sample.SignedRequestsHelper
import scala.io.Source
import scala.xml.XML
import scala.collection.immutable.TreeMap
import scala.collection.immutable.List
import java.io.IOException
import scala.collection.immutable.IndexedSeq
import scala.actors._
import scala.actors.Actor._

case class MappedBook(val isbn: String, val title: String, val salesrank: Long)	

def timeAndPrint(fetchMethod : List[String] => List[MappedBook], 
	typeOfProcess : String, isbnList : List[String]) {
	
	val startTime = System.nanoTime()
	val result = fetchMethod(isbnList)
	val totalTime =  (System.nanoTime() - startTime)/1.0e9
	
	val finalresult = result.foldLeft(Map[Long, List[String]]()){(mapper,combo) => { 
	 val isbnTitle =  List(combo.isbn, combo.title)
	  TreeMap(combo.salesrank -> isbnTitle ) ++ mapper }}
		
	printResult(typeOfProcess, finalresult, totalTime)
}

def getTitleRankSequential(isbnList : List[String]): List[MappedBook] = {
	val sequentialOutput = isbnList.map{isbn => parseXMLDocument(getSignedRequests(isbn), isbn) }
	sequentialOutput
}

def getTitleRankConcurrent(isbnList : List[String]): List[MappedBook] = {
	val caller = self
		isbnList.foreach{isbn => 
			actor{caller ! parseXMLDocument(getSignedRequests(isbn), isbn)}}
	val concurrentOutput = isbnList.map{(isbn) =>
		receive { case data: MappedBook => { 
			new MappedBook (data.isbn, data.title, data.salesrank)
			}
		}
	}
	concurrentOutput
}

def getSignedRequests(isbnNo : String) = {
	val helperReq = SignedRequestsHelper.getInstance("webservices.amazon.com",
		"************", "*************")

	val params : java.util.Map[String,String] =  new java.util.HashMap[String, String]()
	params.put("AssociateTag", "*******")
	params.put("Operation", "ItemLookup")
	params.put("ResponseGroup", "Large")
	params.put("SearchIndex", "Books")
	params.put("IdType", "ISBN")
	params.put("ItemId", isbnNo)

	val signedUrl = helperReq.sign(params)
	signedUrl
}

def parseXMLDocument(signedURL : String, isbnNo : String)= {
	try{
		val xmlString = Source.fromURL(signedURL).mkString
		val xml = XML.loadString(xmlString)

		val salesRank = (xml \\ "Items" \\ "Item" \\ "SalesRank").text
		val title = (xml \\ "Items" \\ "Item" \\ "ItemAttributes" \\ "Title").text
		val comboIsbnTitle = List(isbnNo, title)
		val mappedValue = Map((salesRank).toLong -> comboIsbnTitle)
		val isbn = isbnNo

		new MappedBook (isbn, title,salesRank.toLong)
		}catch{
		case ex: IOException => {
			new MappedBook(isbnNo, "Connection Interrupted", 0)
		}
		case ex: NumberFormatException => {
			new MappedBook(isbnNo, "Requested Book doesn't exist.", 1)
		}
	}
}

def printResult(process : String, finalOuput : Map[Long, List[String]], timeTaken : Double){
	println(" \n+++++++++++++++++++++++++++++++++++++++++++++++++++\n" + 
			process + " process. This took  " + timeTaken + " seconds " +
			" \n+++++++++++++++++++++++++++++++++++++++++++++++++++\n")
	println("Sales Rank \tISBN \t\t Title" + 
			" \n-----------------------------------------------------------------------")
	
	finalOuput.foreach(e => println(e._1 + " \t " + e._2(0) + " \t " + e._2(1))) 
}

val isbnList = Source.fromFile("isbn.txt").getLines.toList

timeAndPrint(getTitleRankSequential, "Sequential", isbnList)
timeAndPrint(getTitleRankConcurrent, "Concurrent", isbnList)