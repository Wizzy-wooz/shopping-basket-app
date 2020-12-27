import constants.{CatalogContainsMalformedItems, FailedToLoadCatalog, IllegalInputArgsMessage, SupplementItemsIncorrectMessage}

package object exceptions {

  final case class CatalogValidationFailed(private val message: String = CatalogContainsMalformedItems,
                                           private val cause: Throwable = new RuntimeException) extends Exception(message, cause)

  final case class FailedToLoadTheCatalog(private val message: String = FailedToLoadCatalog,
                                          private val cause: Throwable = new RuntimeException) extends Exception(message, cause)

  final case class IncorrectlyProvidedItems(private val message: String = IllegalInputArgsMessage,
                                            private val cause: Throwable = new IllegalArgumentException) extends Exception(message, cause)

  final case class NotProvidedItems(private val message: String = s"No selected items. $IllegalInputArgsMessage",
                                    private val cause: Throwable = new IllegalArgumentException) extends Exception(message, cause)

  final case class IncorrectlyProvidedSupplementItems(private val message: String = SupplementItemsIncorrectMessage,
                                                      private val cause: Throwable = new IllegalArgumentException) extends Exception(message, cause)
}
