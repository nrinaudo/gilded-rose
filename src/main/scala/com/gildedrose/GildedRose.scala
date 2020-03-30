package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isBrie(item: Item): Boolean          = item.name.equals("Aged Brie")
  private def isBackstagePass(item: Item): Boolean = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras(item: Item): Boolean      = item.name.equals("Sulfuras, Hand of Ragnaros")

  private def decreaseQuality(item: Item): Unit =
    if(item.quality > 0 && !isSulfuras(item)) item.quality -= 1

  private def increaseQuality(item: Item, quantity: Int): Unit =
    if(item.quality < 50) item.quality = math.min(50, quantity + item.quality)

  def updateQuality() {
    items.foreach { item =>
      if(isBrie(item))
        increaseQuality(item, 1)
      else if(isBackstagePass(item)) {

        if(item.sellIn < 6)
          increaseQuality(item, 3)
        else if(item.sellIn < 11)
          increaseQuality(item, 2)
        else
          increaseQuality(item, 1)
      }
      else
        decreaseQuality(item)

      if(!isSulfuras(item)) {
        item.sellIn = item.sellIn - 1
      }

      if(item.sellIn < 0) {

        if(isBrie(item)) {
          increaseQuality(item, 1)
        }
        else if(isBackstagePass(item)) {
          item.quality = item.quality - item.quality
        }
        else {
          decreaseQuality(item)
        }

      }
    }
  }

}
