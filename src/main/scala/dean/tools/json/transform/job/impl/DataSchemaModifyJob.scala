package dean.tools.json.transform.job.impl

import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.{MongoDBComponent, WorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.DataSchemaModifyJsonComponent
import org.mongodb.scala.result.UpdateResult

class DataSchemaModifyJob extends JobComponent[WorkFlowParam, UpdateResult]
  with DummyDBComponent with DataSchemaModifyJsonComponent {
  override val db = new MongoDB
  override val json  = new DataSchemaModifyJson
}
