package com.gildedrose

class Item(val name: String, var sellIn: Int, var quality: Int) {
  override def toString = s"Item($name, $sellIn, $quality)"
}

object Item {
  def unapply(item: Item): Some[(String, Int, Int)] = Some((item.name, item.sellIn, item.quality))

  object AgedBrie {
    val name: String = "Aged Brie"

    def unapply(item: Item): Option[(Int, Int)] =
      if(item.name == name) Some((item.sellIn, item.quality))
      else None
  }

  object BackstagePass {
    val name: String = "Backstage passes to a TAFKAL80ETC concert"

    def unapply(item: Item): Option[(Int, Int)] =
      if(item.name == name) Some((item.sellIn, item.quality))
      else None
  }

  object Sulfuras {
    val name: String = "Sulfuras, Hand of Ragnaros"

    def unapply(item: Item): Boolean = item.name == name
  }
}
