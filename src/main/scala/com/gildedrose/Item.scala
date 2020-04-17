package com.gildedrose

class Item(val name: String, var sellIn: Int, var quality: Int) {
  override def toString = s"Item($name, $sellIn, $quality)"
}

object Item {
  def unapply(item: Item): Some[(String, Int, Int)] = Some((item.name, item.sellIn, item.quality))

  object AgedBrie {
    def unapply(item: Item): Option[(Int, Int)] =
      if(item.name == GildedRose.AgedBrie) Some((item.sellIn, item.quality))
      else None
  }

  object BackstagePass {
    def unapply(item: Item): Option[(Int, Int)] =
      if(item.name == GildedRose.BackstagePass) Some((item.sellIn, item.quality))
      else None
  }
}
