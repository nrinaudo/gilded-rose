package com.gildedrose

class Item(val name: String, var sellIn: Int, var quality: Int) {
  override def toString = s"Item($name, $sellIn, $quality)"
}

object Item {
  def unapply(item: Item): Some[(String, Int, Int)] = Some((item.name, item.sellIn, item.quality))

  object Brie {
    def unapply(item: Item): Option[(Int, Int)] =
      if(item.name == "Aged Brie") Some((item.sellIn, item.quality))
      else None
  }

  object Pass {
    def unapply(item: Item): Option[(Int, Int)] =
      if(item.name == "Backstage passes to a TAFKAL80ETC concert") Some((item.sellIn, item.quality))
      else None
  }

  object Sulfuras {
    def unapply(item: Item): Option[(Int, Int)] =
      if(item.name == "Sulfuras, Hand of Ragnaros") Some((item.sellIn, item.quality))
      else None
  }
}
