package infrastructure.user

import java.util.UUID

import domain.user.{PageView, PageViewRepository}

import scala.concurrent.Future

/**
  * Memory implementation of [[PageViewRepository]].
  */
class PageViewRepositoryMemoryImpl extends PageViewRepository {

  /**
    * Stores the [[PageView]]s in memory.
    */
  private var pageViewStore: Seq[PageView] = Seq.empty[PageView]

  /**
    * Retrieves all pages visited by a user.
    *
    * @param userId user id
    * @return pages visited
    */
  override def getUserPageViews(userId: UUID): Future[Seq[PageView]] = {
    Future.successful(pageViewStore.filter(_.userId == userId))
  }

  /**
    * Stores user's page view activity.
    *
    * @param pageView Page view
    */
  override def saveUserPageView(pageView: PageView): Future[Unit] = {
    this.pageViewStore = pageViewStore :+ pageView
    Future.successful(())
  }

  /**
    * Deletes user's all page views.
    *
    * @param userId user id
    */
  override def deleteUserPageViews(userId: UUID): Future[Unit] = {
    this.pageViewStore = pageViewStore.filterNot(_.userId == userId)
    Future.successful(())
  }
}
