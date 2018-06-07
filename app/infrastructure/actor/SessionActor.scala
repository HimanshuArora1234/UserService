package infrastructure.actor

import akka.actor.{Actor, ReceiveTimeout}

import scala.concurrent.duration._

class SessionActor extends Actor {

  context.setReceiveTimeout(30 milliseconds)

  def receive = {
    case ReceiveTimeout ⇒
      println("Receive timed out")
  }
}


