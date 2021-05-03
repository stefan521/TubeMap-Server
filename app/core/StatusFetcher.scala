package core

import akka.actor.ActorSystem

import scala.concurrent.Future
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import cats.data.EitherT
import cats.implicits.catsSyntaxEitherId
import cats.instances.future._
import core.JsonParser.jsResultToEither
import core.StatusCacher.cacheStatus
import model.{AppError, CacheResult, Line, TubeStatus}
import validation.ValidatedConfig

import scala.concurrent.ExecutionContext.Implicits.global


object StatusFetcher {

  val request: HttpRequest = HttpRequest(uri = "https://api.tfl.gov.uk/Line/Mode/tube/Status")

  import model.JsonReads._

  def fetchStatus(config: ValidatedConfig)(implicit system: ActorSystem): Future[Either[AppError, CacheResult]] =
    (
      for {
        newStatus <- EitherT(getNewStatus(system))
        parsedStatus <- EitherT(Future.successful(TubeStatus(newStatus).asRight[AppError]))
        _ = {
            println(s" API KEY ${config.apiKey} ")
        }
        cached <- EitherT(Future.successful(cacheStatus(parsedStatus)))
      } yield cached
    ).value

  private def parseTubeStatusJson(
    res: HttpResponse
  )(
    implicit system: ActorSystem
  ): Future[Either[AppError, List[Line]]] =
    Unmarshal(res).to[String].map { data =>
      jsResultToEither[List[Line]](data)
    }

  private def getNewStatus(implicit system: ActorSystem): Future[Either[AppError, List[Line]]] =
    Http()
      .singleRequest(request)
      .flatMap(parseTubeStatusJson)

}
