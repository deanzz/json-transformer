package dean.tools.json.transform.job.impl

import com.mongodb.client.result.UpdateResult
import dean.tools.json.transform.db.mongo.{MongoDBComponent, WorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.ColumnEncryptJsonComponent

class ColumnEncryptJob extends JobComponent[WorkFlowParam, UpdateResult]
  with MongoDBComponent with ColumnEncryptJsonComponent {
  override val db = new MongoDB
  override val json  = new ColumnEncryptJson
}
