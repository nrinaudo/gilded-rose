package com.gildedrose

class GildedRose(val items: Array[Item]) {

  def isBrie(item: Item)     = item.name.equals("Aged Brie")
  def isPass(item: Item)     = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  def isSulfuras(item: Item) = item.name.equals("Sulfuras, Hand of Ragnaros")

  def increaseQuality(item: Item, quantity: Int) =
    if(item.quality < 50) item.quality = item.quality + quantity

  def decreaseQuality(item: Item) =
    if(item.quality > 0 && !isSulfuras(item))
      item.quality = item.quality - 1

  def updateItemQuality(item: Item): Unit = {
    if(isBrie(item)) {
      increaseQuality(item, 1)
    }
    else if(isPass(item)) {
      if(item.sellIn < 6) {
        increaseQuality(item, 3)
      }
      else if(item.sellIn < 11) {
        increaseQuality(item, 2)
      }
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
      else if(isPass(item)) {
        item.quality = 0
      }
      else
        decreaseQuality(item)
    }
  }

  def updateQuality(): Unit =
    items.foreach(updateItemQuality)
}
