package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isBrie(item: Item): Boolean     = item.name.equals("Aged Brie")
  private def isPass(item: Item): Boolean     = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras(item: Item): Boolean = item.name.equals("Sulfuras, Hand of Ragnaros")

  private def increaseQuality(item: Item, quantity: Int) =
    if(item.quality < 50) item.quality = math.min(item.quality + quantity, 50)

  private def decreaseQuality(item: Item, quantity: Int) =
    if(item.quality > 0 && !isSulfuras(item)) item.quality = math.max(0, item.quality - quantity)

  private def lastMinute(sellIn: Int) = sellIn < 5 || sellIn == Int.MaxValue

  private def late(sellIn: Int) = sellIn < 10

  private def decreaseSellIn(item: Item) =
    if(!isSulfuras(item)) item.sellIn = item.sellIn - 1

  def updateQuality() {
    items.foreach { item =>
      decreaseSellIn(item)

      val expired = item.sellIn < 0

      if(isBrie(item)) {
        if(expired)
          increaseQuality(item, 2)
        else
          increaseQuality(item, 1)
      }
      else if(isPass(item)) {
        if(expired)
          item.quality = 0
        else {
          if(lastMinute(item.sellIn))
            increaseQuality(item, 3)
          else if(late(item.sellIn))
            increaseQuality(item, 2)
          else
            increaseQuality(item, 1)
        }
      }
      else {
        if(expired)
          decreaseQuality(item, 2)
        else
          decreaseQuality(item, 1)
      }
    }
  }
}
