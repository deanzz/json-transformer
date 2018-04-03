package dean.tools.json.transform.conf

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

object ConfigurationFactory {

  private var confOpt: Option[Config] = None

  def get: Config = {
    confOpt.getOrElse {
      val conf = Try {
        val path = System.getProperty("config.file")
        println(s"config.file = $path")
        ConfigFactory.parseFile(new File(path))
      }.getOrElse(ConfigFactory.load("application.conf"))
      confOpt = Some(conf)
      conf
    }
  }
}
