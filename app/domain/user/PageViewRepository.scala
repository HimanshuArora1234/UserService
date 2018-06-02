package domain.user

import java.util.UUID

import scala.concurrent.Future

/**
  * Data access facade for [[PageView]] domain object.
  */
trait PageViewRepository {

  /**
    * Retrieves all pages visited by a user.
    *
    * @param userId         user id
    * @param startTimestamp page views from this timestamp (if given otherwise current timestamp)
    * @param endTimestamp   page views till this timestamp in past (if given otherwise no limit)
    * @return pages visited
    */
  def getUserPageViews(userId: UUID, startTimestamp: Option[Long], endTimestamp: Option[Long]): Future[Seq[PageView]]

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
