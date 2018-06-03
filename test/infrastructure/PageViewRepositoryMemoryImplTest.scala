package infrastructure

import java.time.Instant
import java.util.UUID

import domain.user.PageView
import infrastructure.user.PageViewRepositoryMemoryImpl
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec

/**
  * Unit test suite for [[infrastructure.user.PageViewRepositoryMemoryImpl]].
  */
class PageViewRepositoryMemoryImplTest extends PlaySpec
  with ScalaFutures {

  val pageViewRepositoryMemoryImpl: PageViewRepositoryMemoryImpl = new PageViewRepositoryMemoryImpl

  val userId: UUID = UUID.randomUUID
  val pageView = PageView(
    user_id = userId,
    name = "Test Page",
    timestamp = Instant.now.toString
  )

  "PageViewRepositoryMemoryImpl" should {

    "should save the user page view & return it when retrieving" in {
      pageViewRepositoryMemoryImpl.saveUserPageView(pageView).futureValue
      pageViewRepositoryMemoryImpl.getUserPageViews(userId).futureValue.contains(pageView) mustBe true
    }

    "should delete user's all page views" in {
      pageViewRepositoryMemoryImpl.deleteUserPageViews(userId).futureValue
      pageViewRepositoryMemoryImpl.getUserPageViews(userId).futureValue.contains(pageView) mustBe false
    }
  }

}
