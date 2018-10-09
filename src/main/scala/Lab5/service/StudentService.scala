package Lab5.service

import akka.actor.{Actor, ActorLogging, Props}
import Lab5.model.Student
import Lab5.response.Response

object StudentService {
  sealed trait StudentRequest
  case class GetStudent(id: String)          extends StudentRequest
  case object GetAllStudents                 extends StudentRequest
  case class CreateStudent(student: Student) extends StudentRequest
  case class UpdateStudent(student: Student) extends StudentRequest
  case class RemoveStudent(id: String)       extends StudentRequest

  def props() = Props(new StudentService)
}

class StudentService extends Actor with ActorLogging {

  import StudentService._

  // A map where for (key -> value) key is student ID and value is Student structure itself.
  // Notice that students is mutable. However, Map is immutable data structure.
  // Ex: students = students + ("id-1" -> Student("id-1", fullname))
  var students = Map.empty[String, Student]

  override def receive: Receive = {

    /**
      * Get a list of all students
      */
    case GetAllStudents =>
      log.info("Received GetAllStudents request. Responding with all students.")
      sender() ! students.values.toList

    case GetStudent(id) =>
      val studentOption = students.get(id)

      studentOption match {

        case Some(student) =>
          log.info("Received GetStudent with id: {}. Returning student.", id)
          sender() ! Right(student)

        case None =>
          log.info("Received GetStudent with id: {}. However, such student could not be found.", id)
          sender() ! Left(Response.Error(s"Could not find student with id: $id"))
      }

    case CreateStudent(student) =>
      // check if student with such id exists in `students`
      if (students.contains(student.id)) {
        log.info("Received CreateStudent with id: {}. However, student with such id already exists.", student.id)
        sender() ! Left(Response.Error(s"Student with ${student.id} already exist"))
      } else {
        log.info("Received CreateStudent. Creating a student with id: {}", student.id)
        val newStudent = student.id -> Student(student.id, student.name, student.age)
        students = students + newStudent
        sender() ! Right(Response.Accepted(s"Successfully created $students"))


      }

    case UpdateStudent(updateStudent) =>

      // TODO: first check that if such student exists
      val studentOption = students.get(updateStudent.id)

      // TODO: if student exists then remove him from `students` and then add `updatedStudent` to students
      studentOption match {
        case Some(student) => {
          students = students + (updateStudent.id -> updateStudent)
          sender() ! Right(Response.Accepted(s"Successfully updated"))
        }
        case None => {
          sender() ! Left(Response.Error(s"Error, such student ${updateStudent} does not exist"))
        }
      }

    // TODO: if student does NOT exist in `students` reply with Error

    // TODO: do not forget logs


    case RemoveStudent(id) =>
      students.get(id) match {
        case Some(value) => {
          students = students - id
          sender() ! Right(Response.Accepted(s"Student with id = {$id} deleted"))
        }
        case None => {
          sender() ! Left(Response.Error(s"Student with id = {$id} not found"))
        }
      }
  }
}