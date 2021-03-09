package actors

import actors.MyWebSocketActor.SendUpdate
import akka.actor._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))

  case object SendUpdate
}

class MyWebSocketActor(requester: ActorRef) extends Actor {

  def receive: Receive = {

    case msg: String =>
      replyAndScheduleNextUpdate(s"First Message in reply to:      ${msg}")

    case SendUpdate =>
      replyAndScheduleNextUpdate(s"Subsequent Message.")
  }

  protected def replyAndScheduleNextUpdate(message: String): Unit = {
    requester ! message

    context.system.scheduler.scheduleOnce(10.seconds) {
      self ! MyWebSocketActor.SendUpdate
    }
  }

  override def postStop(): Unit = {
    println("\n\n     @@@ Actor shut down @@@  \n\n")
    // this is where we do cleanup
  }
}