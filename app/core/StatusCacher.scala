package core

import cats.implicits.catsSyntaxEitherId
import com.mongodb.client.MongoCollection
import controllers.Application.mongoDb
import core.JsonParser.jsResultToEither
import model.{AppError, CacheResult, CacheUnchanged, CacheUpdated, CollectionEmpty, NoCollection, TubeStatus}
import org.bson.{BsonDocument, Document}
import play.api.Logger
import play.api.libs.json.Json

import scala.util.{Failure, Success, Try}


object StatusCacher {

  import model.JsonReads._
  import model.JsonWrites._

  val collectionName = "status"
  val logger: Logger = play.api.Logger(getClass)

  def cacheStatus(status: TubeStatus): Either[AppError, CacheResult] =
    for {
      collection <- getMongoCollection(collectionName)
      result <- updateStatus(status, collection)
    } yield result

  private def updateStatus(
    newStatus: TubeStatus,
    collection: MongoCollection[Document]
  ): Either[AppError, CacheResult] = {
    val newStatusMongoDoc = Document.parse(Json.stringify(Json.toJson(newStatus)))

    for {
      oldStatus <- upsertStatus(collection, newStatusMongoDoc)
      statusUnchanged <- Right(oldStatus.exists(_.equals(newStatusMongoDoc)))
      cacheResult <- if (statusUnchanged) Right(CacheUnchanged) else Right(CacheUpdated)
    } yield cacheResult
  }

  private def upsertStatus(collection: MongoCollection[Document], newStatus: Document): Either[AppError, Option[Document]] = {
    collectionEmpty(collection).flatMap(isEmpty => Right {
      if (isEmpty) {
        collection.insertOne(newStatus)
        None
      } else {

        // Todo: Better error handling with mongo interactions.
        // Todo: Ignore Id, add timestamp
        Some(collection.findOneAndReplace(new Document(), newStatus))
      }
    })
  }

  private def collectionEmpty(collection: MongoCollection[Document]): Either[AppError, Boolean] =
    (collection.countDocuments() == 0).asRight[AppError]

  private def getMongoCollection(name: String): Either[NoCollection, MongoCollection[Document]] =
    Try(mongoDb.getCollection(name)) match {
      case Success(value) =>
        Right(value)

      case Failure(exception) =>
        logger.error(s"Error getting the $collectionName collection ${exception.getMessage}")
        Left(NoCollection(collectionName))
    }
}
