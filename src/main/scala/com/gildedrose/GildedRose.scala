package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isBrie(item: Item): Boolean          = item.name.equals("Aged Brie")
  private def isBackstagePass(item: Item): Boolean = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras(item: Item): Boolean      = item.name.equals("Sulfuras, Hand of Ragnaros")

  private def increaseQuality(item: Item, quantity: Int): Unit =
    if(item.quality < 50) item.quality = math.min(50, quantity + item.quality)

  private def updateBrie(item: Item): Unit = {
    item.sellIn -= 1

    if(item.sellIn < 0)
      increaseQuality(item, 2)
    else
      increaseQuality(item, 1)

  }

  private def updateBackstagePass(item: Item): Unit = {
    item.sellIn -= 1

    // This is necessary to preserve (bad) legacy handling of sell-in underflowing ints:
    // - before update of sell-in, it's < 6, so quality is incremented by 3
    // - after update, it's greater than 0, so its quality is not reset.
    if(item.sellIn == Int.MaxValue)
      increaseQuality(item, 3)
    else if(item.sellIn < 0)
      item.quality = 0
    else if(item.sellIn < 5)
      increaseQuality(item, 3)
    else if(item.sellIn < 10)
      increaseQuality(item, 2)
    else
      increaseQuality(item, 1)

  }

  private def updateSulfuras(item: Item): Unit = ()

  private def updateRegularItem(item: Item): Unit = {

    def decreaseQuality(quantity: Int): Unit =
      if(item.quality > 0) item.quality = math.max(0, item.quality - quantity)

    item.sellIn -= 1
    if(item.sellIn < 0)
      decreaseQuality(2)
    else
      decreaseQuality(1)

  }

  private def updateItem(item: Item): Unit =
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
