package task.airport

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object Client extends JSApp {

  @JSExport
  override def main(): Unit = {

    Logging.initialize()
    val log = Logging logger getClass

    log.fine("Initializing")

    log.info("Initialized: ")
    // log.fine(s"Configuration: ")

    // log.fine("Starting")

    // log.info(s"Started: ")

    // log.fine("Stopping")

    // log.info(s"Stopped: ")

  }

}
