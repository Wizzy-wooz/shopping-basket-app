package service.catalog.validation

import cats.data.Validated._
import cats.data._
import cats.implicits._
import model.item.{Item, SpecialOffer}
import constants._
import scala.collection.mutable.ListBuffer

/**
  * Interface that defines ways of item's service.catalog.validation
  *
  */
sealed trait ItemValidator {

  type ValidationResult[A] = ValidatedNel[String, A]

  private def validateName(itemName: String): ValidationResult[String] =
    if (itemName.matches(RegexOnlyLetters) && itemName.length > 0) itemName.validNel
    else s"Item $ItemNameIncorrectMsg".invalidNel

  private def validatePrice(price: BigDecimal): ValidationResult[BigDecimal] =
    if (price >= 0) price.validNel else s"Item's $PriceIncorrectMsg".invalidNel

  private def validateSpecialOffers(specialOffers: Option[Seq[SpecialOffer]]): ValidationResult[Option[Seq[SpecialOffer]]] = {
    val validationErrors = new ListBuffer[String]()
    val nonEmptySpecialOffers = specialOffers.nonEmpty

    def validateSupplementItem() = {
      if (nonEmptySpecialOffers && specialOffers.get.exists(so => so.supplementItem.nonEmpty
        && !so.supplementItem.get.item.name.matches(RegexOnlyLetters) || so.supplementItem.get.item.name.length < 0)) {
        validationErrors += s"Supplement item's $ItemNameIncorrectMsg"
      }
      if (nonEmptySpecialOffers && specialOffers.get.exists(so => so.supplementItem.nonEmpty
        && so.supplementItem.get.item.price <= 0)) {
        validationErrors += s"Supplement item's $PriceIncorrectMsg"
      }
    }

    if (nonEmptySpecialOffers && specialOffers.get.exists(so => so.discount <= 0)) {
      validationErrors += DiscountIncorrectMsg
      if (nonEmptySpecialOffers && specialOffers.get.exists(so => so.supplementItem.nonEmpty && so.supplementItem.get.quantity <= 0)) {
        validationErrors += QuantityIncorrectMsg
        validateSupplementItem()
      }
      validationErrors.mkString("").invalidNel
    } else specialOffers.validNel
  }

  def validateItem(name: String, price: BigDecimal, specialOffers: Option[Seq[SpecialOffer]]): ValidationResult[Item] = {
    (validateName(name),
      validatePrice(price),
      validateSpecialOffers(specialOffers)).mapN(Item)
  }
}

object ItemValidator extends ItemValidator