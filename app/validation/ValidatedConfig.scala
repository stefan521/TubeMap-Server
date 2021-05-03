package validation

import cats.implicits._
import model.ApiKey
import play.api.ConfigLoader._
import play.api.{Configuration, Logger}
import validation.Validation.ValidationResult

class ValidatedConfig(config: Configuration) {
  val logger: Logger = play.api.Logger(getClass)

  lazy val apiKey: Option[ApiKey] = {
    val validatedApiKey = validateApiKey(config)

    validatedApiKey.leftMap { err =>
      logger.warn(s"API KEY ERROR: ${err.map(_.errorMessage)}")
    }

    validatedApiKey.toOption
  }

  def validateApiKey(config: Configuration): ValidationResult[ApiKey] = {
    val apiKey = config.get[String](Constants.configPathForApiKey)

    ConfigValidation.validateApiKey(apiKey).map(ApiKey)
  }
}
