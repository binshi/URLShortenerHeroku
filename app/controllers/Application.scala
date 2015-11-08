package controllers
import models.URL
import models.Users
import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import java.util.Calendar


object Application extends Controller {
  
  val urlForm = Form(
    "URL" -> nonEmptyText)
    
/*
 * Initial form load    
 */
  def index = Action {
    Ok(views.html.index(urlForm, null, null, null))
  }
  
  /*
   * Actions for submit and display buttons
   */
  def shortenURLAsPage = Action { implicit request =>
    val actionType = request.queryString.get("actionType").flatMap(_.headOption).getOrElse(0)
    
    if (actionType == "Submit") {
      urlForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(errors, null, null, null)),
      longURL => {
        Ok(views.html.index(urlForm, URL.shorten(longURL), null, null))
      })
    } else if  (actionType == "Display") {
      urlForm.bindFromRequest.fold(
    errors => BadRequest(views.html.index(errors, null, null, null)),
      shortURL => {
        val listUsers: List[String] = Users.displayByShortURL(shortURL)
        val mapUsers = listUsers.foldLeft[Map[String, Int]](Map.empty)((m, c) => m + (c -> (m.getOrElse(c, 0) + 1)))
        Ok(views.html.index(urlForm, null, mapUsers, shortURL))
      })
    } else {
        Ok(views.html.index(urlForm, null, null, null))
    }
  }
  
  /*
   * Redirect to the original URL based on the short URL
   */
  def redirectToLongURL(shortURL: String) = Action { implicit request =>
      val listURLs: List[String] = URL.findByShortURL(shortURL)
      var address = request.remoteAddress
      if (address == "0:0:0:0:0:0:0:1") address = "127.0.0.1"
      
      val cal = Calendar.getInstance()
    	val currentDate = cal.getTime()
    	var longURL = listURLs.head
    	
    	URL.insertUser(shortURL, address, currentDate)
    	URL.incrementClicks(shortURL)
    	Ok(views.html.redirect(longURL))
    }
}