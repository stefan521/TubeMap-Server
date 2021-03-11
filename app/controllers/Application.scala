package controllers

import core.{StatusFetcher, StatusEmitterActor}
import akka.actor.ActorSystem
import akka.stream.Materializer

import javax.inject._
import play.api._
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt


@Singleton
class Application @Inject()(
  val controllerComponents: ControllerComponents
)(implicit system: ActorSystem, mat: Materializer) extends BaseController {

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
       Right(ActorFlow.actorRef { out => StatusEmitterActor.props(out) })
    }
  }

  private val logger = Logger(this.getClass).logger

  private val updateStatusTask = system.scheduler.scheduleAtFixedRate(0.second, 5.second) {
    () => StatusFetcher.fetchStatus(system)
  }
}
