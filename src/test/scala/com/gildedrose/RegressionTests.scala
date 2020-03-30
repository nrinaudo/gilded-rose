package com.gildedrose

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalactic.Equality

class RegressionTests extends AnyFunSuite with Matchers {

  implicit val itemEquality: Equality[Item] = new Equality[Item] {
    override def areEqual(a: Item, b: Any) = b match {
      case b2: Item => a.name == b2.name && a.sellIn == b2.sellIn && a.quality == b2.quality
      case _        => false
    }
  }

  def cloneItems(inventory: Array[Item]): Array[Item] =
    inventory.map(item => new Item(item.name, item.sellIn, item.quality))

  test("Legacy and modern implementations should behave the same") {
    val inventory = Array(new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20))

    val legacy = new LegacyGildedRose(cloneItems(inventory))
    val modern = new GildedRose(cloneItems(inventory))

    legacy.updateQuality()
    modern.updateQuality()

    val expected = new Item("Backstage passes to a TAFKAL80ETC concert", 9, 22)

    legacy.items.head should equal(expected)
    modern.items.head should equal(expected)
  }
}
