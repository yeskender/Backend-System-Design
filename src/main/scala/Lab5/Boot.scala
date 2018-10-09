package Lab5

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import Lab5.model.Student
import Lab5.response.Response
import Lab5.service.StudentService
import akka.pattern.ask
import akka.util.Timeout
import Lab5.serialization.JsonSupport

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.io.StdIn

object Boot extends App with JsonSupport {
  implicit val system: ActorSystem             = ActorSystem("lab5-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // needed fot akka's ASK pattern
  implicit val timeout: Timeout = Timeout(60.seconds)

  val studentService = system.actorOf(StudentService.props(), "student-service")

  val route: Route = {
    concat(
      path("students") {
        pathEndOrSingleSlash {
          get {
            complete {
              (studentService ? StudentService.GetAllStudents).mapTo[List[Student]]
            }
          }
        }
      },
      pathPrefix("student" / Segment) { studentId =>
        pathEndOrSingleSlash {
          concat(
            get {
              complete {
                (studentService ? StudentService.GetStudent(studentId)).mapTo[Either[Response.Error, Student]]
              }
            },
            post {
              decodeRequest {
                entity(as[Student]) { student =>
                  complete {
                    // may need a check of student.id and studentId
                    (studentService ? StudentService.CreateStudent(student)).mapTo[Either[Response.Error, Response.Accepted]]
                  }
                }
              }
            },
            put {
              decodeRequest {
                entity(as[Student]) { student =>
                  complete {
                    // may need a check of student.id and studentId
                    (studentService ? StudentService.UpdateStudent(student))
                      .mapTo[Either[Response.Error, Response.Accepted]]
                  }
                }
              }
            },
            delete {
              complete {
                (studentService ? StudentService.RemoveStudent(studentId))
                  .mapTo[Either[Response.Error, Response.Accepted]]
              }
            }
          )
        }
      }
    )
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8081)
  println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done
}
