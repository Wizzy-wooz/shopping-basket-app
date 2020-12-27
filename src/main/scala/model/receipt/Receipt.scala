package model.receipt

/** Purchase is special type to keep data about item(s) bought
  *
  * @param purchases information about items in basket to be checkout
  *
  */
case class Receipt(purchases: Seq[Purchase])