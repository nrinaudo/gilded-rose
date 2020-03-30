package com.gildedrose

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RegressionTests extends AnyFunSuite with Matchers {
  test("Legacy and modern implementations should behave the same") {
    val inventory = Array(new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20))

    val legacy = new LegacyGildedRose(inventory)
    val modern = new GildedRose(inventory)

    legacy.updateQuality()
    modern.updateQuality()

    legacy.items should be(modern.items)
  }
}
