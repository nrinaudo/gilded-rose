package com.gildedrose

@deprecated("this class will be removed use com.gildedrose.fp.Item instead", "1.1")
class Item(val name: String, var sellIn: Int, var quality: Int) {
  override def toString = s"Item($name, $sellIn, $quality)"
}
