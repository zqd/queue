package actors

import akka.actor.{Actor, ActorRef, Props}
import play.api.libs.json.{JsValue, Json}

/**
  * Created by 19016 on 2017/1/13.
  */
class RouteActor(out: ActorRef) extends Actor {
  override def receive: Receive = {

    case msg: JsValue =>
      println(msg.toString())
      val action = msg.as[UserAction]
      wcActor ! action

    case rs: List[QueueData] =>
      out ! Json.toJson(rs)

    case rs:Result =>
      out ! Json.toJson(rs)
  }
}

object RouteActor {
  def props(out: ActorRef) = Props(new RouteActor(out))
}
