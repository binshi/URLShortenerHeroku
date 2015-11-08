## Versions
- Play version: 2.1.3
- JDK version: 1.7.0_79
- Scala version: 2.10
- Postgres version: 9.4.5
- Uses anorm and evolutions to create tables

## Repo and deployment
- Git repository: https://github.com/binshi/URLShortenerHeroku
- Heroku deployment: https://urlshortenerplay.herokuapp.com/

## Endpoints
The user input form that accepts a user-specified url (original or shortened) and has a submit and display button. 
- [Submit click]: When user inputs original URL and hits the submit button, shortened URL is presented to the user.
- [Short url click]: When the user clicks on the shortened URL he is taken to the website of the original url.
- [Display click]: When user inputs shortened URL and hits the display button, the ipaddress and hits for the shortened url are presented to the user.

## Code
#### Model
- [URL.scala]: Maps to shortURL table in database. Contains algorithm to shorten URL 
- [Users.scala]: Maps for users table in database. 

#### Controller
- [Application.scala]: Controller for submit, display and short url to orginal url actions 

#### Views
- [index.scala.html]: User input form with submit and display buttons. Also used for presenting results
- [redirect.scala.html]: Redirects to original url page when clicked on short url

#### evolutions/default
- [1.sql]: Creates database tables shorturl and users via anorm

[URL.scala]: <https://github.com/binshi/URLShortenerHeroku/blob/master/app/models/URL.scala>
[Users.scala]: <https://github.com/binshi/URLShortenerHeroku/blob/master/app/models/Users.scala>
[Application.scala]: <https://github.com/binshi/URLShortenerHeroku/blob/master/app/controllers/Application.scala>
[index.scala.html]: <https://github.com/binshi/URLShortenerHeroku/blob/master/app/views/index.scala.html>
[redirect.scala.html]: <https://github.com/binshi/URLShortenerHeroku/blob/master/app/views/redirect.scala.html>
[1.sql]: <https://github.com/binshi/URLShortenerHeroku/blob/master/conf/evolutions/default/1.sql>

