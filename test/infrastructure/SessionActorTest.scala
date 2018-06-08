import akka.actor.{ActorSystem, ReceiveTimeout}
import akka.testkit.{ ImplicitSender, TestActors, TestKit, TestProbe}
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike}
import infrastructure.actor.SessionActor._
import infrastructure.actor.PageViewEvent
import infrastructure.user.UserSessionRepositoryMemoryImpl
import domain.user.{UserSession, UserSessionRepository}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.mockito.ArgumentMatchers.any
import akka.actor.ReceiveTimeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Unit test suite for [[infrastructure.actor.SessionActor]].
  */
class SessionActorTest extends TestKit(ActorSystem("Session-Actor-Test-System")) with WordSpecLike with Matchers
  with BeforeAndAfterAll with MockitoSugar {

  val mockUserSessionRepository: UserSessionRepositoryMemoryImpl = mock[UserSessionRepositoryMemoryImpl]
  when(mockUserSessionRepository.saveUserSessionEvent(any[UserSession])).thenReturn(Future.successful(()))

  val userId: UUID = UUID.randomUUID
  val sessionActor: ActorRef = TestActorRef[SessionActor](Props(userId, mockUserSessionRepository))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
  
   "SessionActor" must {
   
    "have user login timestamp set to None on creation" in {
      sessionActor.underlyingActor.loginInstant mustBe None
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
