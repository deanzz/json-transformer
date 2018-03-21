package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent


trait ClearingRepeatColumnSecondJsonComponent extends JsonPluginComponent{
  class ClearingRepeatColumnSecondJson extends JsonPlugin[OldClearingRepeatColumnSecondParam]{
    override def fromOld(json: JSONObject): OldClearingRepeatColumnSecondParam = {
      val condition = if(json.containsKey("condition")) Some(json.getJSONArray("condition")) else None
      val column = if(json.containsKey("column")) Some(json.getJSONArray("column")) else None

      OldClearingRepeatColumnSecondParam(condition, column)
    }

    override def toNew(oldObj: OldClearingRepeatColumnSecondParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.column.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.condition.map(v => newJson.put("condition", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }
  case class OldClearingRepeatColumnSecondParam(condition: Option[JSONArray],
                                  column: Option[JSONArray])
}