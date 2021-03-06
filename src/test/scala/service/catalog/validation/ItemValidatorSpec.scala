package service.catalog.validation

import cats.implicits._
import model.item.{Item, SpecialOffer, SupplementItem}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ItemValidatorSpec extends AnyFlatSpec with GivenWhenThen{
  "ItemValidator" should "check correctness of the item" in {
    Given("Item")
    val item = Item("Bread", 0.80, Some(Seq(SpecialOffer(0.5, Some(SupplementItem(Item("Soup", 0.65, None), 2))))))

    When("item being validated")
    val validationResult: ItemValidator.ValidationResult[Item] =
      ItemValidator.validateItem(item.name, item.price, item.specialOffers)

    Then("its validatedResult should be valid.")
    val validatedName:ItemValidator.ValidationResult[String] = item.name.validNel
    val validatedPrice: ItemValidator.ValidationResult[BigDecimal] = item.price.validNel
    val validatedSpecialOffers:ItemValidator.ValidationResult[Option[Seq[SpecialOffer]]] = item.specialOffers.validNel
    val validatedItem: ItemValidator.ValidationResult[Item] = (validatedName, validatedPrice, validatedSpecialOffers).mapN(Item)
    assert(validationResult == validatedItem)
  }
}
