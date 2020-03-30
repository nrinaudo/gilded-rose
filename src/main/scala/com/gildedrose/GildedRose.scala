package com.gildedrose

class GildedRose(val items: Array[Item]) {

  private def isBrie(item: Item): Boolean          = item.name.equals("Aged Brie")
  private def isBackstagePass(item: Item): Boolean = item.name.equals("Backstage passes to a TAFKAL80ETC concert")
  private def isSulfuras(item: Item): Boolean      = item.name.equals("Sulfuras, Hand of Ragnaros")

  def updateQuality() {
    items.foreach { item =>
      if(!isBrie(item)
         && !isBackstagePass(item)) {
        if(item.quality > 0) {
          if(!isSulfuras(item)) {
            item.quality = item.quality - 1
          }
        }
      }
      else {
        if(item.quality < 50) {
          item.quality = item.quality + 1

          if(isBackstagePass(item)) {
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
          if(!isBackstagePass(item)) {
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

}
