package dean.tools.json.transform.job.impl

import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.{MongoDBComponent, MongoWorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.ClearingRepeatColumnSecondJsonComponent


class ClearingRepeatColumnSecondJob extends JobComponent[MongoWorkFlowParam]
  with ClearingRepeatColumnSecondJsonComponent {

  override val json  = new ClearingRepeatColumnSecondJson
}
