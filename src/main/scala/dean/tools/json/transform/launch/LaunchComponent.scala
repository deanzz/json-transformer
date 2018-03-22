package dean.tools.json.transform.launch

import dean.tools.json.transform.db.DBPluginComponent
import dean.tools.json.transform.log.LoggerComponent

trait LaunchComponent[R, U] {
  this: DBPluginComponent with LoggerComponent =>
  val db: DBPlugin[R, U]
  val logger: Logger

  def launch(): Unit
}
