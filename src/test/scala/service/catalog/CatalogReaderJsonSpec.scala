package service.catalog

import com.typesafe.config.ConfigFactory
import exceptions.FailedToLoadTheCatalog
import model.catalog.Catalog
import model.item.{Item, SpecialOffer, SupplementItem}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class CatalogReaderJsonSpec extends AnyFlatSpec with GivenWhenThen {
  "CatalogReaderJson" should "create Catalog of items from a json file." in {
    Given("name of the json file with items")
    val pathToCatalog = "catalogFilePath"

    When("reader decode json file")
    val testtCatalog =
      CatalogReaderJson.getCurrentCatalogFromPath(s"/${ConfigFactory.load().getString(pathToCatalog)}")

    Then("decoded file is a catalog of items available in the shop.")
    val expectedCatalog = Catalog(Set(
      Item("Milk", 1.30, None),
      Item("Soup", 0.65, None),
      Item("Bread", 0.80, Some(Seq(SpecialOffer(0.5, Some(SupplementItem(Item("Soup", 0.65, None), 2)))))),
      Item("Apples", 1.00, Some(Seq(SpecialOffer(0.1, None))))
    ))
    assert(testtCatalog === expectedCatalog)
  }

  it should "throw exception if file does not exist" in {
    Given("name of the json file with items")
    val pathToCatalog = "catalogFilePathThatDoesNotExist"

    Then("reader tries to decode json file exception should be thrown if file does not exist.")
    assertThrows[FailedToLoadTheCatalog] {
      CatalogReaderJson.getCurrentCatalogFromPath(s"/${ConfigFactory.load().getString(pathToCatalog)}")
    }
  }

  it should "throw exception if file is corrupted" in {
    Given("name of the json file with items")
    val pathToCatalog = "corruptedCatalog"

    Then("reader tries to decode json file exception should be thrown if file is corrupted.")
    assertThrows[FailedToLoadTheCatalog] {
      CatalogReaderJson.getCurrentCatalogFromPath(s"/${ConfigFactory.load().getString(pathToCatalog)}")
    }
  }
}
