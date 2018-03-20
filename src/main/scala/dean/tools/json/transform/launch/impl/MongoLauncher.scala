package dean.tools.json.transform.launch.impl

import com.mongodb.client.result.UpdateResult
import dean.tools.json.transform.db.mongo.{MongoDBComponent, WorkFlowParam}
import dean.tools.json.transform.launch.LaunchComponent

class MongoLauncher extends LaunchComponent[WorkFlowParam, UpdateResult] with MongoDBComponent{
  override val db: DBPlugin[WorkFlowParam, UpdateResult] = new MongoDB
}
