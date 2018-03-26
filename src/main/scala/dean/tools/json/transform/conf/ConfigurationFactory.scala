package dean.tools.json.transform.conf

import com.typesafe.config.{Config, ConfigFactory}

object ConfigurationFactory {

  private var confOpt: Option[Config] = None

  def get: Config = {
    confOpt.getOrElse{
      val conf = ConfigFactory.load("application.conf")
      confOpt = Some(conf)
      conf
    }
  }
}
