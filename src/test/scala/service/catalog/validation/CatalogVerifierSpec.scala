package service.catalog.validation

import exceptions.{DuplicateItemsExist, IncorrectlyProvidedSupplementItems, InvalidItems}
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

    When("CatalogVerifier validates the correct catalog")
    CatalogVerifier.validate(catalog)

    Then("no exception should be thrown.")
  }

  it should "throw duplication exception if duplicate items exist" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("Milk", 1.30, None),
      Item("milk", 1.50, None)
    ))

    Then("exception should be thrown if duplicate items exist.")
    assertThrows[DuplicateItemsExist] {
      CatalogVerifier.validate(catalog)
    }
  }

  it should "throw exception if duplicate items exist" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("Milk", 1.30, None),
      Item("Bread", 0.80, Some(Seq(SpecialOffer(0.5, Some(SupplementItem(Item("Soup", 0.65, None), 2)))))),
      Item("Apples", 1.00, Some(Seq(SpecialOffer(0.1, None))))
    ))

    Then("exception should be thrown if supplement item exists in special offer, but not in catalog.")
    assertThrows[IncorrectlyProvidedSupplementItems] {
      CatalogVerifier.validate(catalog)
    }
  }

  it should "throw exception if items with negative price exist" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("Milk", -1.30, None),
      Item("Apples", 1.00, Some(Seq(SpecialOffer(0.1, None))))
    ))

    Then("exception should be thrown if items with negative price exist.")
    assertThrows[InvalidItems] {
      CatalogVerifier.validate(catalog)
    }
  }

  it should "throw exception if items with incorrect name exist" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("@@@@@M!lk", 1.30, None),
      Item("Apples", 1.00, Some(Seq(SpecialOffer(0.1, None))))
    ))

    Then("exception should be thrown if item with incorrect name exist.")
    assertThrows[InvalidItems] {
      CatalogVerifier.validate(catalog)
    }
  }
}