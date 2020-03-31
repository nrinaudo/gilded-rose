package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isBrie(item: Item): Boolean          = item.name.equals("Aged Brie")
  private def isBackstagePass(item: Item): Boolean = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras(item: Item): Boolean      = item.name.equals("Sulfuras, Hand of Ragnaros")

  private def increaseQuality(quality: Int, quantity: Int): Int =
    if(quality < 50) math.min(50, quantity + quality)
    else quality

  private def updateBrie(item: Item): Item = {
    item.sellIn -= 1

    if(item.sellIn < 0)
      item.quality = increaseQuality(item.quality, 2)
    else
      item.quality = increaseQuality(item.quality, 1)

    item
  }

  private def updateBackstagePass(item: Item): Item = {
    item.sellIn -= 1

    // This is necessary to preserve (bad) legacy handling of sell-in underflowing ints:
    // - before update of sell-in, it's < 6, so quality is incremented by 3
    // - after update, it's greater than 0, so its quality is not reset.
    if(item.sellIn == Int.MaxValue)
      item.quality = increaseQuality(item.quality, 3)
    else if(item.sellIn < 0)
      item.quality = 0
    else if(item.sellIn < 5)
      item.quality = increaseQuality(item.quality, 3)
    else if(item.sellIn < 10)
      item.quality = increaseQuality(item.quality, 2)
    else
      item.quality = increaseQuality(item.quality, 1)

    item
  }

  private def updateSulfuras(item: Item): Item = item

  private def updateRegularItem(item: Item): Item = {

    def decreaseQuality(quantity: Int): Unit =
      if(item.quality > 0) item.quality = math.max(0, item.quality - quantity)

    item.sellIn -= 1
    if(item.sellIn < 0)
      decreaseQuality(2)
    else
      decreaseQuality(1)

    item
  }

  private def updateItem(item: Item): Item =
    if(isBrie(item))
      updateBrie(item)
    else if(isBackstagePass(item))
      updateBackstagePass(item)
    else if(isSulfuras(item))
      updateSulfuras(item)
    else
      updateRegularItem(item)

  def updateQuality(): Unit =
    items.foreach(updateItem)

}
