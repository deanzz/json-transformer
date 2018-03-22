package dean.tools.json.transform.job.impl

import org.mongodb.scala.BulkWriteResult
import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.{MongoDBComponent, MongoWorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.SVDJsonComponent

class SVDJob extends JobComponent[MongoWorkFlowParam, BulkWriteResult]
  with SVDJsonComponent {

  override val json  = new SVDJson
}
