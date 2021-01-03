package service.catalog.validation

import exceptions._
import model.catalog.Catalog

object CatalogVerifier {
  /**
    * checks that items are correct
    *
    */
  //TODO add more checks
  def validate(catalog: Catalog): Unit = {
    verifyThatDuplicateItemsDoNotExist(catalog)
    verifyThatAllItemsCorrect(catalog)
    verifyThatOnlyOneOverallDiscountExistsPerItem(catalog)
    verifyThatSupplementItemDoesNotExistInCatalog(catalog)
  }

  /**
    * checks that there are no duplicate items
    *
    * @param catalog catalog of items
    */
  private def verifyThatDuplicateItemsDoNotExist(catalog: Catalog): Unit = {
    if (!catalog.items.groupBy(_.name.trim.capitalize).values.forall(_.size == 1)) throw DuplicateItemsExist()
  }

  /**
    * checks if there is a condition of buying supplement item
    * then this item exists
    *
    * @param catalog catalog of items
    */
  private def verifyThatSupplementItemDoesNotExistInCatalog(catalog: Catalog): Unit =
    if (!catalog.items
      .map(_.specialOffers)
      .filter(_.nonEmpty)
      .flatMap(_.get)
      .map(_.supplementItem)
      .filter(_.nonEmpty)
      .map(_.get.item)
      .forall(supplementItem => catalog.items.contains(supplementItem))) throw IncorrectlyProvidedSupplementItems()

  /**
    * checks if there is only one overall discount per item
    *
    * @param catalog catalog of items
    */
  private def verifyThatOnlyOneOverallDiscountExistsPerItem(catalog: Catalog): Unit =
    catalog.items
      .filter(item => item.specialOffers.nonEmpty)
      .map(_.specialOffers.get)
      .map(seq => seq.count(_.supplementItem.isEmpty))
      .foreach(count => if (count > 1) throw TwoOverallDiscountsCantExist())
  /**
    * checks that all items are valid
    *
    * @param catalog catalog of items
    */
  private def verifyThatAllItemsCorrect(catalog: Catalog): Unit =
    catalog.items.map(item => ItemValidator.validateItem(item.name, item.price, item.specialOffers))
      .map(result => result.fold(errors => new Exception(errors.toList.mkString(" ")), result => result))
      .foreach {
        case value: java.lang.Exception =>
          println(value)
          throw InvalidItems()
        case value => value
      }
}
