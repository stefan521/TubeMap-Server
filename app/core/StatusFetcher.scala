package core

import akka.actor.ActorSystem

import scala.concurrent.Future
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import model.Line

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{JsResult, _}


object StatusFetcher {

  val request = HttpRequest(uri = "https://api.tfl.gov.uk/Line/Mode/tube/Status")

  import model.JsonReads._

  private def jsResultToEither[A](result: JsResult[A]): Either[Throwable, A] =
    result match {
      case JsSuccess(successfulResult: A, _: JsPath) =>
        Right(successfulResult)

      case JsError(_) =>
        Left(new IllegalArgumentException(s"Failed to read Json $result"))
    }

  private def parseTubeStatusJson(
    res: HttpResponse
  )(
    implicit system: ActorSystem
  ): Future[Either[Throwable, List[Line]]] =
    Unmarshal(res).to[String].map { data =>
      jsResultToEither(Json.fromJson[List[Line]](Json.parse(data)))
    }

  def fetchStatus(implicit system: ActorSystem): Future[Either[Throwable, List[Line]]] =
    Http()
      .singleRequest(request)
      .flatMap(parseTubeStatusJson)

}
