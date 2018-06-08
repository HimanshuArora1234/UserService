
package infrastructure

import java.time.Instant
import java.util.UUID

import domain.user.UserSession
import infrastructure.user.UserSessionRepositoryMemoryImpl
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec

/**
  * Unit test suite for [[infrastructure.user.UserSessionRepositoryMemoryImpl]].
  */
class UserSessionRepositoryMemoryImplTest extends PlaySpec
  with ScalaFutures {

  val userSessionRepositoryMemoryImpl: UserSessionRepositoryMemoryImpl = new UserSessionRepositoryMemoryImpl

  val userId: UUID = UUID.randomUUID
  val userSession = UserSession(
    user_id = userId,
    loginTimestamp = Instant.now.toString,
    logoutTimestamp = Instant.now.toString
  )

  "UserSessionRepositoryMemoryImpl" should {

    "save the user session event & return it when retrieving" in {
      userSessionRepositoryMemoryImpl.saveUserSessionEvent(userSession).futureValue
      userSessionRepositoryMemoryImpl.getUserSessionEvents(userId).futureValue.contains(userSession) mustBe true
    }

    "delete user's all session events" in {
      userSessionRepositoryMemoryImpl.deleteUserSessionEvents(userId).futureValue
      userSessionRepositoryMemoryImpl.getUserSessionEvents(userId).futureValue.contains(userSession) mustBe false
    }
  }

}
