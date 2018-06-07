package infrastructure.wiring

import com.google.inject.AbstractModule
import domain.user.{PageViewRepository, UserSessionRepository}
import infrastructure.user.{PageViewRepositoryMemoryImpl, UserSessionRepositoryMemoryImpl}

/**
  * Guice's wiring module.
  */
class Module extends AbstractModule {

  def configure() = {

    bind(classOf[PageViewRepository]).to(classOf[PageViewRepositoryMemoryImpl])
    bind(classOf[UserSessionRepository]).to(classOf[UserSessionRepositoryMemoryImpl])

  }
}