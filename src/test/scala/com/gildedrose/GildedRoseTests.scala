package com.gildedrose

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GildedroseTests extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  test("Non-expired conjured items should degrade at the rate of 2 every day") {
    val item = new Item("Conjured item", 10, 2)

    new GildedRose(Array(item)).updateQuality()

    item.quality should be(0)
  }

  test("Expired conjured items should degrade at the rate of 4 every day") {
    val item = new Item("Conjured item", 0, 5)

    new GildedRose(Array(item)).updateQuality()

    item.quality should be(1)
  }

}
