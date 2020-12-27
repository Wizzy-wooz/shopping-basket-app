package service.catalog.validation

import exceptions.{CatalogValidationFailed, IncorrectlyProvidedSupplementItems}
import model.catalog.Catalog

object CatalogVerifier {
  implicit def enhanceWithContainsDuplicates[T](s: List[T]): Object {
    def containsDuplicates: Boolean} = new {def containsDuplicates: Boolean = s.distinct.size != s.size}

  /**
    * checks that items are correct
    *
    * @return itself
    */
  //TODO add more checks
  def validate(catalog: Catalog): Unit = {
    verifyThatItemsWithTheSameNameDoNotExist(catalog)
    verifyThatSupplementItemDoesNotExistInCatalog(catalog)
    verifyThatItemsWithTheSameNameDoNotExist(catalog)
  }

  /**
    * checks that there are no duplicate items
    *
    * @return itself
    */
  private def verifyThatItemsWithTheSameNameDoNotExist(catalog: Catalog): Unit =
    if(catalog.items.map(_.name.trim.toLowerCase).toList.containsDuplicates) throw new CatalogValidationFailed

  /**
    * checks if there is a condition of buying supplement item
    * then this item exists
    *
    * @return itself
    */
  private def verifyThatSupplementItemDoesNotExistInCatalog(catalog: Catalog): Unit =
    if (!catalog.items
      .map(_.specialOffers)
      .filter(_.nonEmpty)
      .flatMap(_.get)
      .map(_.supplementItem)
      .filter(_.nonEmpty)
      .map(_.get.item)
      .forall(supplementItem => catalog.items.contains(supplementItem))) throw new IncorrectlyProvidedSupplementItems

  private def verifyThatAllItemsCorrect(catalog: Catalog): Unit =
    catalog.items.map(item => ItemValidator.validateItem(item.name, item.price, item.specialOffers))
      .map(result => result.fold(errors => new Exception(errors.toList.mkString(" ")), result => result))
      .foreach{
        case value:java.lang.Exception =>
          println(value)
          throw new CatalogValidationFailed
        case value => value
      }
}
