package com.gildedrose

class GildedRose(val items: Array[Item]) {

  def isBrie(item: Item)     = item.name.equals("Aged Brie")
  def isPass(item: Item)     = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  def isSulfuras(item: Item) = item.name.equals("Sulfuras, Hand of Ragnaros")

  def increaseQuality(item: Item, quantity: Int) =
    if(item.quality < 50) item.quality = math.min(50, item.quality + quantity)

  def decreaseQuality(item: Item) =
    if(item.quality > 0 && !isSulfuras(item))
      item.quality = item.quality - 1

  def isExpired(item: Item) = item.sellIn < 0

  def updateItemQuality(item: Item): Unit = {

    if(!isSulfuras(item)) {
      item.sellIn = item.sellIn - 1
    }

    if(isBrie(item)) {
      if(isExpired(item)) increaseQuality(item, 2)
      else increaseQuality(item, 1)
    }
    else if(isPass(item)) {
      if(isExpired(item)) {
        item.quality = 0
      }
      else if(item.sellIn < 5 || item.sellIn == Int.MaxValue) {
        increaseQuality(item, 3)
      }
      else if(item.sellIn < 10 || item.sellIn == Int.MaxValue) {
        increaseQuality(item, 2)
      }
      else
        increaseQuality(item, 1)
    }
    else {
      decreaseQuality(item)
      if(isExpired(item))
        decreaseQuality(item)
    }
  }

  def updateQuality(): Unit =
    items.foreach(updateItemQuality)
}
