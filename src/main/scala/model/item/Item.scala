package model.item

/** Item defines an item for sale
  *
  *  @param name naming according shop convention
  *  @param price how much costs an item
  *  @param specialOffers which and how many items need to be bought in order to get discount
  */
case class Item (name: String,
                 price: BigDecimal,
                 specialOffers: Option[Seq[SpecialOffer]])