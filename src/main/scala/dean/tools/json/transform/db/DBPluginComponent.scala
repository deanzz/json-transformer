package dean.tools.json.transform.db

import dean.tools.json.transform.NodeType

import scala.concurrent.Future

trait DBPluginComponent {

  trait DBPlugin[R, U]{
    def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[R]]

    def updateParam(workflow: R, newParam: String): Future[U]
  }



}
