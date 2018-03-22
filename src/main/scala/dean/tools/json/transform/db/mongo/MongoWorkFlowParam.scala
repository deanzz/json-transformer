package dean.tools.json.transform.db.mongo

import dean.tools.json.transform.NodeType
import dean.tools.json.transform.db.NodeTypeWithParam
import org.mongodb.scala.bson.ObjectId


case class MongoWorkFlowParam(_id: ObjectId,
                              nodeType: NodeType,
                              param: String) extends NodeTypeWithParam