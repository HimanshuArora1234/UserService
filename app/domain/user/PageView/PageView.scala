package domain.user.PageView

import java.util.UUID

import play.api.libs.json.Json

/**
  * Domain object to hold user's page view activity.
  *
  * @param user_id      User's unique id,
  * @param name Page name visited by user
  * @param timestamp
  */
case class PageView(user_id: UUID, name: String, timestamp: String)

/**
  * Companion object.
  */
object PageView {

  implicit val pageViewFmt = Json.format[PageView]

}