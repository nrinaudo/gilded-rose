package com.gildedrose

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GildedroseTests extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  val conjuredNameGen: Gen[String] = arbitrary[String].map(name => s"Conjured $name")
  val regularNameGen: Gen[String] = arbitrary[String].suchThat {
    case Item.AgedBrie.name | Item.BackstagePass.name | Item.Sulfuras.name => false
    case name                                                              => !name.startsWith("Conjured")
  }
  val qualityGen: Gen[Int]       = Gen.chooseNum(0, 50)
  val expiredSellInGen: Gen[Int] = Gen.oneOf(Gen.negNum[Int], Gen.const(0))
  val validSellInGen: Gen[Int]   = Gen.posNum[Int]

  def assertQuality(observed: Int, expected: Int) =
    if(expected < 0) observed should be(0)
    else if(expected > 50) observed should be(50)
    else observed should be(expected)

  def updated(name: String, sellIn: Int, quality: Int) = {
    val item = new Item(name, sellIn, quality)

    new GildedRose(Array(item)).updateQuality()

    item.quality
  }

  test("Non-expired conjured items should degrade at the rate of 2 every day") {
    forAll(conjuredNameGen -> "name", validSellInGen -> "sellIn", qualityGen -> "quality") { (name, sellIn, quality) =>
      assertQuality(
        observed = updated(name, sellIn, quality),
        expected = quality - 2
      )
    }
  }

  test("Expired conjured items should degrade at the rate of 4 every day") {
    forAll(conjuredNameGen -> "name", expiredSellInGen -> "sellIn", qualityGen -> "quality") {
      (name, sellIn, quality) =>
        assertQuality(
          observed = updated(name, sellIn, quality),
          expected = quality - 4
        )
    }
  }

  test("Non-expired regular items should degrade at the rate of 1 every day") {
    forAll(regularNameGen -> "name", validSellInGen -> "sellIn", qualityGen -> "quality") { (name, sellIn, quality) =>
      assertQuality(
        observed = updated(name, sellIn, quality),
        expected = quality - 1
      )
    }
  }

  test("Expired regular items should degrade at the rate of 2 every day") {
    forAll(regularNameGen -> "name", expiredSellInGen -> "sellIn", qualityGen -> "quality") { (name, sellIn, quality) =>
      assertQuality(
        observed = updated(name, sellIn, quality),
        expected = quality - 2
      )
    }
  }

  test("Non-expired aged brie should increase in quality at the rate of 1 every day") {
    forAll(validSellInGen -> "sellIn", qualityGen -> "quality") { (sellIn, quality) =>
      assertQuality(
        observed = updated(Item.AgedBrie.name, sellIn, quality),
        expected = quality + 1
      )
    }
  }

  test("Expired aged brie should increase in quality at the rate of 2 every day") {
    forAll(expiredSellInGen -> "sellIn", qualityGen -> "quality") { (sellIn, quality) =>
      assertQuality(
        observed = updated(Item.AgedBrie.name, sellIn, quality),
        expected = quality + 2
      )
    }
  }

  test("Sulfuras' quality should never change") {
    forAll(arbitrary[Int] -> "sellIn") { sellIn =>
      updated(Item.Sulfuras.name, sellIn, 80) should be(80)
    }
  }

  test("Expired backstage passes should have a value of 0") {
    forAll(expiredSellInGen -> "sellIn", qualityGen -> "quality") { (sellIn, quality) =>
      assertQuality(
        observed = updated(Item.BackstagePass.name, sellIn, quality),
        expected = 0
      )
    }
  }

}
