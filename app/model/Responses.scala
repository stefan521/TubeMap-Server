package model

sealed trait AppError
case class GenericError(errorMsg: String) extends AppError
case class NoCollection(collectionName: String) extends AppError
case class CollectionEmpty(collectionName: String) extends AppError
case class FailedToReadJson(badJson: String) extends AppError


trait CacheResult
case object CacheUnchanged extends CacheResult
case object CacheUpdated extends CacheResult
