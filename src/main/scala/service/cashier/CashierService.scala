package service.cashier

import model.item.{Item, SpecialOffer}
import model.receipt.{Purchase, Receipt}
import scala.collection.immutable
import scala.math.BigDecimal.RoundingMode._

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
        case item if item.specialOffers.isEmpty =>
          Purchase(s"${itemToBuy.name} price:${item.price}£ qty:$quantity",
            itemToBuy.price * quantity,
            constants.NoOffers)

        //use-case2: items with special offers discount but without supplement items discounts
        case  item if item.specialOffers.get.forall(_.supplementItem.isEmpty) =>
          val discountAppliedForAllItems: BigDecimal = item.specialOffers.get.map(_.discount).sum
          Purchase(s"${itemToBuy.name} price:${item.price}£ qty:$quantity",
            itemToBuy.price * quantity * (1- discountAppliedForAllItems),
            s"${itemToBuy.name} ${(discountAppliedForAllItems * 100).intValue()}% off: £${discountAppliedForAllItems.setScale(2, HALF_UP)}")

        //use-case3: items with all kinds of special offers:
        // initially supplement items discounts is calculated and then overall discount is applied
        case item =>
          val totalSum = item.price * quantity

          val discountAppliedForBuyingSupplementItems: Map[String, BigDecimal] = item.specialOffers.get.filter(_.supplementItem.nonEmpty).map{

            //items with special offers: discount applied for certain amount of items and then for all items if such exists
            specialOffer =>
              val numberOfItemsWithDiscountForBuyingSupplementItems = getNumberOfItemsWithDiscount(specialOffer, basket)
              val discount = numberOfItemsWithDiscountForBuyingSupplementItems * specialOffer.discount * item.price
              // here discount is 100%, get for free items for buying additional ones
              val maxDiscount = getNumberOfItemsWithDiscount(specialOffer, basket) * item.price
              //checking discount to avoid giving discount more than possible
              val totalDiscountForBuyingSupplementItems: BigDecimal = if (discount > maxDiscount) maxDiscount else discount
              val discountFormatted = (specialOffer.discount * 100).intValue()

              (s"${itemToBuy.name} $discountFormatted% off: £${totalDiscountForBuyingSupplementItems.setScale(2, HALF_UP)} -" +
                  s" got $discountFormatted% off for each ${itemToBuy.name} b" +
                  s"y buying ${specialOffer.supplementItem.get.quantity} ${specialOffer.supplementItem.get.item.name}",
                totalDiscountForBuyingSupplementItems)
          }.toMap

          val discountAppliedForSupplementItems: BigDecimal = discountAppliedForBuyingSupplementItems.values.sum
          val specialOffersForBuyingSupplementItemsDescription = discountAppliedForBuyingSupplementItems.keySet.mkString("\n")
          val discountAppliedForAllItems: BigDecimal = item.specialOffers.get.filter(_.supplementItem.isEmpty).map(_.discount).sum

          val subtotal = (totalSum - discountAppliedForSupplementItems) * (1 - discountAppliedForAllItems)
          val subTotalDiscount = (totalSum - discountAppliedForSupplementItems) *discountAppliedForAllItems

          val specialOffersDescription  =
            if (discountAppliedForAllItems <= 0) specialOffersForBuyingSupplementItemsDescription
            else specialOffersForBuyingSupplementItemsDescription + "\n" + s"Plus for all " +
              s"${itemToBuy.name} ${(discountAppliedForAllItems * 100).intValue()}% off:" +
              s" £${subTotalDiscount.setScale(2, HALF_UP)}"

          Purchase(s"${itemToBuy.name} price:${item.price}£ qty:$quantity", if (subtotal < 0) 0 else subtotal, specialOffersDescription)
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
  def findTotalSum(receipt: Receipt): BigDecimal = receipt.purchases.map(_.subtotal).sum

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
