package dean.tools.json.transform.job.impl


import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.{MongoDBComponent, MongoWorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.MatchingJsonComponent

class MatchingJob extends JobComponent[MongoWorkFlowParam]
  with MatchingJsonComponent {

  override val json  = new MatchingJson
}
