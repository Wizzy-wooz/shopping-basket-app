package service.cashier

import model.catalog.Catalog
import model.item.{Item, SpecialOffer, SupplementItem}
import model.receipt.{Purchase, Receipt}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import service.basket.BasketServiceCommandLineInput

class CashierServiceSpec extends AnyFlatSpec with GivenWhenThen {

  val catalog = Catalog(Set(
    Item("Milk", 1.30, None),
    Item("Soup", 0.65, None),
    Item("Bread", 0.80, Some(Seq(SpecialOffer(0.5, Some(SupplementItem(Item("Soup", 0.65, None), 2)))))),
    Item("Apples", 1.00, Some(Seq(SpecialOffer(0.1, None))))
  ))
  val basketService = new BasketServiceCommandLineInput(catalog)

  "CashierService" should "create a receipt for items in the basket." in {
    Given("Catalog of items available and basket")
    val selectedItems = Array("Milk", "Apples", "Soup", "Soup", "Bread")
    val basket = basketService.createBasket(selectedItems)

    When("checkout the basket")
    val receipt = CashierService.checkOut(basket)

    Then("it must contain item name, price and quantity to buy, how much to pay per purchase, " +
      "info about which special offers applied and the amount of discount")
    val expectedReceipt = Receipt(Set(
      Purchase("Bread price:0.8£ qty:1", 0.40, "Bread 50% off: £0.4 - got 50% off for 1 Bread by buying 2 Soup"),
      Purchase("Apples price:1.0£ qty:1", 0.90, "Apples 10% off: £0.1"),
      Purchase("Soup price:0.65£ qty:2", 1.30, "(No offers available)"),
      Purchase("Milk price:1.3£ qty:1", 1.3, "(No offers available)")))
    assert(receipt === expectedReceipt)
  }

  it should "create a receipt with discount only for those items that have special offers." in {
    Given("Catalog of items available and basket with 2 soups and 2 breads.")
    val selectedItems = Array("Bread", "Soup", "Soup", "Bread")
    val basket = basketService.createBasket(selectedItems)

    When("checkout the basket")
    val receipt = CashierService.checkOut(basket)

    Then("Subtotal for bread is 1.20£ (0,8£(no discount) + 0.4£(50% off))")
    val expectedReceipt = Receipt(Set(
      Purchase("Bread price:0.8£ qty:2", 1.20, "Bread 50% off: £0.4 - got 50% off for 1 Bread by buying 2 Soup"),
      Purchase("Soup price:0.65£ qty:2", 1.30, "(No offers available)")))
    assert(receipt === expectedReceipt)
  }
}