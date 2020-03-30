package com.gildedrose

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalactic.Equality

class GildedRoseTests extends AnyFunSuite with Matchers {

  test("Item references processed by GildedRose.updateQuality should be stable") {
    val inventory = Array(new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20))
    val modern    = new GildedRose(inventory)

    modern.updateQuality()

    assert(modern.items eq inventory, "The inventory array reference changed")

    inventory.zip(modern.items).foreach {
      case (original, updated) =>
        assert(original eq updated, s"Reference for item ${original} changed")
    }
  }
}
