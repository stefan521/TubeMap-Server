package validation

import com.typesafe.config.Config

import scala.util.{Failure, Success, Try}


object ConfigValidation {

  /*
    PRIVATE
   */
  private val configPathForApiKey = "unified.api_key"

  private val noApiKeyError = new IllegalStateException(
    """"
      |There is no Tfl API key stored in the environment variable TUBE_API_KEY.
      |You can obtain one at: https://api-portal.tfl.gov.uk/
      |""".stripMargin
  )

  /*
    PUBLIC
   */
  def getApiKeyOrThrowable(config: Config): Either[Throwable, String] =
    if (config.hasPath(configPathForApiKey))
      Try(config.getValue(configPathForApiKey)) match {

        case Failure(exception) =>
          Left(exception)

        case Success(value) =>
          Right(value.render())
      }
    else
      Left(noApiKeyError)

  // TODO: validate this is a string and has a hexadecimal format at least 10 chars long and at most 50
}
