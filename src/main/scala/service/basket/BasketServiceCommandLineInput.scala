package service.basket

import exceptions.{IncorrectlyProvidedItems, NotProvidedItems}
import model.catalog.Catalog
import model.item.Item

/**
  * Service responsible for all operations with basket: create, add, update, remove, etc
  *
  */
class BasketServiceCommandLineInput(catalog: Catalog) extends BasketService{
  /**
    * Creates basket containing info about the item and how many of it selected
    *
    * @param args from command line (user input)
    * @return
    */
  override def createBasket(args: Array[String]): Map[Item, Int] = {
    if (args.length == 0) throw new NotProvidedItems
    val itemsToBuy = args.map(_.toLowerCase.capitalize)
    itemsToBuy.foreach(item => if(!item.matches(constants.RegexOnlyLetters)) throw new IncorrectlyProvidedItems)

    val basket: Map[Item, Int] =
      itemsToBuy.groupBy(identity)
        .mapValues(_.length)
        .map{
          case (key, value) =>
            catalog.items.find(item => item.name equalsIgnoreCase key) match {
              case Some(item) => (item, value)
              case None => throw new IllegalArgumentException(s"Item with name $key does not exist in Catalog.")
            }
        }
    basket
  }
}
