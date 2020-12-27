package model.catalog

import model.item.Item
import exceptions._

import scala.language.reflectiveCalls

/** Catalog contains information about available items in the shop's catalog
  *
  * @param items set of items
  *
  */
case class Catalog(items: Set[Item]) {
  implicit def enhanceWithContainsDuplicates[T](s: List[T]): Object {
    def containsDuplicates: Boolean} = new {def containsDuplicates: Boolean = s.distinct.size != s.size}

  /**
    * checks that items are correct
    *
    * @return itself
    */
  //TODO add more checks
  def validate: Catalog = {
    verifyThatItemsWithTheSameNameDoNotExist
    verifyThatSupplementItemDoesNotExistInCatalog
  }

  /**
    * checks that there are no duplicate items
    *
    * @return itself
    */
  private def verifyThatItemsWithTheSameNameDoNotExist: Catalog = {
    if (this.items.map(_.name.trim.toLowerCase).toList.containsDuplicates) throw new CatalogValidationFailed
    else this
  }

  /**
    * checks if there is a condition of buying supplement item
    * then this item exists
    *
    * @return itself
    */
  private def verifyThatSupplementItemDoesNotExistInCatalog: Catalog =
    if (this.items
      .map(_.specialOffers)
      .filter(_.nonEmpty)
      .flatMap(_.get)
      .map(_.supplementItem)
      .filter(_.nonEmpty)
      .map(_.get.item)
      .forall(supplementItem => items.contains(supplementItem))) this
    else throw new IncorrectlyProvidedSupplementItems
}