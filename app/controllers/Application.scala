package controllers

import akka.actor.Actor.Receive
import core.{StatusEmitterActor, StatusFetcher}
import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.Materializer
import model.{Overground, Tube}

import javax.inject._
import play.api._
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

@Singleton
class Application @Inject()(
  val controllerComponents: ControllerComponents
)(implicit system: ActorSystem, mat: Materializer) extends BaseController {

  def index(): Action[AnyContent] =
    Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index())
    }

  def socket = WebSocket.accept[JsValue, JsValue] { requestHeader =>
    ActorFlow.actorRef { actorRef =>
      StatusEmitterActor.props(actorRef)
    }
  }

  private val logger = Logger(this.getClass).logger

//  private val updateStatusTask = system.scheduler.scheduleAtFixedRate(0.second, 10.second) {
//    () => StatusFetcher.fetchStatus
//  }
}
