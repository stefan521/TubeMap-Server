package controllers

import actors.MyWebSocketActor
import akka.actor.ActorSystem
import akka.stream.Materializer

import javax.inject._
import play.api._
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import scala.concurrent.Future


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class Application @Inject()(
  config: Configuration,
  val controllerComponents: ControllerComponents
)(implicit system: ActorSystem, mat: Materializer) extends BaseController {

  private val logger = Logger(this.getClass).logger

  def index(): Action[AnyContent] =
    Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index())
    }

  def status(): Action[AnyContent] =
    Action { implicit request: Request[AnyContent] => {
      Ok("Works.")
    }}

  def socket = WebSocket.acceptOrResult[String, String] { request =>
    Future.successful {
      for {
        apiKey <- getApiKeyOrFailure()
        flow <- Right(ActorFlow.actorRef { out => MyWebSocketActor.props(out) })
      } yield flow
    }
  }

  private def getApiKey(): Option[String] = config.getOptional[String]("unified.api_key")

  private def getApiKeyOrFailure(): Either[Status, String] = {
    getApiKey() match {
      case None =>
        logger.warn("The environment var TUBE_API_KEY does not contain an Unified API key.")
        Left(InternalServerError)

      case Some(key) =>
        Right(key)
    }
  }
}
