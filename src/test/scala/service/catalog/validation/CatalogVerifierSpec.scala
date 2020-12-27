package service.catalog.validation

import model.catalog.Catalog
import model.item.{Item, SpecialOffer, SupplementItem}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class CatalogVerifierSpec extends AnyFlatSpec with GivenWhenThen {
  "CatalogVerifier" should "validate correctness of the items in it" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("Milk", 1.30, None),
      Item("Soup", 0.65, None),
      Item("Bread", 0.80, Some(Seq(SpecialOffer(0.5, Some(SupplementItem(Item("Soup", 0.65, None), 2)))))),
      Item("Apples", 1.00, Some(Seq(SpecialOffer(0.1, None))))
    ))

    When("CatalogVerifier validates the catalog")
    CatalogVerifier.validate(catalog)

    Then("no exception should be thrown.")
  }
}