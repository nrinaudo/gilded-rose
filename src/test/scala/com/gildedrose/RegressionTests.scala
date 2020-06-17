package com.gildedrose

import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class RegressionTests extends AnyFunSuite with ScalaCheckPropertyChecks with Matchers {

  val nameGen: Gen[String] = Gen.frequency(
    1 -> Gen.const("Aged Brie"),
    1 -> Gen.const("Backstage passes to a TAFKAL80ETC concert"),
    1 -> Gen.const("Sulfuras, Hand of Ragnaros"),
    3 -> arbitrary[String]
  )

  test("Legacy implementation should behave the same as the refactored one") {

    forAll(nameGen -> "name", arbitrary[Int] -> "sellIn", arbitrary[Int] -> "quality") { (name, sellIn, quality) =>
      val legacyItem = new Item(name, sellIn, quality)
      new LegacyGildedRose(Array(legacyItem)).updateQuality()

      val refactoredItem = new Item(name, sellIn, quality)
      new GildedRose(Array(refactoredItem)).updateQuality()

      refactoredItem.name should be(legacyItem.name)
      refactoredItem.sellIn should be(legacyItem.sellIn)
      refactoredItem.quality should be(legacyItem.quality)
    }
  }
}
