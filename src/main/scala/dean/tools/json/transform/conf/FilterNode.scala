package dean.tools.json.transform.conf

import com.typesafe.config.Config
import dean.tools.json.transform.NodeType

//case class FilterNode(nodeType: NodeType, className: String)

object FilterNode{
  def apply(conf: Config): (NodeType, String) ={
    val nodeType = NodeType.valueOf(conf.getString("nodeType"))
    val className = conf.getString("class")
    (nodeType, className)
  }
}
