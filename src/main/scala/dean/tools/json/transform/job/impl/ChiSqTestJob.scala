package dean.tools.json.transform.job.impl

import com.mongodb.client.result.UpdateResult
import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.{MongoDBComponent, WorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.ChiSqTestJsonComponent

class ChiSqTestJob extends JobComponent[WorkFlowParam, UpdateResult]
  with DummyDBComponent with ChiSqTestJsonComponent {
  override val db = new MongoDB
  override val json  = new ChiSqTestJson
}
