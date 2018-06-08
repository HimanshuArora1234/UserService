package infrastructure.user.UserSession

import java.time.Instant
import java.util.UUID

import akka.actor.{Actor, PoisonPill, ReceiveTimeout}
import com.typesafe.config.{Config, ConfigFactory}
import domain.user.UserSession.{UserSession, UserSessionRepository}
import infrastructure.user.UserSession.SessionActor._
import play.api.Logger

import scala.concurrent.duration._

/**
  * An actor to track and persist user's login and logout time.
  *
  * @param uuid                  User id whose login activity is tracked by this actor
  * @param userSessionRepository [[UserSessionRepository]]
  */
class SessionActor(uuid: UUID, userSessionRepository: UserSessionRepository) extends Actor {

  val logger = Logger.logger

  // actor state to keep the track of login timestamp
  var loginInstant: Option[Instant] = None

  // If no message received by actor before idle timeout then a `ReceiveTimeout` will be sent to this actor
  context.setReceiveTimeout(config.getInt("session.idle-time") minutes)

  def receive = {

    case ReceiveTimeout =>

      logger.info(s"Session actor for user $uuid has been idle for so long hence logging out and killing actor")

      // logout since user has been idle for so long
      loginInstant match {
        case Some(login) =>
          userSessionRepository.saveUserSessionEvent(
            UserSession(
              user_id = uuid,
              loginTimestamp = login.toString,
              logoutTimestamp = Instant.now.toString
            )
          )

        case None => // Do nothing
      }

      self ! PoisonPill // Killing actor

    case PageViewEvent => if (loginInstant.isEmpty) {
      logger.info(s"User $uuid just logged in")
      loginInstant = Some(Instant.now)
    }
  }

}

// Protocol of Session actor
case object PageViewEvent

/**
  * Companion object.
  */
object SessionActor {
  val config: Config = ConfigFactory.load
}


