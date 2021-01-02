package model.receipt

/** Purchase is special type to keep data about item(s) bought
  *
  * @param itemDescription information about item,
  * @param subtotal amount to pay fot items bought
  * @param specialOffersDescription special offers applied
  *
  */
case class Purchase(itemDescription: String, subtotal: BigDecimal, specialOffersDescription: String)