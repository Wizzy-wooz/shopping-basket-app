package service.basket

import model.item.Item

/**
  * Interface that defines basket functionality
  *
  */
trait BasketService {
  /**
    * Creates basket containing info about the item and how many of it selected
    *
    * @param selectedItems from command line (user input)
    * @return
    */
  def createBasket(selectedItems: Array[String]): Map[Item, Int]
}
