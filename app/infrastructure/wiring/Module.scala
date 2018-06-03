package infrastructure.wiring

import com.google.inject.AbstractModule
import domain.user.PageViewRepository
import infrastructure.user.PageViewRepositoryMemoryImpl

/**
  * Guice's wiring module.
  */
class Module extends AbstractModule {

  def configure() = {

    bind(classOf[PageViewRepository]).to(classOf[PageViewRepositoryMemoryImpl])

  }
}