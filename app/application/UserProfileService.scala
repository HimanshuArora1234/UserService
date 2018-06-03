package application

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

import domain.user.{PageView, UserProfile}

/**
  * Application service for [[domain.user.UserProfile]].
  */
class UserProfileService {

  /**
    * Generates [[UserProfile]] stats of last few days.
    *
    * @param userId        user id
    * @param userPageViews page viewed by user
    * @param statDays      stats to be generated for last `statDays` days
    * @return user's profile stats
    */
  def generateUserProfile(userId: UUID, userPageViews: Seq[PageView], statDays: Int = 7): UserProfile = {

    val currentTimestamp: Long = Instant.now.toEpochMilli
    val statDaysBeforeTimestamp: Long = Instant.now.minus(statDays, ChronoUnit.DAYS).toEpochMilli
    val pageViewsInLastSevenDays: Seq[PageView] = userPageViews.filter(pV =>
      pV.user_id == userId && pV.timestamp >= statDaysBeforeTimestamp && pV.timestamp <= currentTimestamp
    )

    val numberOfPagesVisitedInLastSevenDays: Int = pageViewsInLastSevenDays.length
    val maxVisitedPageInLastSevenDays: String = pageViewsInLastSevenDays.map(_.name).max

    val numberOfDaysActiveInLastSevenDays: Int = (1 to statDays).aggregate((0, Instant.now))(
      (x, y) => (
        if (isPageViewed(pageViewsInLastSevenDays, x._2, x._2.minus(1, ChronoUnit.DAYS))) x._1 + 1 else x._1,
        x._2.minus(1, ChronoUnit.DAYS)
      ),
      (x, y) => (x._1 + y._1, x._2)
    )._1


    UserProfile(
      userId = userId,
      number_of_days_active_last_7_days = numberOfDaysActiveInLastSevenDays,
      number_pages_viewed_the_last_7_days = numberOfPagesVisitedInLastSevenDays,
      most_viewed_page_last_7_days = maxVisitedPageInLastSevenDays,
      time_spent_on_site_last_7_days = 0.0
    )
  }

  /**
    * Returns true if user visited a page between a given time range.
    *
    * @param userPageViews User's page view activity list
    * @param start         check if user visited a page starting from this instance
    * @param end           check if user visited a page till this instance in past
    */
  private def isPageViewed(userPageViews: Seq[PageView], start: Instant, end: Instant): Boolean = {
    userPageViews.exists(pV => pV.timestamp > end.toEpochMilli && pV.timestamp <= start.toEpochMilli)
  }



}
