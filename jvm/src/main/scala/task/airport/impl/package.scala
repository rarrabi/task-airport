package task.airport

import akka.stream.scaladsl.Source

package object impl {

  implicit class RichSource[In, Mat](val source: Source[In, Mat]) extends AnyVal {

    def foldMapGroupBy[Key](key: In => Key): Source[Map[Key, In], Mat] =
      source
        .foldMapWith { in =>
          key(in) -> in
        }
        .named("foldMapGroupBy")

    def foldMapWith[Key, Value](transform: In => (Key, Value)): Source[Map[Key, Value], Mat] =
      source
        .fold {
          Map.empty[Key, Value]
        } { (map, in) =>
          map + transform(in)
        }
        .named("foldMapWith")

    def foldMapSeqWith[Key, Value](transform: In => (Key, Value)): Source[Map[Key, Seq[Value]], Mat] =
      source
        .fold {
          Map.empty[Key, Seq[Value]] withDefaultValue Seq.empty[Value]
        } { case (map, in) =>
          val (key, value) = transform(in)
          map + (key -> (map(key) :+ value))
        }
        .named("foldMapSeqWith")

  }

  implicit class RichSourceTuple[Key, Value, Mat](val source: Source[(Key, Value), Mat]) extends AnyVal {

    def foldMap: Source[Map[Key, Value], Mat] =
      source
        .foldMapWith(identity)
        .named("foldMap")

    def foldMapSeq: Source[Map[Key, Seq[Value]], Mat] =
      source
        .foldMapSeqWith(identity)
        .named("foldMapSeq")

  }

  implicit class RichSourceMap[Key, Value, Mat](val source: Source[Map[Key, Value], Mat]) extends AnyVal {

    def joinConcat[In](joined: Source[In, Mat])(key: In => Key): Source[(Value, In), Mat] =
      source
        .flatMapConcat { map =>
          joined.map { in =>
            map(key(in)) -> in
          }
        }
        .named("joinConcat")

  }

}
