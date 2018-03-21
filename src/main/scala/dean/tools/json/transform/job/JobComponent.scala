package dean.tools.json.transform.job

import dean.tools.json.transform.db.DBPluginComponent
import dean.tools.json.transform.db.mongo.WorkFlowParam
import dean.tools.json.transform.json.JsonPluginComponent
import scala.concurrent.Future

trait JobComponent[R <: WorkFlowParam, U] {
  this: DBPluginComponent with JsonPluginComponent =>

  val db: DBPlugin[R, U]
  val json: JsonPlugin[_]
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass)

  def work(workflow: R): Future[U] = {
    val newJson = json.transform(workflow.param)
    //log.info(s"${workflow.nodeType}:\noldJson:\n ${workflow.param}\nnewJson:\n$newJson")
    log.info(s"${workflow.nodeType}: $newJson")
    db.updateParam(workflow, newJson)
  }

}
