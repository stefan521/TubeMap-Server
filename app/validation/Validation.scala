package validation

import cats.data.ValidatedNec

object Validation {
  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  sealed trait DomainValidation {
    def errorMessage: String
  }

  object NoApiKeyProvided extends DomainValidation {
    override def errorMessage: String =
      """"
        |There is no Tfl API key stored in the environment variable TUBE_API_KEY.
        |You can obtain one at: https://api-portal.tfl.gov.uk/
        |""".stripMargin
  }

  case class NoHexadecimalFormat(key: String) extends DomainValidation {
    override def errorMessage: String = s"$key must be a hexadecimal number"
  }

  case class TooShort(key: String, minLength: Int) extends DomainValidation {
    override def errorMessage: String = s"$key is too short. It must be at least $minLength characters long."
  }

  case class TooLong(key: String, maxLength: Int) extends DomainValidation {
    override def errorMessage: String = s"$key is too long. It must be at most $maxLength characters long."
  }

  case class Error(msg: String) extends DomainValidation {
    override def errorMessage: String = msg
  }
}
