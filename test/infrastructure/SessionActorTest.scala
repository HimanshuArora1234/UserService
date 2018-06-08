import java.util.UUID

import akka.actor.{ActorSystem, Props, ReceiveTimeout}
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import domain.user.UserSession
import infrastructure.actor.{PageViewEvent, SessionActor}
import infrastructure.user.UserSessionRepositoryMemoryImpl
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.Future

/**
  * Unit test suite for [[infrastructure.actor.SessionActor]].
  */
class SessionActorTest extends TestKit(ActorSystem("Session-Actor-Test-System")) with WordSpecLike
  with BeforeAndAfterAll with MockitoSugar {

  val mockUserSessionRepository: UserSessionRepositoryMemoryImpl = mock[UserSessionRepositoryMemoryImpl]
  when(mockUserSessionRepository.saveUserSessionEvent(any[UserSession])).thenReturn(Future.successful(()))

  val userId: UUID = UUID.randomUUID
  val sessionActor = TestActorRef[SessionActor](Props(classOf[SessionActor], userId, mockUserSessionRepository))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "SessionActor" must {

    "have user login timestamp set to None on creation" in {
      assert(sessionActor.underlyingActor.loginInstant === None)
    }

    "initialize user login timestamp on reception of first page view event" in {
      sessionActor ! PageViewEvent
      assert(sessionActor.underlyingActor.loginInstant !== None)
    }

    "kill itself when received timeout message" in {
      val probe = TestProbe()
      probe watch sessionActor
      sessionActor ! ReceiveTimeout
      probe.expectTerminated(sessionActor)
    }

  }

}
