import akka.actor.{ActorRef, ActorSystem, Props}

/**
  * Created by 19016 on 2017/1/13.
  */
package object actors {
  val actorSystem: ActorSystem = ActorSystem.create("queue")
  val wcActor: ActorRef = actorSystem.actorOf(Props[WCActor])
}
