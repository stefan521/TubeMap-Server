package controllers

import core.{StatusEmitterActor, StatusFetcher}
import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.Materializer
import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase

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

  def socket = WebSocket.accept[JsValue, JsValue] { requestHeader =>
    ActorFlow.actorRef { actorRef =>
      StatusEmitterActor.props(actorRef)
    }
  }

  private val logger = play.api.Logger(getClass)

  private val updateStatusTask = system.scheduler.scheduleAtFixedRate(0.second, 10.second) {
    () => StatusFetcher.fetchStatus(system)
  }
}

object Application {
  lazy val mongoClient: MongoClient = new MongoClient("localhost:2717")
  lazy val mongoDb: MongoDatabase = mongoClient.getDatabase("test")
}
