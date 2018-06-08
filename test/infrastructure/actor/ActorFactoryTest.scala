package infrastructure.actor

import java.util.UUID

import akka.actor.ActorSystem
import infrastructure.user.UserSession.UserSessionRepositoryMemoryImpl
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Unit tests suite for [[ActorFactory]].
  */
class ActorFactoryTest extends PlaySpec with ScalaFutures with MockitoSugar {

  implicit val actorSystem: ActorSystem = ActorSystem("ActorFactory-Test-System")

  val mockUserSessionRepository: UserSessionRepositoryMemoryImpl = mock[UserSessionRepositoryMemoryImpl]

  "ActorFactory" should {

    "not create a new session actor if one already present" in {

      val userId: UUID = UUID.randomUUID()
      val actorRef = ActorFactory.getOrCreateSessionActor(userId, mockUserSessionRepository).futureValue

      ActorFactory.getOrCreateSessionActor(userId, mockUserSessionRepository).futureValue mustBe actorRef

    }
  }

}
