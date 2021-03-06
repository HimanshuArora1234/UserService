package infrastructure.user.PageView

import java.util.UUID
import javax.inject.Singleton

import domain.user.PageView.{PageView, PageViewRepository}

import scala.concurrent.Future

/**
  * Memory implementation of [[PageViewRepository]].
  */
@Singleton
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
    Future.successful(this.pageViewStore.filter(_.user_id == userId))
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
    this.pageViewStore = pageViewStore.filterNot(_.user_id == userId)
    Future.successful(())
  }
}
