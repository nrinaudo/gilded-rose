package com.gildedrose

import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class RegressionTests extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {
  test("The legacy implementation and the modernized version should be equivalent") {
    forAll { (name: String, sellIn: Int, quality: Int) =>
      val legacy = new Item(name, sellIn, quality)
      new LegacyGildedRose(Array(legacy)).updateQuality()

      val modern = new Item(name, sellIn, quality)
      new GildedRose(Array(modern)).updateQuality()

      legacy.name should be(modern.name)
      legacy.quality should be(modern.quality)
      legacy.sellIn should be(modern.sellIn)

    }
  }
}
