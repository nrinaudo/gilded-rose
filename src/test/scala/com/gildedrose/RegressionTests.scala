package com.gildedrose

import org.scalacheck.{Arbitrary, Gen, Shrink}
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
    2 -> arbitrary[Int]
  )

  implicit val arbItem: Arbitrary[Item] = Arbitrary {
    for {
      name    <- nameGen
      sellIn  <- sellInGen
      quality <- qualityGen
    } yield new Item(name, sellIn, quality)
  }

  implicit val shrinkItem: Shrink[Item] = {
    def copy(item: Item)(name: String = item.name, sellIn: Int = item.sellIn, quality: Int = item.quality): Item =
      new Item(name, sellIn, quality)


    Shrink { item =>
      Shrink.shrink(item.name).map(name => copy(item)(name = name)) append
      Shrink.shrink(item.sellIn).map(sellIn => copy(item)(sellIn = sellIn)) append
      Shrink.shrink(item.quality).map(quality => copy(item)(quality = quality))
    }

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
