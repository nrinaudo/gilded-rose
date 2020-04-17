package com.gildedrose

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class RegressionTests extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {
  test("The legacy implementation and the modernized version should be equivalent") {

    val nameGen = Gen
      .oneOf(
        Gen.const(Item.AgedBrie.name),
        Gen.const(Item.BackstagePass.name),
        Gen.const(Item.Sulfuras.name),
        arbitrary[String]
      )
      .suchThat(!_.startsWith("Conjured"))

    // Generates a random int around the specified boundary.
    def genBoundary(boundary: Int): Gen[Int] =
      Gen.oneOf(boundary - 1, boundary, boundary + 1)

    val sellInGen: Gen[Int] = Gen.frequency(
      1 -> genBoundary(0),
      1 -> genBoundary(6),
      1 -> genBoundary(11),
      3 -> arbitrary[Int]
    )

    val qualityGen: Gen[Int] = Gen.frequency(
      1 -> genBoundary(0),
      1 -> genBoundary(50),
      1 -> genBoundary(80),
      3 -> arbitrary[Int]
    )

    forAll(nameGen -> "name", sellInGen -> "sellIn", qualityGen -> "quality") { (name, sellIn, quality) =>
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
