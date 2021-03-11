package actors

import akka.actor.Actor
import validation.ConfigValidation.getApiKeyOrThrowable

class FetchStatusActor extends Actor {
  //      println(s" ACTOR SAYS KEY IS: ${getApiKeyOrThrowable(context.system.settings.config)} ")
  
  override def receive: Receive = ???
}
