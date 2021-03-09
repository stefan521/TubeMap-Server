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

  private val unifiedApiKey = config.get[String]("unified.api_key")
  private val logger = Logger(this.getClass).logger

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] =
    Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index())
    }

  def status(): Action[AnyContent] =
    Action { implicit request: Request[AnyContent] => {
      config.entrySet.foreach(t => println(t))

      println {
        config.getOptional[String]("akka.http.server.idle-timeout")
      }

      println {
        config.getOptional[String]("akka.http.client.idle-timeout")
      }

      println {
        config.getOptional[String]("akka.http.server.websocket.periodic-keep-alive-max-idle")
      }

      import ConfigLoader.configLoader

      println {
        config.getOptional("akka.http")
      }

      Ok("haha it works pretty well.")
    }}

  def socket = WebSocket.acceptOrResult[String, String] { request =>
    Future.successful {
      Right(ActorFlow.actorRef { out =>
        MyWebSocketActor.props(out)
      })
    }
  }

//  def socket = WebSocket.accept[String, String] { request =>
//    ActorFlow.actorRef { out =>
//      MyWebSocketActor.props(out)
//    }
//  }
}
