package domain.user.PageView

import java.util.UUID

import scala.concurrent.Future

/**
  * Data access facade for [[PageView]] domain object.
  */
trait PageViewRepository {

  /**
    * Retrieves all pages visited by a user.
    *
    * @param userId user id
    * @return pages visited
    */
  def getUserPageViews(userId: UUID): Future[Seq[PageView]]

  /**
    * Stores user's page view activity.
    *
    * @param pageView Page view
    */
  def saveUserPageView(pageView: PageView): Future[Unit]

  /**
    * Deletes user's all page views.
    *
    * @param userId user id
    */
  def deleteUserPageViews(userId: UUID): Future[Unit]

}
