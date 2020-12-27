package service.catalog

import model.catalog.Catalog

/**
  * Interface that defines ways of reading catalog(s)
  *
  */
trait CatalogReader {
  /**
    * reads catalog from file's path
    *
    * @param path filepath as a String
    * @return catalog of items
    */
  def getCurrentCatalogFromPath(path: String): Catalog
}