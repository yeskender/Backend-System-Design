package Lab5.serialization

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import Lab5.model.{Fullname, Student}
import Lab5.response.Response
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * Adds JSON support for models and reponses. Akka uses these formats when serializing/deserializing JSON.
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  // models
  implicit val fullnameFormat: RootJsonFormat[Fullname] = jsonFormat3(Fullname)
  implicit val studentFormat: RootJsonFormat[Student]   = jsonFormat3(Student)

  // responses
  implicit val acceptedFormat: RootJsonFormat[Response.Accepted] = jsonFormat2(Response.Accepted)
  implicit val errorFormat: RootJsonFormat[Response.Error]       = jsonFormat2(Response.Error)
}
