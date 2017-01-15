package actors

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


class WCActor extends Actor {

  private val buffer = ListBuffer.empty[String]
  private val userActors = mutable.Map.empty[String, Set[ActorRef]]

  override def receive: Receive = {
    case action: UserAction =>

      val user = if (action.action != "login") {
        getUser(sender())
      } else {
        action.user.get
      }
      if (user != "") {
        //业务处理
        val rs = proc(action.action, user)
        userActors.get(user) match {
          case None =>
          case Some(set) => set.foreach(sender => sender ! Result(rs))
        }
        //通知当前user
        if (buffer.nonEmpty) {
          userActors(buffer.head).foreach(sender => sender ! Result("onOrder"))
        }
        //处理结果
        val data = buffer.map(name => QueueData(name, buffer.indexOf(name).toString)).toList
        userActors.values.flatten.foreach(sender => sender ! data)
      }

    case _ =>

  }

  private def proc(action: String, user: String): String = {

    action match {
      case "will" =>
        if (isLogin(user) && !buffer.contains(user)) {
          buffer.append(user)
          "willSuccess"
        } else {
          "willFail"
        }

      case "cancel" =>
        val index = buffer.indexOf(user)
        if (index != -1) {
          buffer.remove(index)
          "cancelSuccess"
        } else {
          "cancelFail"
        }
      case "done" =>
        if (buffer.nonEmpty && buffer.head == user) {
          buffer.remove(0)
          "doneSuccess"
        } else {
          "doneFail"
        }
      case "login" =>
        userActors.put(user, userActors.getOrElse(user, Set.empty[ActorRef]) + sender())
        "loginSuccess"
      case "logout" =>
        userActors.remove(user)
        //TODO 由于登陆和业务没有分开，此处暂时这样写
        val index = buffer.indexOf(user)
        if (index != -1) {
          buffer.remove(index)
        }
        "logoutSuccess"
      case _ => throw new RuntimeException("未知类型")
    }
  }

  private def isLogin(userName: String): Boolean = {
    userActors.keySet.contains(userName)
  }

  private def getUser(actorRef: ActorRef): String = {
    userActors.find(v => v._2.contains(actorRef)) match {
      case None => ""
      case Some(x) => x._1
    }
  }
}
