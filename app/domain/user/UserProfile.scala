package domain.user

import java.util.UUID

import play.api.libs.json.Json

/**
  * Domain object to hold user profile.
  *
  * @param userId                              user's unique id
  * @param number_pages_viewed_the_last_7_days number of pages visited by user in last 7 days
  * @param time_spent_on_site_last_7_days      time spent (in hours) on site in last 7 days
  * @param number_of_days_active_last_7_days   number of days user was active in last 7 days
  * @param most_viewed_page_last_7_days        most viewed page name in last 7 days
  */
case class UserProfile(
                        userId: UUID,
                        number_pages_viewed_the_last_7_days: Int,
                        time_spent_on_site_last_7_days: Double,
                        number_of_days_active_last_7_days: Int,
                        most_viewed_page_last_7_days: String
                      )

/**
  * Companion object.
  */
object UserProfile {

  implicit val userProfileFmt = Json.format[UserProfile]

}