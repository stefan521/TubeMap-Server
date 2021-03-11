package core

import akka.NotUsed
import akka.actor.ActorSystem

import scala.concurrent.Future
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt


/*
  TODO: Maybe implement this as an Akka Cluster Singleton Actor.
 */
object StatusFetcher {

  def fetchStatus(implicit system: ActorSystem): Future[String] =
    for {
      response <- Http().singleRequest(HttpRequest(uri = "http://localhost:9000/status"))
      strict <- response.entity.toStrict(2.seconds)
    } yield strict.data.utf8String
}
