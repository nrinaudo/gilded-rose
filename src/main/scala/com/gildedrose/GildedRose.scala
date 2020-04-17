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

  private def decreaseSellIn(item: Item) = item match {
    case Item.Sulfuras()    => ()
    case Item(_, sellIn, _) => item.sellIn = sellIn - 1
  }

  private def updateItem(item: Item) = {
    decreaseSellIn(item)

    val expired = item.sellIn < 0

    item match {
      case Item.AgedBrie(_, quality) if expired                      => item.quality = increaseQuality(quality, 2)
      case Item.AgedBrie(_, quality)                                 => item.quality = increaseQuality(quality, 1)
      case Item.BackstagePass(_, quality) if expired                 => item.quality = 0
      case Item.BackstagePass(sellIn, quality) if lastMinute(sellIn) => item.quality = increaseQuality(quality, 3)
      case Item.BackstagePass(sellIn, quality) if late(sellIn)       => item.quality = increaseQuality(quality, 2)
      case Item.BackstagePass(_, quality)                            => item.quality = increaseQuality(quality, 1)
      case Item.Sulfuras()                                           => ()
      case Item(_, _, quality) if expired                            => item.quality = decreaseQuality(quality, 2)
      case Item(_, _, quality)                                       => item.quality = decreaseQuality(quality, 1)
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
