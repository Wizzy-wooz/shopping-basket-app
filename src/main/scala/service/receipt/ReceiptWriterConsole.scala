package service.receipt

import model.receipt.Receipt
import constants.Newline
import service.cashier.CashierService

import scala.math.BigDecimal.RoundingMode

object ReceiptWriterConsole extends ReceiptWriter {
  /**
    * writes to the specified path, by default None => meaning to console
    *
    * @param receipt
    * @param path
    */
  override def writeReceipt(receipt: Receipt, path: Option[String]): Unit = {
    receipt.purchases.foreach{purchase =>
      println(purchase.itemDescription + Newline
        + s"Subtotal: £${purchase.subtotal.setScale(2, RoundingMode.HALF_UP)}" + Newline
        + purchase.specialOffersDescription)
      println()}
    println(s"Total price: £${CashierService.findTotalSum(receipt).setScale(2, RoundingMode.HALF_UP)}")
  }
}