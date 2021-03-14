package core

import model.{AppError, FailedToReadJson}
import org.bson.Document
import play.api.libs.json.{JsError, JsPath, JsSuccess, JsValue, Json, Reads}

object JsonParser {

  def jsResultToEither[A](stringJson: String)(implicit readsA: Reads[A]): Either[AppError, A] =
    Json.fromJson[A](Json.parse(stringJson)) match {
      case JsSuccess(successfulResult: A, _: JsPath) =>
        Right(successfulResult)

      case JsError(_) =>
        Left(FailedToReadJson(stringJson))
    }
}
