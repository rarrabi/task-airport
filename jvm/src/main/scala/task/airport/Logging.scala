package task.airport

import org.slf4j.{LoggerFactory, Logger => JLogger}

import scala.reflect.ClassTag

object Logging {

  type Logger = JLogger

  def initialize(): Unit = {
    sys.props += "org.slf4j.simpleLogger.logFile" -> "System.out"
    sys.props += "org.slf4j.simpleLogger.defaultLogLevel" -> "DEBUG"
  }

  def logger[T: ClassTag]: Logger =
    logger(implicitly[ClassTag[T]].runtimeClass)

  def logger(clazz: Class[_]): Logger =
    LoggerFactory getLogger clazz

}
