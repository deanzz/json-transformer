package dean.tools.json.transform.dao

import dean.tools.json.transform.enums.NodeType

import scala.concurrent.Future

trait DaoComponent {
  trait AbstractDao[R, U]{
    def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[R]]
    def bulkUpdateParam(paramSeq: Seq[(R, String)]): Future[U]
  }
}