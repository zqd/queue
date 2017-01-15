package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import actors._

import scala.concurrent.ExecutionContext

class Application @Inject() (implicit actorSystem: ActorSystem,
                  mat: Materializer,
                  ec: ExecutionContext) extends Controller {


  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def socket = WebSocket.accept[JsValue,JsValue]{
    request =>
      println("socket")
      ActorFlow.actorRef(out => RouteActor.props(out))
  }

}