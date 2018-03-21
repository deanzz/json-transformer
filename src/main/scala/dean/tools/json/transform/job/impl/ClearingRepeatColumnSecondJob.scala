package dean.tools.json.transform.job.impl

import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.WorkFlowParam
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.ClearingRepeatColumnSecondJsonComponent
import org.mongodb.scala.result.UpdateResult

class ClearingRepeatColumnSecondJob extends JobComponent[WorkFlowParam, UpdateResult]
  with DummyDBComponent with ClearingRepeatColumnSecondJsonComponent {
  override val db = new MongoDB
  override val json  = new ClearingRepeatColumnSecondJson
}
