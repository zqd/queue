package actors

import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, Writes, _}


case class UserAction(action:String, user:Option[String])

object UserAction {
  implicit val actionReads: Reads[UserAction] = (
    (__ \ "action").read[String] ~ (__ \ "user").readNullable[String]
    ) (UserAction.apply _)

//  implicit val actionWrites: Writes[UserAction] = (
  //    (__ \ "action").write[String] ~ (__ \ "user").writeNullable[String]
  //    ) (unlift(UserAction.unapply))
}


case class QueueData(user:String, state:String)

object QueueData {
//  implicit val resultReads: Reads[QueueData] = (
//    (__ \ "user").read[String] ~ (__ \ "state").read[String]
//    ) (QueueData.apply _)

  implicit val resultWrites: Writes[QueueData] = (
    (__ \ "user").write[String] ~ (__ \ "state").write[String]
    ) (unlift(QueueData.unapply))
}

case class Result(rsType:String)

object Result {

    implicit val resultWrites: Writes[Result] = Writes(rs => Json.toJson(JsObject(Map("rsType" -> JsString(rs.rsType)).toSeq)))
}

