package com.gildedrose

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GildedroseTests extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  val conjuredNameGen: Gen[String] = arbitrary[String].map(name => s"Conjured $name")
  val qualityGen: Gen[Int]         = Gen.chooseNum(0, 50)

  test("Non-expired conjured items should degrade at the rate of 2 every day") {
    forAll(conjuredNameGen -> "name", qualityGen -> "quality") { (name, quality) =>
      val item = new Item(name, 10, quality)

      new GildedRose(Array(item)).updateQuality()

      item.quality should be(math.max(0, quality - 2))
    }
  }

  test("Expired conjured items should degrade at the rate of 4 every day") {
    forAll(conjuredNameGen -> "name", qualityGen -> "quality") { (name, quality) =>
      val item = new Item(name, 0, quality)

      new GildedRose(Array(item)).updateQuality()

      item.quality should be(math.max(0, quality - 4))
    }
  }

}
