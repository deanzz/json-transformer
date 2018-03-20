package dean.tools.json.transform.job.impl

import com.mongodb.client.result.UpdateResult
import dean.tools.json.transform.db.mongo.{MongoDBComponent, WorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.KSNormTestPerfectJsonComponent

class KSNormTestPerfectJob extends JobComponent[WorkFlowParam, UpdateResult]
  with MongoDBComponent with KSNormTestPerfectJsonComponent {
  override val db = new MongoDB
  override val json  = new KSNormTestPerfectJson
}
