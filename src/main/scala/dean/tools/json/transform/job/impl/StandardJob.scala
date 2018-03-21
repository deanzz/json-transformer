package dean.tools.json.transform.job.impl

import com.mongodb.client.result.UpdateResult
import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.WorkFlowParam
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.StandardJsonComponent

class StandardJob extends JobComponent[WorkFlowParam, UpdateResult]
  with DummyDBComponent with StandardJsonComponent {
  override val db = new MongoDB
  override val json  = new StandardJson
}
