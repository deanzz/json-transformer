package dean.tools.json.transform.db

import dean.tools.json.transform.enums.NodeType
import org.mongodb.scala.MongoCollection

import scala.concurrent.Future

trait DBPluginComponent {
  trait DBPlugin[T]/*[R, U]*/{
    val client: T
    //val collectionName: String
    //def getCollection: MongoCollection[T]
    //def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[R]]
    //def bulkUpdateParam(workflowWithParamSeq: Seq[(R, String)]): Future[U]
  }
}
