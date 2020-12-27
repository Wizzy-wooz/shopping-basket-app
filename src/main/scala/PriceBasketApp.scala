
import com.typesafe.config.ConfigFactory
import service.basket.BasketServiceCommandLineInput
import service.cashier.CashierService._
import service.catalog.CatalogReaderJson
import service.receipt.ReceiptWriterConsole._
import service.catalog.validation.CatalogVerifier._

/**
  * App requires arguments to be provided. Check catalog.json for available item names that can be used.
  *to run application : sbt run Apples Bread Milk Soup Soup
  */
object PriceBasketApp extends App{
  val currentCatalog = CatalogReaderJson.getCurrentCatalogFromPath(s"/${ConfigFactory.load().getString("catalogFilePath")}")
  validate(currentCatalog)
  val basket = new BasketServiceCommandLineInput(currentCatalog).createBasket(args)
  val receipt = checkOut(basket)
  writeReceipt(receipt)
}