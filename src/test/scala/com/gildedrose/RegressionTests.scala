package com.gildedrose

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalactic.Equality

class RegressionTests extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  implicit val itemEquality: Equality[Item] = new Equality[Item] {
    override def areEqual(a: Item, b: Any) = b match {
      case b2: Item => a.name == b2.name && a.sellIn == b2.sellIn && a.quality == b2.quality
      case _        => false
    }
  }

  val specialNameGen: Gen[String] = Gen.oneOf(
    "Aged Brie",
    "Backstage passes to a TAFKAL80ETC concert",
    "Sulfuras, Hand of Ragnaros"
  )

  val nameGen: Gen[String] = Gen.frequency(
    3 -> specialNameGen,
    1 -> arbitrary[String]
  )

  implicit val arbItem: Arbitrary[Item] = Arbitrary {
    for {
      name    <- nameGen
      sellIn  <- arbitrary[Int]
      quality <- arbitrary[Int]
    } yield new Item(name, sellIn, quality)
  }

  def cloneItems(inventory: Array[Item]): Array[Item] =
    inventory.map(item => new Item(item.name, item.sellIn, item.quality))

  test("Legacy and modern implementations should behave the same") {
    forAll { inventory: Array[Item] =>
      val legacy = new LegacyGildedRose(cloneItems(inventory))
      val modern = new GildedRose(cloneItems(inventory))

      legacy.updateQuality()
      modern.updateQuality()

      legacy.items should contain theSameElementsInOrderAs modern.items
    }
  }
}
