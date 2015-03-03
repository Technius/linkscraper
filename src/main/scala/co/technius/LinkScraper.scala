package co.technius

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.{ Source => ScalaSource }

object LinkScraper {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorFlowMaterializer()

    val linkRegex = """<a[^>]*href=[\'"]?([^\'" >]+)""".r

    val src = Source(args.toList).
      mapAsync(url => Future(url -> ScalaSource.fromURL(url).mkString)).
      map { case (url, src) =>
        url -> linkRegex.findAllMatchIn(src).flatMap(_.subgroups).toList
      }

    val start = System.currentTimeMillis()
    src runForeach { case (url, links) =>
      println(s"$url has ${links.length} links.")
    } onComplete { case _ =>
      val elapsed = System.currentTimeMillis() - start
      println(s"scraping complete in $elapsed millis")
      system.shutdown()
    }
  }
}
