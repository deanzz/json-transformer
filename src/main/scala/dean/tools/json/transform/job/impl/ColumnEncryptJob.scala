package dean.tools.json.transform.job.impl


import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.{MongoDBComponent, MongoWorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.ColumnEncryptJsonComponent

class ColumnEncryptJob extends JobComponent[MongoWorkFlowParam]
  with ColumnEncryptJsonComponent {
  override val json  = new ColumnEncryptJson
}
