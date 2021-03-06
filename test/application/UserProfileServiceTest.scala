package application

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

import domain.user.PageView.PageView
import domain.user.UserProfile.UserProfile
import domain.user.UserSession.UserSession
import org.scalatestplus.play.PlaySpec

/**
  * Unit test suite for [[UserProfileService]].
  */
class UserProfileServiceTest extends PlaySpec {

  val userProfileService: UserProfileService = new UserProfileService

  val userId: UUID = UUID.randomUUID
  val now: Instant = Instant.now
  val pageView1 = PageView(
    user_id = userId,
    name = "Test Page1",
    timestamp = now.toString
  )
  val pageView2 = PageView(
    user_id = userId,
    name = "Test Page1",
    timestamp = now.minus(1, ChronoUnit.DAYS).toString
  )
  val pageView3 = PageView(
    user_id = userId,
    name = "Test Page2",
    timestamp = now.minus(6, ChronoUnit.DAYS).toString
  )
  val pageView4 = PageView(
    user_id = userId,
    name = "Test Page1",
    timestamp = now.minus(8, ChronoUnit.DAYS).toString
  )
  val userSession1 = UserSession(
    user_id = userId,
    loginTimestamp = now.minus(1, ChronoUnit.DAYS).minus(2, ChronoUnit.MINUTES).toString,
    logoutTimestamp = now.minus(1, ChronoUnit.DAYS).toString
  )
  val userSession2 = UserSession(
    user_id = userId,
    loginTimestamp = now.minus(8, ChronoUnit.DAYS).minus(2, ChronoUnit.MINUTES).toString,
    logoutTimestamp = now.minus(8, ChronoUnit.DAYS).toString
  )
   
 

  "UserProfileService" should {

    "generate correct stats of user profile for last 7 days" in {

      val userProfile: UserProfile = userProfileService.generateUserProfileStats(
       userId,
       Seq(pageView1, pageView2, pageView3, pageView4),
       Seq(userSession1, userSession2)
      )

      userProfile.userId mustBe userId
      userProfile.most_viewed_page_last_7_days mustBe "Test Page1"
      userProfile.number_of_days_active_last_7_days mustBe 3
      userProfile.number_pages_viewed_the_last_7_days mustBe 3
      userProfile.time_spent_on_site_last_7_days mustBe 2.00
    }

  }

}
