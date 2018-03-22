package dean.tools.json.transform.job

import dean.tools.json.transform.db.NodeTypeWithParam
import dean.tools.json.transform.json.JsonPluginComponent

trait JobComponent[R <: NodeTypeWithParam, U] {
  this: JsonPluginComponent =>

  val json: JsonPlugin[_]

  def work(workflow: R): (R, String) = {
    val newJson = json.transform(workflow.param)
    (workflow, newJson)
  }

}
