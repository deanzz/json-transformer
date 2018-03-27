package dean.tools.json.transform.dao.mongo.param

import dean.tools.json.transform.dao.NodeTypeWithParam
import dean.tools.json.transform.enums.NodeType
import org.mongodb.scala.bson.ObjectId



case class MiniGraph(key: String,
                     nodeType: Option[NodeType],
                     param: Option[String])

case class IntegrationConfig(_id: ObjectId,
                             miniGraph: Seq[MiniGraph])

case class IntegrationConfigParam(_id: ObjectId, key: String, nodeType: NodeType, param: String) extends NodeTypeWithParam
