package service.catalog

import exceptions.FailedToLoadTheCatalog
import io.circe.generic.auto._
import io.circe.parser.decode
import model.catalog.Catalog

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * Reader that works with json files
  *
  */
object CatalogReaderJson extends CatalogReader{
  /**
    * reads catalog from file's path
    *
    * @param path filepath as a String
    * @return catalog of items
    */
  override def getCurrentCatalogFromPath(path: String): Catalog = {
    val catalogJson = Try(Source.fromInputStream(getClass.getResourceAsStream(path)).getLines.mkString) match {
      case Success(value) => value
      case Failure(e) => throw FailedToLoadTheCatalog(s"Due to exception: ${e.getStackTrace}")
    }
    decode[Catalog](catalogJson) match {
      case Left(failure) =>
        throw new RuntimeException(s"Failed to parse file $path due to ${failure.getStackTrace}.")
      case Right(parsedCatalog) => parsedCatalog
    }
  }
}
