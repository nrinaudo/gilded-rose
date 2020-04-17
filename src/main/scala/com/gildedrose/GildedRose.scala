package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isSulfuras(item: Item): Boolean = item.name.equals(GildedRose.Sulfuras)

  private def increaseQuality(quality: Int, quantity: Int) =
    if(quality < 50) math.min(quality + quantity, 50)
    else quality

  private def decreaseQuality(quality: Int, quantity: Int) =
    if(quality > 0) math.max(0, quality - quantity)
    else quality

  private def lastMinute(sellIn: Int) = sellIn < 5 || sellIn == Int.MaxValue

  private def late(sellIn: Int) = sellIn < 10

  private def decreaseSellIn(item: Item) =
    if(!isSulfuras(item)) item.sellIn = item.sellIn - 1

  private def updateItem(item: Item) = {
    decreaseSellIn(item)

    val expired = item.sellIn < 0

    item.name match {
      case GildedRose.AgedBrie if expired                      => item.quality = increaseQuality(item.quality, 2)
      case GildedRose.AgedBrie                                 => item.quality = increaseQuality(item.quality, 1)
      case GildedRose.BackstagePass if expired                 => item.quality = 0
      case GildedRose.BackstagePass if lastMinute(item.sellIn) => item.quality = increaseQuality(item.quality, 3)
      case GildedRose.BackstagePass if late(item.sellIn)       => item.quality = increaseQuality(item.quality, 2)
      case GildedRose.BackstagePass                            => item.quality = increaseQuality(item.quality, 1)
      case GildedRose.Sulfuras                                 => ()
      case _ if expired                                        => item.quality = decreaseQuality(item.quality, 2)
      case _                                                   => item.quality = decreaseQuality(item.quality, 1)
    }
  }

  def updateQuality(): Unit =
    items.foreach(updateItem)
}

object GildedRose {
  val AgedBrie: String      = "Aged Brie"
  val BackstagePass: String = "Backstage passes to a TAFKAL80ETC concert"
  val Sulfuras: String      = "Sulfuras, Hand of Ragnaros"
}
