package infrastructure.actor

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, Props}

object ActorFactory {

  def getOrCreateSessionActor(uuid: UUID)(implicit actorSystem: ActorSystem): ActorRef = {
    actorSystem.actorOf(Props[SessionActor], s"${uuid.toString}-session-actor")
  }

}
