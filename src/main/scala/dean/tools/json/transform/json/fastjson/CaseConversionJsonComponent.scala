package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

trait CaseConversionJsonComponent extends JsonPluginComponent {

  class CaseConversionJson extends JsonPlugin[Option[JSONArray]] {
    override def fromOld(json: JSONObject): Option[JSONArray] = {
      if (json.containsKey("column")) Some(json.getJSONArray("column")) else None
    }

    override def toNew(oldObj: Option[JSONArray]): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
      }
      rootJson.add(newJson)
      rootJson.toJSONString
    }

  }

}

