package task.airport

package object model {

  case class Degree(value: Double)

  case class Feet(value: Int)

  case class URL(value: String) {
    require(value.nonEmpty)
  }

}
