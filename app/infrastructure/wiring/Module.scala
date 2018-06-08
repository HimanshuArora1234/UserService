package infrastructure.wiring

import com.google.inject.AbstractModule
import domain.user.PageView.PageViewRepository
import domain.user.UserSession.UserSessionRepository
import infrastructure.user.PageView.PageViewRepositoryMemoryImpl
import infrastructure.user.UserSession.UserSessionRepositoryMemoryImpl

/**
  * Guice's wiring module.
  */
class Module extends AbstractModule {

  def configure() = {

    bind(classOf[PageViewRepository]).to(classOf[PageViewRepositoryMemoryImpl])
    bind(classOf[UserSessionRepository]).to(classOf[UserSessionRepositoryMemoryImpl])

  }
}