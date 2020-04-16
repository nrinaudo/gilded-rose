package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isBrie(item: Item): Boolean     = item.name.equals("Aged Brie")
  private def isPass(item: Item): Boolean     = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras(item: Item): Boolean = item.name.equals("Sulfuras, Hand of Ragnaros")

  private def increaseQuality(item: Item, quantity: Int) =
    if(item.quality < 50) item.quality = math.min(item.quality + quantity, 50)

  private def decreaseQuality(item: Item) =
    if(item.quality > 0 && !isSulfuras(item)) item.quality = item.quality - 1

  private def lastMinute(sellIn: Int) = sellIn < 5 || sellIn == Int.MaxValue

  private def late(sellIn: Int)       = sellIn < 10 || sellIn == Int.MaxValue
  private def decreaseSellIn(item: Item) =
    if(!isSulfuras(item)) item.sellIn = item.sellIn - 1

  def updateQuality() {
    items.foreach { item =>
      decreaseSellIn(item)

      val expired = item.sellIn < 0

      if(isBrie(item)) {
        increaseQuality(item, 1)
        if(expired)
          increaseQuality(item, 1)
      }
      else if(isPass(item)) {
        if(expired)
          item.quality = 0
        else {
          increaseQuality(item, 1)

          if(late(item.sellIn)) {
            increaseQuality(item, 1)
          }

          if(lastMinute(item.sellIn)) {
            increaseQuality(item, 1)
          }
        }
      }
      else {
        decreaseQuality(item)
        if(expired)
          decreaseQuality(item)
      }
    }
  }
}
