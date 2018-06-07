package domain.user

import java.util.UUID

import play.api.libs.json.Json

/**
  * Domain object to hold user's login logout information.
  *
  * @param user_id         user id
  * @param loginTimestamp  timestamp when user logged in
  * @param logoutTimestamp timestamp when user logged out
  */
case class UserSession(user_id: UUID, loginTimestamp: String, logoutTimestamp: String)

/**
  * Companion object.
  */
object UserSession {

  implicit val userSessionFmt = Json.format[UserSession]

}