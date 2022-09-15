package li.flxkbr.gen

import li.flxkbr.schema.generator.{ColumnName, SchemaName}
import li.flxkbr.model.Row
import scala.collection.immutable.HashMap
import li.flxkbr.schema.generator.ColumnPath
import scala.util.Random

class ReferenceCache {

  def add[A](name: SchemaName, columnNames: IndexedSeq[ColumnName], data: IndexedSeq[Row]) = {

    if (data.size > 10_000) {
      println(s"Warning: Caching $name data with more than 10'000 entires. Performance may degrade")
    }

    val cd = CachedData(
      name,
      columnNames.zipWithIndex.toMap,
      data,
    )

    cache = cache + (name -> cd)
  }

  def pickRandomColumnValue[A](path: ColumnPath) = {
    val CachedData(_, cols, data) = cache(path.schema)
    val index = cols(path.column)
    data(Random.nextInt(data.size)).at[A](index)
  }

  def drop(name: SchemaName): Unit = {
    cache = cache - name
  }

  private var cache = HashMap.empty[SchemaName, CachedData]

  private case class CachedData(name: SchemaName, cols: Map[ColumnName, Int], data: IndexedSeq[Row])
}
