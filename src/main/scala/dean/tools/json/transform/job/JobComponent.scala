package dean.tools.json.transform.job

import org.mongodb.scala.result.UpdateResult
import dean.tools.json.transform.db.DBPluginComponent
import dean.tools.json.transform.db.mongo.WorkFlowParam
import dean.tools.json.transform.json.JsonPluginComponent
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait JobComponent[R <: WorkFlowParam, U] {
  this: DBPluginComponent with JsonPluginComponent =>

  val db: DBPlugin[R, U]
  val json: JsonPlugin[_]

  def work(workflow: R): Future[U] = {
    val newJson = json.transform(workflow.param)
    //db.updateParam(workflow, newJson)


    println(s"${workflow.nodeType}:\noldJson:\n ${workflow.param}\nnewJson:\n$newJson")
    val u: U = com.mongodb.client.result.UpdateResult.unacknowledged().asInstanceOf[U]
    Future(u)
  }

}
