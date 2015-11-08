package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import java.util.Date
import play.api.Play.current
import java.util.Calendar

case class URL(
    id: Pk[Long],
    shortURLPath: String, 
    longURL: String, 
    numClicks: Long, 
    created: Date
)

object URL {
    val AlphaNum = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    val HTTPPrefix = "http://";
  
    val url = {
      get[Pk[Long]]("id") ~
      get[String]("shortURLPath") ~
      get[String]("longURL") ~
      get[Long]("numClicks") ~
      get[Date]("created") map {
        case id~shortURLPath~longURL~numClicks~created => 
          URL(id, shortURLPath, longURL, numClicks, created)
      }
  }
  
def shorten(longURL: String) : URL = { 
    
  
  val cal = Calendar.getInstance()  
    
    val shortURLList: List[String] = URL.findByLongURL(appendHttpToURL(longURL))
    val timeInMillis = cal.getTimeInMillis()
    val nowDate = cal.getTime()
    
    if (shortURLList.isEmpty) {
      val shortenedPath = getShortenedPath(timeInMillis)
      val newURL = URL(NotAssigned, shortenedPath, appendHttpToURL(longURL), 0, nowDate)
      create(newURL)
      return newURL
    } else {
      var existURL = URL(NotAssigned, shortURLList(0), appendHttpToURL(longURL), 0, nowDate)
      return existURL
    }
  }
  

  def getShortenedPath(timeInMillis : Long) : String = {
	var urlMillis = timeInMillis
  val base = AlphaNum.length()
	var value : String = ""
	var mod : Long = 0
	while (urlMillis != 0) {
	  mod = urlMillis % base
	  value = AlphaNum.substring(mod.toInt, (mod + 1).toInt) + value;
	  urlMillis /= base;
	}
	return value
  }
  
  def appendHttpToURL(url: String) : String = {
    if (!url.startsWith(HTTPPrefix)) {
      return HTTPPrefix + url
    }
    return url
  }
  
  def create(url: URL) { DB.withConnection { 
    implicit c =>
      SQL("INSERT INTO shorturl(shortURLPath,longURL,created) VALUES ({shortURLPath},{longURL},{created})").on(
        'shortURLPath -> url.shortURLPath, 'longURL -> url.longURL, 'created -> url.created
      ).executeUpdate()
    }
  }
  
  /*
   * Fetch the short URL if it already exists in the database
   */  
    def findByLongURL(longURL: String): List[String] =DB.withConnection { 
       implicit c => 
       SQL("select * from shorturl WHERE longurl = {longURL}")
    .on('longURL -> longURL)
    .as(str("shorturlpath") * )
}
    
  /*
   * Fetch long URL from the database
   */
  def findByShortURL(shortURLPath: String) : List[String] = DB.withConnection { 
    implicit c =>
    SQL("SELECT * from shorturl WHERE shortURLPath = {shortURLPath}")
    		 .on('shortURLPath -> shortURLPath)
    		 .as(str("longURL") * )
  }
  
  
  /*
   * Increment click each time the shortened URL is clicked
   */
  def incrementClicks(shortURLPath: String) { DB.withConnection { 
    implicit c =>
		SQL("UPDATE shorturl SET numClicks = numClicks+1 WHERE shortURLPath = {shortURLPath}")
		 .on('shortURLPath -> shortURLPath)
		 .executeUpdate()
     }
  }
  
   /*
   * Insert userlist table with shortURL, ipAddress and timestamp
   */
  def insertUser(shortURLPath: String, ipaddr: String, clicktime: Date) { DB.withConnection { 
    implicit c =>
		SQL("INSERT INTO userlist(shortURLPath,ipaddr,clicktime) VALUES ({shortURLPath},{ipaddr},{clicktime})")
		 .on('shortURLPath -> shortURLPath, 'ipaddr -> ipaddr, 'clicktime -> clicktime)
		 .executeUpdate()
     }
  }
}