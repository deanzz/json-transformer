package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

trait ChiSqTestJsonComponent extends JsonPluginComponent{
  class ChiSqTestJson extends JsonPlugin[OldChiSqTestParam]{
    override def fromOld(json: JSONObject): OldChiSqTestParam = {
      val selectedBase = json.getString("selectedBase")
      val selectedBottom = json.getJSONArray("selectedBottom")
      OldChiSqTestParam(selectedBase, selectedBottom)
    }

    override def toNew(oldObj: OldChiSqTestParam): String = {
      val selectedColumnsJson = toSelectedColumnsJson(oldObj.selectedBottom)
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      newJson.put("selectedBase", oldObj.selectedBase)
      newJson.put("selectedColumns", selectedColumnsJson)
      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }
  case class OldChiSqTestParam(selectedBase: String, selectedBottom: JSONArray)
}

