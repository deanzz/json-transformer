package dean.tools.json.transform.db

import dean.tools.json.transform.NodeType

trait NodeTypeWithParam{
  def nodeType: NodeType
  def param: String
}
