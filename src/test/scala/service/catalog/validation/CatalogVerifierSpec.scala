package service.catalog.validation

import exceptions.{DuplicateItemsExist, IncorrectlyProvidedSupplementItems, InvalidItems, TwoOverallDiscountsCantExist}
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

  it should "throw exception if discount is 0" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("M!lk", 1.30, None),
      Item("Apples", 1.00, Some(Seq(SpecialOffer(0, None))))
    ))

    Then("exception should be thrown if discount is 0.")
    assertThrows[InvalidItems] {
      CatalogVerifier.validate(catalog)
    }
  }

  it should "throw exception if discount is negative" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("Apples", 1.00, Some(Seq(SpecialOffer(-0.1, None))))
    ))

    Then("exception should be thrown if discount is negative.")
    assertThrows[InvalidItems] {
      CatalogVerifier.validate(catalog)
    }
  }

  it should "throw exception if quantity of supplement item is 0" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("Bread", 0.80, Some(Seq(SpecialOffer(0.5, Some(SupplementItem(Item("Soup", 0.65, None), 0)))))),
    ))

    Then("exception should be thrown if quantity of supplement item is 0.")
    assertThrows[IncorrectlyProvidedSupplementItems] {
      CatalogVerifier.validate(catalog)
    }
  }

  it should "throw exception if quantity of supplement item is negative" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("Bread", 0.80, Some(Seq(SpecialOffer(0.5, Some(SupplementItem(Item("Soup", 0.65, None), -2)))))),
    ))

    Then("exception should be thrown if quantity of supplement item is negative.")
    assertThrows[IncorrectlyProvidedSupplementItems] {
      CatalogVerifier.validate(catalog)
    }
  }

  it should "throw exception if overall discount number per item is more than 1" in {
    Given("Current catalog")
    val catalog = Catalog(Set(
      Item("Bread", 0.80, Some(Seq(SpecialOffer(0.5, None), SpecialOffer(0.5, None)))),
    ))

    Then("exception should be thrown 0f overall discount number per item is more than 1.")
    assertThrows[TwoOverallDiscountsCantExist] {
      CatalogVerifier.validate(catalog)
    }
  }
}