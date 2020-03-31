package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isBrie(item: fp.Item): Boolean          = item.name.equals("Aged Brie")
  private def isBackstagePass(item: fp.Item): Boolean = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras(item: fp.Item): Boolean      = item.name.equals("Sulfuras, Hand of Ragnaros")

  private def increaseQuality(quality: Int, quantity: Int): Int =
    if(quality < 50) math.min(50, quantity + quality)
    else quality

  private def updateBrie(item: fp.Item): fp.Item = {
    val sellIn   = item.sellIn - 1
    val quantity = if(sellIn < 0) 2 else 1

    item.copy(
      sellIn = sellIn,
      quality = increaseQuality(item.quality, quantity)
    )
  }

  private def updateBackstagePass(item: fp.Item): fp.Item = {
    val sellIn = item.sellIn - 1

    if(sellIn < 0)
      item.copy(
        sellIn = sellIn,
        quality = 0
      )
    else {
      // This is necessary to preserve (bad) legacy handling of sell-in underflowing ints:
      // - before update of sell-in, it's < 6, so quality is incremented by 3
      // - after update, it's greater than 0, so its quality is not reset.
      val quantity =
        if(sellIn == Int.MaxValue || sellIn < 5) 3
        else if(sellIn < 10) 2
        else 1

      item.copy(
        sellIn = sellIn,
        quality = increaseQuality(item.quality, quantity)
      )
    }
  }

  private def updateSulfuras(item: fp.Item): fp.Item = item

  private def updateRegularItem(item: fp.Item): fp.Item = {

    def decreaseQuality(quantity: Int): Int =
      if(item.quality > 0) math.max(0, item.quality - quantity)
      else item.quality

    val sellIn   = item.sellIn - 1
    val quantity = if(sellIn < 0) 2 else 1

    item.copy(
      sellIn = sellIn,
      quality = decreaseQuality(quantity)
    )
  }

  private def updateItem(item: fp.Item): fp.Item =
    if(isBrie(item))
      updateBrie(item)
    else if(isBackstagePass(item))
      updateBackstagePass(item)
    else if(isSulfuras(item))
      updateSulfuras(item)
    else
      updateRegularItem(item)

  def updateQuality(): Unit =
    items.foreach { item =>
      val updated = updateItem(fp.Item(item.name, item.sellIn, item.quality))

      item.sellIn = updated.sellIn
      item.quality = updated.quality
    }
}
