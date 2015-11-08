package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import java.util.Date
import play.api.Play.current
import java.util.Calendar

case class Users(
    id: Pk[Long],
    shortURLPath: String, 
    ipaddr: String, 
    clicktime: Date
)

object Users {
    val users = {
      get[Pk[Long]]("id") ~
      get[String]("shortURLPath") ~
      get[String]("ipaddr") ~
      get[Date]("clicktime") map {
        case id~shortURLPath~ipaddr~clicktime => 
          Users(id, shortURLPath, ipaddr, clicktime)
      }
  }
    
   /*
   * Display IP Address and count hits based on the shortURL
   * .on('shortURLPath -> shortURLPath)
   */
  
    def displayByShortURL(shortURLPath: String) : List[String] = {
    println("shortURLPath" + shortURLPath)
    DB.withConnection { 
    implicit c =>
    SQL("SELECT * from userlist WHERE shortURLPath = {shortURLPath}")
    		 .on('shortURLPath -> shortURLPath)
    		 .as(str("ipaddr") *)
  }
  }
}