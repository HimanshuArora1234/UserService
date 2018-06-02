package domain.user

import java.util.UUID

import play.api.libs.json.Json

/**
  * Domain object to hold user's page view activity.
  *
  * @param userId      User's unique id,
  * @param pageVisited Page name visited by user
  * @param timestamp
  */
case class PageView(userId: UUID, pageVisited: String, timestamp: Long)

/**
  * Companion object.
  */
object PageView {

  implicit val pageViewFmt = Json.format[PageView]

}