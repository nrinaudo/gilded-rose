package com.gildedrose.fp

final case class Item(name: String, sellIn: Int, quality: Int) {
  private def isBrie: Boolean          = name.equals("Aged Brie")
  private def isBackstagePass: Boolean = name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras: Boolean      = name.equals("Sulfuras, Hand of Ragnaros")

  private def increaseQuality(quality: Int, quantity: Int): Int =
    if(quality < 50) math.min(50, quantity + quality)
    else quality

  private def updateBrie: Item = {
    val newSellIn = sellIn - 1
    val quantity  = if(newSellIn < 0) 2 else 1

    copy(
      sellIn = newSellIn,
      quality = increaseQuality(quality, quantity)
    )
  }

  private def updateBackstagePass: Item = {
    val newSellIn = sellIn - 1

    if(newSellIn < 0)
      copy(
        sellIn = newSellIn,
        quality = 0
      )
    else {
      // This is necessary to preserve (bad) legacy handling of sell-in underflowing ints:
      // - before update of sell-in, it's < 6, so quality is incremented by 3
      // - after update, it's greater than 0, so its quality is not reset.
      val quantity =
        if(newSellIn == Int.MaxValue || newSellIn < 5) 3
        else if(newSellIn < 10) 2
        else 1

      copy(
        sellIn = newSellIn,
        quality = increaseQuality(quality, quantity)
      )
    }
  }

  private def updateSulfuras: Item = this

  private def updateRegularItem: Item = {

    def decreaseQuality(quantity: Int): Int =
      if(quality > 0) math.max(0, quality - quantity)
      else quality

    val newSellIn = sellIn - 1
    val quantity  = if(newSellIn < 0) 2 else 1

    copy(
      sellIn = newSellIn,
      quality = decreaseQuality(quantity)
    )
  }

  def updated: Item =
    if(isBrie) updateBrie
    else if(isBackstagePass) updateBackstagePass
    else if(isSulfuras) updateSulfuras
    else updateRegularItem

}
