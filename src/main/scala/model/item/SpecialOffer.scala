package model.item

/** Special offer defines discount applied to item's price
  *
  *  @param discount percent of discount.
  *  @param supplementItem if specified additional item id and its quantity that should be bought in order to get discount
  *                         case not set => discount will be applied
  *                         case set => basket will be checked for existence of supplement purchasing item
  *
  *  examples:
  *          Use Case 1: Apples have a 10% discount off their normal price this week - new SpecialOffer(10)
  *          USe Case 2: Buy 2 tins of soup and get a loaf of bread for half price - new SpecialOffer(50, SupplementItem("1", 2))
  */
case class SpecialOffer(discount: BigDecimal, supplementItem: Option[SupplementItem])
