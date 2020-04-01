package com.gildedrose.fp

import Item._

sealed abstract class Item(val name: String, val sellIn: Int, val quality: Int) extends Product with Serializable {
  private def isBrie: Boolean          = name.equals(AgedBrieName)
  private def isBackstagePass: Boolean = name.equals(BackstagePassName)
  private def isSulfuras: Boolean      = name.equals(SulfurasName)

  protected def copy(sellIn: Int, quality: Int): Item

  def updated: Item
}

object Item {
  private val AgedBrieName      = "Aged Brie"
  private val BackstagePassName = "Backstage passes to a TAFKAL80ETC concert"
  private val SulfurasName      = "Sulfuras, Hand of Ragnaros"

  private def increaseQuality(quality: Int, quantity: Int): Int =
    if(quality < 50) math.min(50, quantity + quality)
    else quality

  def apply(name: String, sellIn: Int, quality: Int): Item = name match {
    case AgedBrieName      => AgedBrie(sellIn, quality)
    case BackstagePassName => BackstagePass(sellIn, quality)
    case SulfurasName      => Sulfuras(sellIn, quality)
    case _                 => Regular(name, sellIn, quality)
  }

  final case class AgedBrie(override val sellIn: Int, override val quality: Int)
      extends Item(AgedBrieName, sellIn, quality) {
    override def copy(sellIn: Int, quality: Int): AgedBrie = AgedBrie(sellIn, quality)

    override def updated: Item = {
      val newSellIn = sellIn - 1
      val quantity  = if(newSellIn < 0) 2 else 1

      copy(
        sellIn = newSellIn,
        quality = increaseQuality(quality, quantity)
      )
    }

  }

  final case class BackstagePass(override val sellIn: Int, override val quality: Int)
      extends Item(BackstagePassName, sellIn, quality) {
    override def copy(sellIn: Int, quality: Int): BackstagePass = BackstagePass(sellIn, quality)

    override def updated: Item = {
      val newSellIn = sellIn - 1

      if(newSellIn < 0)
        copy(
          sellIn = newSellIn,
          quality = 0
        )
      else {
        // This is necessary to preserve (bad) legacy handling of sell-in underflowing ints:
        // - before update of sell-in, it's < 6, so quality is incremented by 3
        // - after update, it's greater than 0, so its quality is not reset.
        val quantity =
          if(newSellIn == Int.MaxValue || newSellIn < 5) 3
          else if(newSellIn < 10) 2
          else 1

        copy(
          sellIn = newSellIn,
          quality = increaseQuality(quality, quantity)
        )
      }
    }

  }

  final case class Sulfuras(override val sellIn: Int, override val quality: Int)
      extends Item(SulfurasName, sellIn, quality) {
    override def copy(sellIn: Int, quality: Int): Sulfuras = Sulfuras(sellIn, quality)
    override def updated: Item                             = this
  }

  final case class Regular(override val name: String, override val sellIn: Int, override val quality: Int)
      extends Item(name, sellIn, quality) {
    override def copy(sellIn: Int, quality: Int): Regular = Regular(name, sellIn, quality)
    override def updated: Item = {

      def decreaseQuality(quantity: Int): Int =
        if(quality > 0) math.max(0, quality - quantity)
        else quality

      val newSellIn = sellIn - 1
      val quantity  = if(newSellIn < 0) 2 else 1

      copy(
        sellIn = newSellIn,
        quality = decreaseQuality(quantity)
      )
    }

  }

}
