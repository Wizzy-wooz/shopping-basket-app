package service.cashier

import model.item.{Item, SpecialOffer}
import model.receipt.{Purchase, Receipt}
import scala.collection.immutable

/**
  * Service provides all operations that a cashier can do: checkout, special offers info, etc.
  *
  */
object CashierService {

  /**
    * Creates basket containing info about the item and how many of it selected
    *
    * @param basket item and how many user wants to buy
    * @return info about purchases such as itemDescription,
    *         total amount to pay per item of selected category,
    *         specialOfferDescription
    */
  def checkOut(basket: Map[Item, Int]): Receipt = {
    val purchases: immutable.Iterable[Purchase] = basket map {
      case (itemToBuy, quantity) => itemToBuy match {

        //use-case1: no special offers
        case item if item.specialOffers.isEmpty => {
          Purchase(s"${itemToBuy.name} price:${item.price}£ qty:$quantity",
            itemToBuy.price * quantity,
            constants.NoOffers)
        }

        //use-case2: items with special offers
        case item => {
          val totalSum = item.price * quantity
          val discountAndSpecialOfferDescription = item.specialOffers.get.map {

            //use-case2.1: items with special offers: discount applied for all items
            case specialOffer if specialOffer.supplementItem.isEmpty =>
              val discount = totalSum * specialOffer.discount
              (discount, s"${itemToBuy.name} ${(specialOffer.discount * 100).intValue()}% off: £${discount.doubleValue()}")

            //use-case2.2: items with special offers: discount applied for certain amount of items and for all items if such exists
            case specialOffer =>
              val discount = getNumberOfItemsWithDiscount(specialOffer, basket) * specialOffer.discount * item.price
              val totalDiscount: BigDecimal = if (discount > totalSum) totalSum else discount
              val discountFormatted = (specialOffer.discount * 100).intValue()

              (totalDiscount,
                s"${itemToBuy.name} $discountFormatted% off: £${totalDiscount.doubleValue()} -" +
                  s" got $discountFormatted% off for 1 ${itemToBuy.name} b" +
                  s"y buying ${specialOffer.supplementItem.get.quantity} ${specialOffer.supplementItem.get.item.name}")
          }.toMap

          val discount: BigDecimal = discountAndSpecialOfferDescription.keySet.sum
          Purchase(
            s"${itemToBuy.name} price:${item.price}£ qty:$quantity",
            if (totalSum - discount < 0) 0 else totalSum - discount,
            discountAndSpecialOfferDescription.values.mkString("\n"))
        }
      }
    }
    Receipt(purchases.toList.distinct)
  }

  /**
    * Finds total sum for receipt
    *
    * @param receipt
    * @return
    */
  def findTotalSum(receipt: Receipt): Double = receipt.purchases.map(_.subtotal).sum.doubleValue()

  /**
    * Finds how many are there item's supplement item in a basket and how many should be bought to get discount.
    *  If there is no such item in a basket than discount is not applied to item
    *  else amount of supplement items in basket is divided by amount of supplement items required by special offer
    *  and the number of items that get discount is found
    *
    * @param so special offers of the item selected
    * @param basket item and how many user wants to buy
    * @return number of items that gets discount according to special offer's conditions
    */
  private def getNumberOfItemsWithDiscount(so: SpecialOffer, basket: Map[Item, Int]): Int = {
    val supplementItem = so.supplementItem.get
    if (basket.get(supplementItem.item).nonEmpty) basket(supplementItem.item) / supplementItem.quantity else 0
  }
}
