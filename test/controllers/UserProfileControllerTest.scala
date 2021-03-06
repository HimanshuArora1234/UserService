package controllers

import java.time.Instant
import java.util.UUID

import application.UserProfileService
import domain.user.PageView.PageView
import domain.user.UserProfile.UserProfile
import domain.user.UserSession.UserSession
import infrastructure.user.PageView.PageViewRepositoryMemoryImpl
import infrastructure.user.UserSession.UserSessionRepositoryMemoryImpl
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.mvc.AnyContent
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  *
  * Unit test suite for [[UserProfileController]].
  */
class UserProfileControllerTest extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  val mockUserProfileService: UserProfileService = mock[UserProfileService]
  val mockPageViewRepository: PageViewRepositoryMemoryImpl = mock[PageViewRepositoryMemoryImpl]
  val mockUserSessionRepository: UserSessionRepositoryMemoryImpl = mock[UserSessionRepositoryMemoryImpl]

  val userId = UUID.randomUUID

  val userProfile: UserProfile = UserProfile(
    userId = userId,
    number_of_days_active_last_7_days = 2,
    number_pages_viewed_the_last_7_days = 4,
    most_viewed_page_last_7_days = "Test Page",
    time_spent_on_site_last_7_days = 5.0
  )

  when(mockUserProfileService.generateUserProfileStats(any[UUID], any[Seq[PageView]], any[Seq[UserSession]])).thenReturn(userProfile)
  when(mockPageViewRepository.getUserPageViews(any[UUID])).thenReturn(Future.successful(Seq.empty[PageView]))
  when(mockUserSessionRepository.getUserSessionEvents(any[UUID])).thenReturn(
    Future.successful(
      Seq(
        UserSession(
          user_id = userId,
          loginTimestamp = Instant.now.toString,
          logoutTimestamp = Instant.now.toString
        )
      )
    )
  )

  "UserProfileController" should {

    "return OK and correct user profile stats on calling GET /v1/user/:userid" in {

      val controller = new UserProfileController(
        stubControllerComponents(stubBodyParser(AnyContent(""))),
        mockPageViewRepository,
        mockUserProfileService,
        mockUserSessionRepository
      )

      val result = controller.getUserProfile("123e4567-e89b-42d3-a456-556642440000")
        .apply(FakeRequest(GET, "/v1/user/123e4567-e89b-42d3-a456-556642440000"))

      status(result) mustBe OK
      contentAsJson(result) mustBe Json.toJson(userProfile)
    }

  }
}
