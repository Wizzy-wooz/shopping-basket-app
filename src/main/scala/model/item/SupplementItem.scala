package model.item

/** SupplementItem specifies how many other items need to be bought in order to get discount for this item
  *
  *  @param item additional item
  *  @param quantity number of items to buy to get discount
  */
case class SupplementItem(item: Item, quantity: Int)

// supplement item is an item without special offers
case object SupplementItem{
  def apply(item: Item, quantity: Int): SupplementItem =
    new SupplementItem(Item(item.name, item.price, None), quantity)
}