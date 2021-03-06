package controllers

import java.util.UUID
import javax.inject.Inject

import akka.actor.{ActorRef, ActorSystem}
import domain.user.PageView.{PageView, PageViewRepository}
import domain.user.UserSession.UserSessionRepository
import infrastructure.actor.ActorFactory
import infrastructure.user.UserSession.PageViewEvent
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Controller to handle page view related http requests.
  */
class PageViewController @Inject()(
                                    cc: ControllerComponents,
                                    pageViewRepository: PageViewRepository,
                                    userSessionRepository: UserSessionRepository
                                  )(implicit ec: ExecutionContext, actorSystem: ActorSystem) extends AbstractController(cc) {

  val logger = Logger.logger

  /**
    * Action to Post a page view [[PageView]].
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `POST` request with
    * a path of `/v1/page`.
    */
  def pageView = Action.async { implicit request: Request[AnyContent] =>

    val maybeJson: Option[JsValue] = request.body.asJson
    logger.info(
      s"Http request received on POST /v1/page route implemented by pageView action with body : ${maybeJson.map(Json.prettyPrint(_))}"
    )

    maybeJson.map(_.as[PageView]) match {
      case Some(pageView) =>

        // Track user login on the reception of Page view event
        val eventualSessionActor: Future[ActorRef] = ActorFactory.getOrCreateSessionActor(pageView.user_id, userSessionRepository)
        eventualSessionActor.map(_.tell(PageViewEvent, ActorRef.noSender))

        pageViewRepository.saveUserPageView(pageView).map(_ => Ok(""))
      case None => Future.successful(BadRequest("Page view JSON is invalid"))
    }
  }

  /**
    * Action to delete page view of a given user.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `DELETE` request with
    * a path of `v1/user/:userid`.
    */
  def deletePageViews(userid: String) = Action.async { implicit request: Request[AnyContent] =>

    logger.info(s"Http request received on DELETE /v1/user/:$userid route implemented by deletePageViews action")

    val userId: UUID = UUID.fromString(userid)

    (
      for {
        _ <- pageViewRepository.deleteUserPageViews(userId)
        - <- userSessionRepository.deleteUserSessionEvents(userId)
      } yield Ok("")
      ) recover {
      case ex: Throwable => logger.error(s"Failed to delete page views of user $userid because of ", ex)
        InternalServerError("Unable to delete page views")
    }
  }
}

