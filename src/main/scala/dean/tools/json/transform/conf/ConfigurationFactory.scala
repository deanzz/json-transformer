package dean.tools.json.transform.conf

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

object ConfigurationFactory {

  private var confOpt: Option[Config] = None
  //private val confFile = new File("./application.conf")

  def get: Config = {
    confOpt.getOrElse{
      val conf = ConfigFactory.load("application.conf")//parseFile(confFile)
      confOpt = Some(conf)
      conf
    }
  }
}
