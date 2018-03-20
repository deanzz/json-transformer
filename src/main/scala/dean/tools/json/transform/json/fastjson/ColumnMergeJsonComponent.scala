package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

import scala.util.Try

trait ColumnMergeJsonComponent extends JsonPluginComponent {

  class ColumnMergeJson extends JsonPlugin[OldColumnMergeParam] {
    override def fromOld(json: JSONObject): OldColumnMergeParam = {
      val columnName = if (json.containsKey("columnName")) {
        val v = Try(json.getJSONObject("columnName").toString).getOrElse(json.getString("columnName"))
        Some(v)
      } else None

      val position = if (json.containsKey("position")) Some(json.getString("position")) else None
      val retainOldColumnName = if (json.containsKey("retainOldColumnName")) json.getBooleanValue("retainOldColumnName") else false
      val selectedArr = if (json.containsKey("selected")) Some(json.getJSONArray("selected")) else None
      OldColumnMergeParam(columnName, position, selectedArr, retainOldColumnName)
    }

    override def toNew(oldObj: OldColumnMergeParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.selected.map {
        v =>
          val selectedColumnsJson = toSelectedColumnsJson(v)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.columnName.map {
        v =>
          if (v.contains("eleVarId")) {
            newJson.put("columnName", JSON.parseObject(v))
          } else newJson.put("columnName", v)
      }

      oldObj.position.map(v => newJson.put("position", v))
      newJson.put("retainOldColumnName", oldObj.retainOldColumnName)

      rootJson.add(newJson)
      rootJson.toJSONString
    }

  }
  case class OldColumnMergeParam(columnName: Option[String],
                               position: Option[String],
                               selected: Option[JSONArray],
                               retainOldColumnName: Boolean = false)
}


