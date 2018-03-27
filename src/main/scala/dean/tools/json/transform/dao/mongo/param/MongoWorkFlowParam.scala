package dean.tools.json.transform.dao.mongo.param

import dean.tools.json.transform.dao.NodeTypeWithParam
import dean.tools.json.transform.enums.NodeType
import org.mongodb.scala.bson.ObjectId


case class MongoWorkFlowParam(_id: ObjectId,
                              nodeType: NodeType,
                              param: String) extends NodeTypeWithParam