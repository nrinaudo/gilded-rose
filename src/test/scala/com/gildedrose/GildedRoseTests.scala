package com.gildedrose

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GildedroseTests extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  val conjuredNameGen: Gen[String] = arbitrary[String].map(name => s"Conjured $name")

  test("Non-expired conjured items should degrade at the rate of 2 every day") {
    forAll(conjuredNameGen -> "name") { name =>
      val item = new Item(name, 10, 2)

      new GildedRose(Array(item)).updateQuality()

      item.quality should be(0)
    }
  }

  test("Expired conjured items should degrade at the rate of 4 every day") {
    forAll(conjuredNameGen -> "name") { name =>
      val item = new Item(name, 0, 5)

      new GildedRose(Array(item)).updateQuality()

      item.quality should be(1)
    }
  }

}
