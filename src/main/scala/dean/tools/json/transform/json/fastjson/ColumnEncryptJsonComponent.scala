package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

trait ColumnEncryptJsonComponent extends JsonPluginComponent {

  class ColumnEncryptJson extends JsonPlugin[OldColumnEncryptParam] {
    override def fromOld(json: JSONObject): OldColumnEncryptParam = {
      val retainOldColumn = if (json.containsKey("retainOldColumn")) json.getBooleanValue("retainOldColumn") else false
      val selectedArr = if (json.containsKey("selected")) Some(json.getJSONArray("selected")) else None
      OldColumnEncryptParam(selectedArr, retainOldColumn)
    }

    override def toNew(oldObj: OldColumnEncryptParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.selected.map {
        v =>
          val selectedColumnsJson = toSelectedColumnsJson(v)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      newJson.put("retainOldColumn", oldObj.retainOldColumn)

      rootJson.add(newJson)
      rootJson.toJSONString
    }

  }
  case class OldColumnEncryptParam(selected: Option[JSONArray],
                                   retainOldColumn: Boolean = false)
}


