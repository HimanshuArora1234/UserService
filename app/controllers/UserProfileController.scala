package controllers

import java.util.UUID
import javax.inject._

import application.UserProfileService
import domain.user.PageViewRepository
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
  * Controller to handle user profile stats related http requests.
  */
@Singleton
class UserProfileController @Inject()(
                                       cc: ControllerComponents,
                                       pageViewRepository: PageViewRepository,
                                       userProfileService: UserProfileService
                                     )(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  val logger = Logger.logger

  /**
    * Action to return user profile stats.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/v1/user/:userid`.
    */
  def getUserProfile(userid: String) = Action.async { implicit request: Request[AnyContent] =>

    logger.info(s"Http request received on GET /v1/user/:$userid route implemented by getUserProfile action")
    val userUUID = UUID.fromString(userid)

    pageViewRepository.getUserPageViews(userUUID)
      .map(userProfileService.generateUserProfileStats(userUUID, _))
      .map(profile => Ok(Json.toJson(profile)))
      .recover {
        case ex: Throwable => logger.error(s"Failed to generate user profile stats for user $userid because of ", ex)
          InternalServerError("Unable to retrieve user profile stats")
      }

  }
}
