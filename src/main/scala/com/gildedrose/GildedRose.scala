package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isBrie(item: Item): Boolean     = item.name.equals("Aged Brie")
  private def isPass(item: Item): Boolean     = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras(item: Item): Boolean = item.name.equals("Sulfuras, Hand of Ragnaros")

  private def increaseQuality(item: Item) =
    if(item.quality < 50) item.quality = item.quality + 1

  private def decreaseQuality(item: Item) =
    if(item.quality > 0 && !isSulfuras(item)) item.quality = item.quality - 1

  private def lastMinute(sellIn: Int) = sellIn < 5 || sellIn == Int.MaxValue

  private def late(sellIn: Int)       = sellIn < 10 || sellIn == Int.MaxValue

  def updateQuality() {
    items.foreach { item =>
      if(!isSulfuras(item)) {
        item.sellIn = item.sellIn - 1
      }

      if(isBrie(item) || isPass(item)) {

        increaseQuality(item)

        if(isPass(item)) {
          if(late(item.sellIn)) {
            increaseQuality(item)
          }

          if(lastMinute(item.sellIn)) {
            increaseQuality(item)
          }
        }
      }
      else
        decreaseQuality(item)

      if(item.sellIn < 0) {
        if(isBrie(item)) {
          increaseQuality(item)
        }
        else if(isPass(item)) {
          item.quality = item.quality - item.quality
        }
        else
          decreaseQuality(item)
      }
    }
  }
}
