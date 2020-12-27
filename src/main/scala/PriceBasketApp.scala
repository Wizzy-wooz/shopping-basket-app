
import com.typesafe.config.ConfigFactory
import service.basket.BasketServiceCommandLineInput
import service.cashier.CashierService._
import service.catalog.CatalogReaderJson
import service.receipt.ReceiptWriterConsole._
import exceptions._
import validation.ItemValidator

object PriceBasketApp extends App{
  val currentCatalog = CatalogReaderJson.getCurrentCatalogFromPath(s"/${ConfigFactory.load().getString("catalogFilePath")}")

  currentCatalog
    .validate
    .items.map(item => ItemValidator.validateItem(item.name, item.price, item.specialOffers))
    .map(result => result.fold(errors => new Exception(errors.toList.mkString(" ")), result => result))
    .foreach{
      case value:java.lang.Exception =>
        println(value)
        throw new CatalogValidationFailed
      case value => value
    }

  val basket = new BasketServiceCommandLineInput(currentCatalog).createBasket(args)
  val receipt = checkOut(basket)
  writeReceipt(receipt)
}