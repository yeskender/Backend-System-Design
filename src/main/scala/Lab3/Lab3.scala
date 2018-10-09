case class FullName(firstName: String, secondName: String)

sealed trait Human
case class Student(name: String) extends Human

case class Teacher(id: String) extends Human

case class Character(fullName: FullName, age: Int) extends Human

object Lab3 extends App {

  /**
    * Part 1
    */

  val aigul = Student("Timur")
  val ainur = Student("Ainur")

  val students: List[Student] =
    List(aigul, ainur, Student("Azamat"), Student("Arman"), Student("Aru"))

  //  1) map students to a collection of teachers. Every student name is teacher's id

  val teachers: List[Teacher] = students.map { s => Teacher(s.name) }
  println(teachers)

  //  2) using foreach print all students

  students.foreach(s => println(s))

  //  3) filter out students with name Azamat and Arman, and using foreach print out only names of students

  students.filter(s => (s.name != "Azamat" && s.name != "Arman")).foreach(s => println(s.name))

  /**
    * Part 2
    *
    * Disclaimer: all characters are fictional, any resemblance
    * with Game of Thrones characters is accidental and unintentional.
    *
    * Print out = in all exercises below, print using `foreach` and passing `println` method
    *
    */
  val daenerys = Character(FullName("Daenerys", "Targaryen"), 28)
  // he knows nothing :(
  val jon     = Character(FullName("Jon", "Snow"), 30)
  val tyrion  = Character(FullName("Tyrion", "Lannister"), 40)
  val petyr   = Character(FullName("Petyr", "Baelish"), 38)
  val drogo   = Character(FullName("Khal", "Drogo"), 24)
  val eddard  = Character(FullName("Eddard", "Stark"), 49)
  val arya    = Character(FullName("Arya", "Stark"), 15)
  val cercei  = Character(FullName("Cersei", "Lannister"), 42)
  val joffrey = Character(FullName("Sansa", "Stark"), 20)
  val sandor  = Character(FullName("Sandor", "Clegane"), 40)

  val characters = List(daenerys, jon, tyrion, petyr, drogo, eddard, cercei, joffrey, sandor)
  // 4) print out characters whose `secondName` is "Lannister"
  characters.filter(c => (c.fullName.secondName == "Lannister")).foreach(c => println(c))

  // 5) print out only `firstName`s of characters whose `secondName` is Stark
  characters.filter(c => (c.fullName.secondName == "Stark")).foreach(c => println(c.fullName.firstName))

  // 6) map allCharacters to `Student`s using map, where each character `firstName` is student's name

  val characterStudents: List[Student] = characters.map(c => Student(c.fullName.firstName))
  characterStudents.foreach(c => println(c))

  // 7) filter out characters who are older than 20 ages old, get only their `firstNames`
  // result should be List("Daenerys", "Jon", "Tyrion", "Petyr", etc.

  val firstNamesOfCharactersOlderThanTwenty: List[String] = characters.filter(c => c.age > 20).map(c => c.fullName.firstName)
  firstNamesOfCharactersOlderThanTwenty.foreach(c => println(c))
  // 8) complete the getInfo method, it should return a string
  // for Student   -- student's name
  // for Teacher   -- teacher's id
  // for Character -- character's in format: firstName secondName, age: character's age
  //                  ex: Eddard Stark, aged: 49
  //                  ex: Petyr  Baelish, aged: 38

  def getInfo(human: Human): String = human match {//??? match pattern
    case Student(name) => name
    case Teacher(id) => id
    case Character(fullName, age) => {
      val name = fullName.firstName
      val surname = fullName.secondName
      s"${fullName.firstName} $surname, age: $age"
    }
  }

  val newFullName: FullName = FullName("Ed", "Sheeran")
  println(getInfo(Character(newFullName, 29)))

  // 9) Flatten the following List of Lists. Notice that it should be List of Humans

  val allLists: List[List[Human]] = List(students, teachers, characters)

  val flattenList = allLists.flatten.foreach(f => println(f))

}
