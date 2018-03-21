package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

trait ColumnTransformRowJsonComponent extends JsonPluginComponent {

  class ColumnTransformRowJson extends JsonPlugin[OldColumnTransformRowParam] {
    override def fromOld(json: JSONObject): OldColumnTransformRowParam = {
      val notZero = if (json.containsKey("notZero")) Some(json.getBooleanValue("notZero")) else None
      val analysisColumns = if (json.containsKey("analysisColumns")) Some(json.getJSONArray("analysisColumns")) else None
      OldColumnTransformRowParam(analysisColumns, notZero)
    }

    override def toNew(oldObj: OldColumnTransformRowParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.analysisColumns.map {
        v =>
          val selectedColumnsJson = toSelectedColumnsJson(v)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.notZero.map(v => newJson.put("notZero", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }

  }
  case class OldColumnTransformRowParam(analysisColumns: Option[JSONArray],
                                        notZero: Option[Boolean])
}


