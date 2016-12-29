package task.airport

import com.typesafe.config.{Config, ConfigFactory}

case class Configuration(interface: String, port: Int) {
  require(interface.nonEmpty)
  require(port >= 0 && port <= 65535)
}

object Configuration {

  def apply(config: Config): Configuration =
    Configuration(
      interface = config getString "interface",
      port = config getInt "port"
    )

  def load(path: String): Configuration =
    apply(ConfigFactory.load() getConfig path)

}
