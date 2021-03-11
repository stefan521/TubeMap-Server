package actors

import actors.StatusEmitterActor.SendUpdate
import akka.actor._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object StatusEmitterActor {
  def props(out: ActorRef) = Props(new StatusEmitterActor(out))

  case object SendUpdate
}

class StatusEmitterActor(requester: ActorRef) extends Actor {

  def receive: Receive = {

    case msg: String =>
      replyAndScheduleNextUpdate(s"First Message in reply to:   ${msg}")

    case SendUpdate =>
      replyAndScheduleNextUpdate(s"Subsequent Message.")
  }

  protected def replyAndScheduleNextUpdate(message: String): Unit = {
    requester ! message

    context.system.scheduler.scheduleOnce(5.seconds) {
      self ! StatusEmitterActor.SendUpdate
    }
  }

  override def postStop(): Unit = {
    // this is where we do cleanup when the connection is closed (can send PoisonPill to the actor to close it)
  }
}