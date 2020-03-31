package com.gildedrose

class GildedRose(val items: Array[Item]) {

  def updateQuality(): Unit =
    items.foreach { item =>
      val updated = fp.Item(item.name, item.sellIn, item.quality).updated

      item.sellIn = updated.sellIn
      item.quality = updated.quality
    }
}
