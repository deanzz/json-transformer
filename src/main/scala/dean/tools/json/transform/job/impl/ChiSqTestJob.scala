package dean.tools.json.transform.job.impl

import org.mongodb.scala.BulkWriteResult
import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.{MongoDBComponent, MongoWorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.ChiSqTestJsonComponent

class ChiSqTestJob extends JobComponent[MongoWorkFlowParam, BulkWriteResult]
  with ChiSqTestJsonComponent {

  override val json  = new ChiSqTestJson
}
