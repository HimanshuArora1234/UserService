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
    * Generates [[UserProfile]] stats of last seven days.
    *
    * @param userId        user id
    * @param userPageViews page viewed by user
    * @return user's profile stats
    */
  def generateUserProfileStats(userId: UUID, userPageViews: Seq[PageView]): UserProfile = {

    val currentTimestamp: Long = Instant.now.toEpochMilli
    val sevenDaysBeforeTimestamp: Long = Instant.now.minus(7, ChronoUnit.DAYS).toEpochMilli
    val pageViewsInLastSevenDays: Seq[PageView] = userPageViews.filter(pV =>
      pV.user_id == userId && pV.timestamp >= sevenDaysBeforeTimestamp && pV.timestamp <= currentTimestamp
    )

    val numberOfPagesVisitedInLastSevenDays: Int = pageViewsInLastSevenDays.length

    val maxVisitedPageInLastSevenDays: String = if (pageViewsInLastSevenDays.isEmpty) "" else {
      pageViewsInLastSevenDays
        .map(_.name)
        .groupBy(identity)
        .mapValues(_.size)
        .maxBy(_._2)
        ._1
    }

    val numberOfDaysActiveInLastSevenDays: Int = (1 to 7).aggregate((0, Instant.now))(
      (countInstantTuple, _) => (
        if (isPageViewed(pageViewsInLastSevenDays, countInstantTuple._2, countInstantTuple._2.minus(1, ChronoUnit.DAYS)))
          countInstantTuple._1 + 1
        else countInstantTuple._1,
        countInstantTuple._2.minus(1, ChronoUnit.DAYS)
      ),
      (countInstantTuple1, countInstantTuple2) => (countInstantTuple1._1 + countInstantTuple2._1, countInstantTuple1._2)
    )._1


    UserProfile(
      userId = userId,
      number_of_days_active_last_7_days = numberOfDaysActiveInLastSevenDays,
      number_pages_viewed_the_last_7_days = numberOfPagesVisitedInLastSevenDays,
      most_viewed_page_last_7_days = maxVisitedPageInLastSevenDays,
      time_spent_on_site_last_7_days = 0.0 // TODO: yet to be implemented
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
