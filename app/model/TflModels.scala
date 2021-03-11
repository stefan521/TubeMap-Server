package model

import org.joda.time.DateTime
import play.api.libs.json.{JodaReads, JodaWrites, Json}


/* Mode */
sealed trait TflMode {
  def name: String
}

final case class Tube(name: String) extends TflMode

final case class Overground(name: String) extends TflMode

/* Line */
case class LineStatus(
  statusSeverity: Int,
  statusSeverityDescription: String,
  reason: Option[String],
  validityPeriods: Option[List[ValidityPeriod]]
)

case class ValidityPeriod(
  isNow: Boolean,
  fromDate: DateTime,
  toDate: DateTime
)

case class Line(
  id: String,
  name: String,
  modeName: String,
  lineStatuses: List[LineStatus],
)

/* Plumbing */
object Constant {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
}

object JsonReads {
  implicit val dateReads = JodaReads.jodaDateReads(Constant.dateFormat)
  implicit val validityPeriodReads = Json.reads[ValidityPeriod]
  implicit val lineStatusReads = Json.reads[LineStatus]
  implicit val lineReads = Json.reads[Line]
}

object JsonWrites {
  implicit val dateWrites = JodaWrites.jodaDateWrites(Constant.dateFormat)
  implicit val validityPeriodWrites = Json.writes[ValidityPeriod]
  implicit val lineStatusWrites = Json.writes[LineStatus]
  implicit val lineWrites = Json.writes[Line]
}
