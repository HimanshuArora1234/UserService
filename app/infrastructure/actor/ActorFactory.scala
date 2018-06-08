package infrastructure.actor

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, Props}
import domain.user.UserSession.UserSessionRepository
import infrastructure.user.UserSession.SessionActor
import play.api.Logger

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

/**
  * Factory object for actor creation.
  */
object ActorFactory {

  val logger = Logger.logger

  /**
    * This function either creates a [[SessionActor]] for a user if one doesn't already exist or returns that one.
    *
    * @param uuid                  user id
    * @param userSessionRepository [[UserSessionRepository]]
    * @param actorSystem           actor system
    * @return Session actorRef
    */
  def getOrCreateSessionActor(uuid: UUID, userSessionRepository: UserSessionRepository)
                             (implicit actorSystem: ActorSystem, ec: ExecutionContext): Future[ActorRef] = {

    val actorName = generateSessionActorName(uuid)
    val eventualSessionActor: Future[ActorRef] = actorSystem.actorSelection(s"/user/$actorName").resolveOne(50 milliseconds)

    eventualSessionActor recover {
      case _ =>
        logger.info(s"No actor named $actorName already exists so creating a new one")
        actorSystem.actorOf(Props(classOf[SessionActor], uuid, userSessionRepository), actorName)
    }
  }

  /**
    * Generates a unique name for actor.
    *
    * @param uuid user id
    * @return actor name
    */
  private def generateSessionActorName(uuid: UUID): String = s"${uuid.toString}-session-actor"

}
