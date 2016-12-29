package task.airport

import java.util.logging.{Logger => JLogger}

import scala.reflect.ClassTag

object Logging {

  type Logger = JLogger

  def initialize(): Unit = {
    // Nothing to do.
  }

  def logger[T: ClassTag]: Logger =
    logger(implicitly[ClassTag[T]].runtimeClass)

  def logger(clazz: Class[_]): Logger =
    JLogger getLogger clazz.getName

}
