package core

import core.StatusEmitterActor.SendUpdate
import akka.actor._
import play.api.Logger
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt


object StatusEmitterActor {
  def props(out: ActorRef) = Props(new StatusEmitterActor(out))

  case object SendUpdate
}

class StatusEmitterActor(clientActorRef: ActorRef) extends Actor {

  import model.JsonWrites._

  protected val logger: Logger = play.api.Logger(getClass)

  protected def replyWithStatusUpdates(): Unit = {
    clientActorRef ! Json.toJson("")
//    StatusFetcher.fetchStatus(context.system) map {
//      case Right(value) =>
//        clientActorRef ! Json.toJson("")
//
//      case Left(err) =>
//        logger.error(err.toString)
//        clientActorRef ! Json.parse(s"""{"body": "An error :D"}""")
//    }
  }

  protected def replyWithFullStatus(): Unit = {
    replyWithStatusUpdates()
  }

  protected def scheduleNextUpdate(): Cancellable =
    context.system.scheduler.scheduleOnce(5.seconds) {
      self ! StatusEmitterActor.SendUpdate
    }

  override def receive: Receive = {
    case message: JsValue =>
      replyWithFullStatus()
      scheduleNextUpdate()

    case SendUpdate =>
      replyWithStatusUpdates()
      scheduleNextUpdate()
  }
}
