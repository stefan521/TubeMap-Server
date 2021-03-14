package model

import org.joda.time.DateTime
import play.api.libs.json.{JodaReads, JodaWrites, Json, OWrites, Reads, Writes}


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

case class TubeStatus(lines: List[Line])

/* Plumbing */
object Constant {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
}

object JsonReads {
  implicit val dateReads: Reads[DateTime]                 = JodaReads.jodaDateReads(Constant.dateFormat)
  implicit val validityPeriodReads: Reads[ValidityPeriod] = Json.reads[ValidityPeriod]
  implicit val lineStatusReads: Reads[LineStatus]         = Json.reads[LineStatus]
  implicit val lineReads: Reads[Line]                     = Json.reads[Line]
  implicit val tubeStatusReads: Reads[TubeStatus]         = Json.reads[TubeStatus]
}

object JsonWrites {
  implicit val dateWrites: Writes[DateTime]                 = JodaWrites.jodaDateWrites(Constant.dateFormat)
  implicit val validityPeriodWrites: Writes[ValidityPeriod] = Json.writes[ValidityPeriod]
  implicit val lineStatusWrites: Writes[LineStatus]         = Json.writes[LineStatus]
  implicit val lineWrites: Writes[Line]                     = Json.writes[Line]
  implicit val tubeStatusWrites: Writes[TubeStatus]         = Json.writes[TubeStatus]
}
