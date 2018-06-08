package controllers

import java.util.UUID

import akka.actor.ActorSystem
import domain.user.PageView.PageView
import infrastructure.user.PageView.PageViewRepositoryMemoryImpl
import infrastructure.user.UserSession.UserSessionRepositoryMemoryImpl
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.mvc.AnyContent
import play.api.test.Helpers.{GET, status, stubBodyParser, stubControllerComponents, _}
import play.api.test.{FakeRequest, Injecting}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  *
  * Unit test suite for [[PageViewController]].
  */
class PageViewControllerTest extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  implicit val actorSystem: ActorSystem = ActorSystem("Test-Actor-System")

  val mockPageViewRepository: PageViewRepositoryMemoryImpl = mock[PageViewRepositoryMemoryImpl]

  when(mockPageViewRepository.saveUserPageView(any[PageView])).thenReturn(Future.successful(()))
  when(mockPageViewRepository.deleteUserPageViews(any[UUID])).thenReturn(Future.successful(()))

  val mockUserSessionRepository: UserSessionRepositoryMemoryImpl = mock[UserSessionRepositoryMemoryImpl]
  when(mockUserSessionRepository.deleteUserSessionEvents(any[UUID])).thenReturn(Future.successful(()))

  val pageView: PageView = PageView(
    user_id = UUID.randomUUID,
    name = "Test Page",
    timestamp = "2018-06-03T12:03:02.381Z"
  )

  "PageViewController" should {

    "return BAD REQUEST when  when posting page view on calling POST /v1/page without the page view in body" in {

      val controller = new PageViewController(
        stubControllerComponents(stubBodyParser(AnyContent(""))),
        mockPageViewRepository,
        mockUserSessionRepository
      )

      val result = controller.pageView.apply(FakeRequest(GET, "/v1/page"))

      status(result) mustBe BAD_REQUEST
    }

    "return OK when posting page view on calling POST /v1/page with valid page view in body" in {

      val controller = new PageViewController(
        stubControllerComponents(stubBodyParser(AnyContent(""))),
        mockPageViewRepository,
        mockUserSessionRepository
      )

      val result = controller.pageView.apply(FakeRequest(POST, "/v1/page").withJsonBody(Json.toJson(pageView)))

      status(result) mustBe OK
    }

    "return OK deleting the user's all page view on calling DELETE /v1/user/:userid" in {

      val controller = new PageViewController(
        stubControllerComponents(stubBodyParser(AnyContent(""))),
        mockPageViewRepository,
        mockUserSessionRepository
      )

      val result = controller.deletePageViews("123e4567-e89b-42d3-a456-556642440000")
        .apply(FakeRequest(DELETE, "/v1/user/123e4567-e89b-42d3-a456-556642440000"))

      status(result) mustBe OK
    }

  }

}
