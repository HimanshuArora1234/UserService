package domain.user.UserSession

import java.util.UUID

import scala.concurrent.Future

/**
  * Data access facade for [[UserSession]] domain object.
  */
trait UserSessionRepository {

  /**
    * Retrieves all session events of user.
    *
    * @param userId user id
    * @return user session events
    */
  def getUserSessionEvents(userId: UUID): Future[Seq[UserSession]]

  /**
    * Stores user's session event.
    *
    * @param userSession user session to save
    */
  def saveUserSessionEvent(userSession: UserSession): Future[Unit]

  /**
    * Deletes user's all session events.
    *
    * @param userId user id
    */
  def deleteUserSessionEvents(userId: UUID): Future[Unit]

}
