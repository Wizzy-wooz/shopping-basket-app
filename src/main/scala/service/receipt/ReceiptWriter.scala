package service.receipt

import model.receipt.Receipt

/**
  * Interface that defines ways of writing receipt(s)
  *
  */
trait ReceiptWriter {
  /**
    * writes to the specified path, by default None => meaning to console
    *
    * @param receipt
    * @param path
    */
  def writeReceipt(receipt: Receipt, path: Option[String] = None)
}
