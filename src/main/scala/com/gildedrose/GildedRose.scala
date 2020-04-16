package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isSulfuras(item: Item): Boolean = item.name.equals(GildedRose.Sulfuras)

  private def increaseQuality(item: Item, quantity: Int) =
    if(item.quality < 50) item.quality = math.min(item.quality + quantity, 50)

  private def decreaseQuality(item: Item, quantity: Int) =
    if(item.quality > 0) item.quality = math.max(0, item.quality - quantity)

  private def lastMinute(sellIn: Int) = sellIn < 5 || sellIn == Int.MaxValue

  private def late(sellIn: Int) = sellIn < 10

  private def decreaseSellIn(item: Item) =
    if(!isSulfuras(item)) item.sellIn = item.sellIn - 1

  def updateQuality() {
    items.foreach { item =>
      decreaseSellIn(item)

      val expired = item.sellIn < 0

      item.name match {
        case GildedRose.AgedBrie if expired                      => increaseQuality(item, 2)
        case GildedRose.AgedBrie                                 => increaseQuality(item, 1)
        case GildedRose.BackstagePass if expired                 => item.quality = 0
        case GildedRose.BackstagePass if lastMinute(item.sellIn) => increaseQuality(item, 3)
        case GildedRose.BackstagePass if late(item.sellIn)       => increaseQuality(item, 2)
        case GildedRose.BackstagePass                            => increaseQuality(item, 1)
        case GildedRose.Sulfuras                                 => ()
        case _ if expired                                        => decreaseQuality(item, 2)
        case _                                                   => decreaseQuality(item, 1)
      }
    }
  }
}

object GildedRose {
  val AgedBrie: String      = "Aged Brie"
  val BackstagePass: String = "Backstage passes to a TAFKAL80ETC concert"
  val Sulfuras: String      = "Sulfuras, Hand of Ragnaros"
}
