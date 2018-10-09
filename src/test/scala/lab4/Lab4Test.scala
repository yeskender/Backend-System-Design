package lab4

import org.scalatest.FlatSpec

class Lab4Test extends FlatSpec {

  "hasMiddleName" should "return true if Student has middle name" in {
    assert(Student("test", Some("mName"), "test", None).hasMiddleName)
  }

  it should "return false if Student does no have a middle name" in {
    assert(!Student("test", None, "test", None).hasMiddleName)
  }

  "hasDrivingLicense" should "return true if Student has a driving license" in {
    assert(Student("test", None, "test", Some("driving")).hasDrivingLicense)
  }

  it should "return false if Student does not have a driving license" in {
    assert(!Student("test", None, "test", None).hasDrivingLicense)
  }

  "addOptions" should "add to options with values" in {
    assert(Lab4.addOptions(Some(5), Some(10)).contains(15))
    assert(Lab4.addOptions(Some(18), Some(24)).contains(42))
  }

  it should "return None if one option has None" in {
    assert(Lab4.addOptions(Some(6), None).isEmpty)
    assert(Lab4.addOptions(None, Some(16)).isEmpty)
  }

  it should "return None if both options are empty" in {
    assert(Lab4.addOptions(None, None).isEmpty)
  }

  "extractDouble" should "return some value if valid string of double is used" in {
    assert(Lab4.extractDouble("1234.3456").contains(1234.3456D))
    assert(Lab4.extractDouble("352156").contains(352156D))
  }

  it should "return None if invalid string of double is used" in {
    assert(Lab4.extractDouble("23b45").isEmpty)
    assert(Lab4.extractDouble("testvalue 23p4o5").isEmpty)

  }

  "extractOperand" should "return some operand if valid string representation of operand is used" in {
    assert(Lab4.extractOperation("ADD").contains(Operation.ADD))
    assert(Lab4.extractOperation("SUBTRACT").contains(Operation.SUBTRACT))
    assert(Lab4.extractOperation("MULTIPLY").contains(Operation.MULTIPLY))
    assert(Lab4.extractOperation("DIVIDE").contains(Operation.DIVIDE))
  }

  it should "return None if invalid string representation of operand is used" in {
    assert(Lab4.extractOperation("ADDITION").isEmpty)
    assert(Lab4.extractOperation("SUBTRACTION").isEmpty)
    assert(Lab4.extractOperation("alskduf76asdo9f").isEmpty)
  }

  "simpleCalculate" should "return result of calculation if all data is valid" in {
    assert(Lab4.simpleCalculate("10.3", "ADD", "5.6").contains(15.9D))
    assert(
      Lab4
        .simpleCalculate("10.3", "SUBTRACT", "5.6")
        .map(BigDecimal(_).setScale(1, BigDecimal.RoundingMode.HALF_UP))
        .contains(4.7D)
    )
    val d = -7056.49303D
    assert(
      Lab4
        .simpleCalculate("532.689", "SUBTRACT", "7589.18203")
        .map(BigDecimal(_).setScale(5, BigDecimal.RoundingMode.HALF_UP)).contains(d)
    )
    assert(Lab4.simpleCalculate("10.3", "MULTIPLY", "5.6").contains(57.68D))
    assert(Lab4.simpleCalculate("4.0", "DIVIDE", "2.0").contains(2.0D))
  }

  it should "return None if invalid operand is passed" in {
    assert(Lab4.simpleCalculate("45", "ADDITION", "4").isEmpty)
    assert(Lab4.simpleCalculate("35", "asdpofuia7a", "78").isEmpty)
  }

  it should "return None if operand1 is invalid" in {
    assert(Lab4.simpleCalculate("7ezxv8", "ADD", "41.234").isEmpty)
    assert(Lab4.simpleCalculate("8f8a9s", "DIVIDE", "25349").isEmpty)
  }

  it should "return None if operand2 is invalid" in {
    assert(Lab4.simpleCalculate("2345.19234", "MULTIPLY", "41.x234").isEmpty)
    assert(Lab4.simpleCalculate("25674", "SUBTRACT", "253p49").isEmpty)
  }

  it should "return None if exception is raised during calculation" in {
    assert(Lab4.simpleCalculate("-89", "DIVIDE", "0").isEmpty)
    assert(Lab4.simpleCalculate("-89", "DIVIDE", "0.0").isEmpty)
    assert(Lab4.simpleCalculate("4567.9", "DIVIDE", "0.00").isEmpty)
    assert(Lab4.simpleCalculate("9138758.245038", "DIVIDE", "0.0000").isEmpty)
  }
}
