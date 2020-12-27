package model.catalog

import model.item.Item

import scala.language.reflectiveCalls

/** Catalog contains information about available items in the shop's catalog
  *
  * @param items set of items
  *
  */
case class Catalog(items: Set[Item])