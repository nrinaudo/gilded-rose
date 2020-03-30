package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isBrie(item: Item): Boolean          = item.name.equals("Aged Brie")
  private def isBackstagePass(item: Item): Boolean = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras(item: Item): Boolean      = item.name.equals("Sulfuras, Hand of Ragnaros")

  private def decreaseQuality(item: Item, quantity: Int): Unit =
    if(!isSulfuras(item) && item.quality > 0) item.quality = math.max(0, item.quality - quantity)

  private def increaseQuality(item: Item, quantity: Int): Unit =
    if(item.quality < 50) item.quality = math.min(50, quantity + item.quality)

  def updateQuality() {
    items.foreach { item =>
      if(isBrie(item)) {
        if(item.sellIn - 1 < 0)
          increaseQuality(item, 2)
        else
          increaseQuality(item, 1)
      }
      else if(isBackstagePass(item)) {
        if(item.sellIn - 1 < 0)
          item.quality = 0
        else if(item.sellIn < 6)
          increaseQuality(item, 3)
        else if(item.sellIn < 11)
          increaseQuality(item, 2)
        else
          increaseQuality(item, 1)
      }
      else {
        if(item.sellIn - 1 < 0)
          decreaseQuality(item, 2)
        else
          decreaseQuality(item, 1)
      }

      if(!isSulfuras(item)) {
        item.sellIn = item.sellIn - 1
      }
    }
  }

}
