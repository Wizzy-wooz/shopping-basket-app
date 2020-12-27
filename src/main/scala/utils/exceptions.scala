import constants._

package object exceptions {

  final case class DuplicateItemsExist(private val message: String = DuplicateItemsExistMsg,
                                       private val cause: Throwable = new RuntimeException) extends Exception(message, cause)

  final case class FailedToLoadTheCatalog(private val message: String = FailedToLoadCatalogMsg,
                                          private val cause: Throwable = new RuntimeException) extends Exception(message, cause)

  final case class IncorrectlyProvidedItems(private val message: String = IllegalInputArgsMsg,
                                            private val cause: Throwable = new IllegalArgumentException) extends Exception(message, cause)

  final case class InvalidItems(private val message: String = InvalidItemsMsg,
                                            private val cause: Throwable = new IllegalArgumentException) extends Exception(message, cause)

  final case class NotProvidedItems(private val message: String = s"No selected items. $IllegalInputArgsMsg",
                                    private val cause: Throwable = new IllegalArgumentException) extends Exception(message, cause)

  final case class IncorrectlyProvidedSupplementItems(private val message: String = SupplementItemsIncorrectMsg,
                                                      private val cause: Throwable = new IllegalArgumentException) extends Exception(message, cause)
}
