package service.basket

import exceptions.NotProvidedItems
import model.catalog.Catalog
import model.item.Item
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class BasketServiceSpec extends AnyFlatSpec with GivenWhenThen{
  val catalog = Catalog(Set(Item("Milk", 1.30, None), Item("Apples", 1.00, None), Item("Soup", 0.65, None)))
  val basketService = new BasketServiceCommandLineInput(catalog)

  "BasketService" should "create a basket from input parameters. " +
    "Basket contains selected items and their number." in {
    Given("Catalog of items available and list of items Customer wants to buy")
    val selectedItems = Array("Milk", "Apples", "Milk")

    When("all selected items put in the basket")
    val basket = basketService.createBasket(selectedItems)

    Then("it contains all selected items with correct quantity.")
    assert(basket === Map(Item("Milk", 1.30, None) -> 2, Item("Apples", 1.00, None) -> 1))
    }

  it should "produce NotProvidedItems when no items selected" in {
    assertThrows[NotProvidedItems] {
      basketService.createBasket(Array[String]())
    }
  }

  it should "produce IllegalArgumentException when items selected do not exist in catalog" in {
    assertThrows[IllegalArgumentException] {
      basketService.createBasket(Array[String]("Oranges"))
    }
  }
}
