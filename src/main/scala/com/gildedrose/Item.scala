package com.gildedrose

class Item(val name: String, var sellIn: Int, var quality: Int) {
  override def toString = s"Item($name, $sellIn, $quality)"
}
