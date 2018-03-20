package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent


trait ColumnDeleteJsonComponent extends JsonPluginComponent{
  class ColumnDeleteJson extends JsonPlugin[OldColumnDeleteParam]{
    override def fromOld(json: JSONObject): OldColumnDeleteParam = {
      val `type` = if(json.containsKey("type")) Some(json.getString("type")) else None
      val column = if(json.containsKey("column")) Some(json.getJSONArray("column")) else None

      OldColumnDeleteParam(`type`, column)
    }

    override def toNew(oldObj: OldColumnDeleteParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.column.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.`type`.map(v => newJson.put("type", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }
  case class OldColumnDeleteParam(`type`: Option[String],
                                  column: Option[JSONArray])
}


