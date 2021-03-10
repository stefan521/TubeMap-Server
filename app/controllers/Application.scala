package controllers

import actors.MyWebSocketActor
import akka.actor.ActorSystem
import akka.stream.Materializer

import javax.inject._
import play.api._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import validation.ConfigValidation._

import scala.concurrent.Future


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class Application @Inject()(
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
      val result = for {
        _ <- getApiKeyOrThrowable(system.settings.config)
        flow <- Right(ActorFlow.actorRef { out => MyWebSocketActor.props(out) })
      } yield flow

      result match {
        case Left(exception) =>
          logger.warn(s"Exception during WebSocket connection. Exception Message: ${exception.getMessage}")
          Left(InternalServerError)

        case Right(flow) =>
          Right(flow)
      }
    }
  }
}
