package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

import scala.util.Try

trait IdColumnJsonComponent extends JsonPluginComponent {

  class IdColumnJson extends JsonPlugin[OldIdColumnParam] {
    var oldJson: JSONObject = null

    override def fromOld(json: JSONObject): OldIdColumnParam = {
      oldJson = json
      val invalidValue = if (json.containsKey("invalidValue")) Some(json.getBooleanValue("invalidValue")) else None
      val column = if (json.containsKey("column")) {
        Try(Some(json.getJSONArray("column"))).getOrElse(None)
      } else None

      OldIdColumnParam(invalidValue, column)
    }

    override def toNew(oldObj: OldIdColumnParam): String = {
      val rootJson = new JSONArray()
      oldObj.column.map {
        seq =>
          val newJson = new JSONObject()
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
          oldObj.invalidValue.map(v => newJson.put("invalidValue", v))
          rootJson.add(newJson)
      }.getOrElse{
        rootJson.add(oldJson)
      }
      rootJson.toJSONString
    }
  }

  case class OldIdColumnParam(invalidValue: Option[Boolean],
                              column: Option[JSONArray])

}


