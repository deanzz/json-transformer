package dean.tools.json.transform.dao

import dean.tools.json.transform.enums.NodeType

trait NodeTypeWithParam{
  def nodeType: NodeType
  def param: String
}
