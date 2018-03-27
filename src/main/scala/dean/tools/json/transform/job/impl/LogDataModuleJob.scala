package dean.tools.json.transform.job.impl

import dean.tools.json.transform.dao.mongo.param.MongoWorkFlowParam

import dean.tools.json.transform.db.mongo.MongoDBComponent
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.json.fastjson.LogDataModuleJsonComponent

class LogDataModuleJob extends JobComponent[MongoWorkFlowParam]
  with LogDataModuleJsonComponent {

  override val json  = new LogDataModuleJson
}
