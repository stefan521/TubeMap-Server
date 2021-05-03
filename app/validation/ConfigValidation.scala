package validation

import cats.data.ValidatedNec
import cats.implicits._
import validation.Validation._
import Constants._

object ConfigValidation {
  private def validateStringLengthAtLeast(length: Int, str: String): ValidatedNec[TooShort, String] =
    if (str.length >= length) str.validNec
    else TooShort(str, length).invalidNec

  private def validateStringLengthAtMost(length: Int, str: String): ValidatedNec[TooLong, String] =
    if (str.length <= length) str.validNec
    else TooLong(str, length).invalidNec

  private def validateHexadecimalFormat(str: String): ValidatedNec[NoHexadecimalFormat, String] =
    if (str.matches(hexadecimalRegex)) str.validNec
    else NoHexadecimalFormat(str).invalidNec

  private def validateStringNotNull(str: String): ValidatedNec[Validation.NoApiKeyProvided.type, String] =
    if (str == null) NoApiKeyProvided.invalidNec
    else str.validNec

  def validateApiKey(value: String): ValidationResult[String] =
    validateStringNotNull(value)
      .combine(validateStringLengthAtLeast(minApiKeyLength, value))
      .combine(validateHexadecimalFormat(value))
      .combine(validateStringLengthAtMost(maxApiKeyLength, value))
}
