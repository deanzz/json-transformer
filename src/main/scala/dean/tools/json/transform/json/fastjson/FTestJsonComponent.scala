package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

import scala.util.Try

trait FTestJsonComponent extends JsonPluginComponent{
  class FTestJson extends JsonPlugin[OldFTestParam]{
    override def fromOld(json: JSONObject): OldFTestParam = {
      val selectedBase = if(json.containsKey("selectedBase")) Some(json.getString("selectedBase")) else None
      val selectedBottom = if (json.containsKey("selectedBottom")) Some(json.getJSONArray("selectedBottom")) else None
      val meanValue = if (json.containsKey("meanValue")) {
        val v = Try(json.getJSONObject("meanValue").toString).getOrElse(json.getString("meanValue"))
        Some(v)
      } else None

      val samples = if(json.containsKey("samples")) Some(json.getString("samples")) else None
      val labelColumn = if(json.containsKey("labelColumn")) Some(json.getString("labelColumn")) else None
      val labelValue = if (json.containsKey("labelValue")) Some(json.getJSONArray("labelValue")) else None
      OldFTestParam(selectedBase, selectedBottom, meanValue, samples, labelColumn, labelValue)
    }

    override def toNew(oldObj: OldFTestParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.selectedBottom.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.meanValue.map {
        v =>
          if (v.contains("eleVarId")) {
            newJson.put("meanValue", JSON.parseObject(v))
          } else newJson.put("meanValue", v)
      }

      oldObj.selectedBase.map(v => newJson.put("selectedBase", v))
      oldObj.samples.map(v => newJson.put("samples", v))
      oldObj.labelColumn.map(v => newJson.put("labelColumn", v))
      oldObj.labelValue.map(v => newJson.put("labelValue", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }

  case class OldFTestParam(selectedBase: Option[String],
                           selectedBottom: Option[JSONArray],
                           meanValue: Option[String],
                           samples: Option[String],
                           labelColumn: Option[String],
                           labelValue: Option[JSONArray])
}


