package infrastructure.user.UserSession

import java.util.UUID
import javax.inject.Singleton

import domain.user.UserSession.{UserSession, UserSessionRepository}

import scala.concurrent.Future


/**
  * Memory implementation of [[UserSessionRepository]].
  */
@Singleton
class UserSessionRepositoryMemoryImpl extends UserSessionRepository {

  /**
    * Stores the [[UserSession]]s in memory.
    */
  private var userSessionStore: Seq[UserSession] = Seq.empty[UserSession]

  /**
    * Retrieves all session events of user.
    *
    * @param userId user id
    * @return user session events
    */
  override def getUserSessionEvents(userId: UUID): Future[Seq[UserSession]] = {
    Future.successful(this.userSessionStore.filter(_.user_id == userId))
  }

  /**
    * Stores user's session event.
    *
    * @param userSession user session to save
    */
  override def saveUserSessionEvent(userSession: UserSession): Future[Unit] = {
    this.userSessionStore = userSessionStore :+ userSession
    Future.successful(())
  }

  /**
    * Deletes user's all session events.
    *
    * @param userId user id
    */
  override def deleteUserSessionEvents(userId: UUID): Future[Unit] = {
    this.userSessionStore = userSessionStore.filterNot(_.user_id == userId)
    Future.successful(())
  }
}
