package main.scala

import scala.collection.JavaConversions._
import scala.io.Source

import com.github.pathikrit.dijon._

/**
 * Grabs info about movies and outputs to cvs so I can import into existing spreadsheets.
 * 
 * @author tmckinnon
 *
 */
object scrapeMoiveInfo {
  
  /** Using the open movie DB API to get info. */
  val omdbUrl = "http://www.omdbapi.com/?i=&t=";
  
  /**
   * @param args
   *    0 - Absolute path to file with movie title list (each line is a new title)
   */
  def main(args: Array[String]) {
    val movies = readMovieList(args(0))
    movies.map(x => handleMovie(x))
  }

  /** Handle each movie. */
  def handleMovie(movie: String) {
    val formattedTitle = formatTitle(movie)
    val _url = omdbUrl + formattedTitle
    val str = Source.fromURL(_url).getLines().next()
    val json = parse(str)
//    val json = json"""{"Title":"10 Things I Hate About You","Year":"1999","Rated":"PG-13","Released":"31 Mar 1999","Runtime":"97 min","Genre":"Comedy, Drama, Romance","Director":"Gil Junger","Writer":"Karen McCullah, Kirsten Smith","Actors":"Heath Ledger, Julia Stiles, Joseph Gordon-Levitt, Larisa Oleynik","Plot":"A new kid must find a guy to date the meanest girl in school, the older sister of the girl he has a crush on, who cannot date until her older sister does.","Language":"English, French","Country":"USA","Awards":"2 wins & 12 nominations.","Poster":"http://ia.media-imdb.com/images/M/MV5BMTI4MzU5OTc2MF5BMl5BanBnXkFtZTYwNzQxMjc5._V1_SX300.jpg","Metascore":"70","imdbRating":"7.2","imdbVotes":"160,025","imdbID":"tt0147800","Type":"movie","Response":"True"}"""
//    """{"Response":"False","Error":"Movie not found!"}"""
    if (json.Title == None) printError(movie, formattedTitle, json.Error.toString) else printResult(json.Title.toString, json.Year.toString, json.Rated.toString, json.Genre.toString)
  }

  def printError(title: String, formattedTitle: String, error: String) {
    println(title + "->" + formattedTitle + " " + error)
  }
  
  def printResult(title: String, year: String, rated: String, genre: String) {
    println((title + " | " + year + " | " + rated + " | " + genre).replaceAll("\"", ""))
  }
  
  def readMovieList(movieListFile: String): Array[String] = {
    Source.fromFile(movieListFile).getLines.toArray
  }

  /** Put movie title into format that omdb wants. */
  def formatTitle(title: String): String = {
    //10+things+i+hate+about+you
    // my list has some marked with foot notes...
    val tmp = moveMisplacedArticles(if (title.endsWith("*")) title.substring(0, title.length() - 1) else title)
    tmp.replaceAll(" ", "+")
  }

  /** Move "The" back to the front of the title. */
  def moveMisplacedArticles(title: String): String = {
    title.replaceFirst(", The", "")
    if (title.endsWith(", The")) "The " + title.replaceFirst(", The", "") else title
  }
}