package com.gildedrose

class GildedRose(val items: Array[Item]) {

  def isBrie(item: Item)     = item.name.equals("Aged Brie")
  def isPass(item: Item)     = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  def isSulfuras(item: Item) = item.name.equals("Sulfuras, Hand of Ragnaros")

  def updateQuality(): Unit =
    items.foreach { item =>
      if(!isBrie(item)
         && !isPass(item)) {
        if(item.quality > 0) {
          if(!isSulfuras(item)) {
            item.quality = item.quality - 1
          }
        }
      }
      else {
        if(item.quality < 50) {
          item.quality = item.quality + 1

          if(isPass(item)) {
            if(item.sellIn < 11) {
              if(item.quality < 50) {
                item.quality = item.quality + 1
              }
            }

            if(item.sellIn < 6) {
              if(item.quality < 50) {
                item.quality = item.quality + 1
              }
            }
          }
        }
      }

      if(!isSulfuras(item)) {
        item.sellIn = item.sellIn - 1
      }

      if(item.sellIn < 0) {
        if(!isBrie(item)) {
          if(!isPass(item)) {
            if(item.quality > 0) {
              if(!isSulfuras(item)) {
                item.quality = item.quality - 1
              }
            }
          }
          else {
            item.quality = item.quality - item.quality
          }
        }
        else {
          if(item.quality < 50) {
            item.quality = item.quality + 1
          }
        }
      }
    }
}
