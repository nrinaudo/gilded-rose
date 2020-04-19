package com.gildedrose

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GildedroseTests extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  val conjuredNameGen: Gen[String] = arbitrary[String].map(name => s"Conjured $name")
  val qualityGen: Gen[Int]         = Gen.chooseNum(0, 50)
  val expiredSellInGen: Gen[Int]   = Gen.oneOf(Gen.negNum[Int], Gen.const(0))
  val validSellInGen: Gen[Int]     = Gen.posNum[Int]

  test("Non-expired conjured items should degrade at the rate of 2 every day") {
    forAll(conjuredNameGen -> "name", validSellInGen -> "sellIn", qualityGen -> "quality") { (name, sellIn, quality) =>
      val item = new Item(name, sellIn, quality)

      new GildedRose(Array(item)).updateQuality()

      item.quality should be(math.max(0, quality - 2))
    }
  }

  test("Expired conjured items should degrade at the rate of 4 every day") {
    forAll(conjuredNameGen -> "name", expiredSellInGen -> "sellIn", qualityGen -> "quality") {
      (name, sellIn, quality) =>
        val item = new Item(name, sellIn, quality)

        new GildedRose(Array(item)).updateQuality()

        item.quality should be(math.max(0, quality - 4))
    }
  }

}
