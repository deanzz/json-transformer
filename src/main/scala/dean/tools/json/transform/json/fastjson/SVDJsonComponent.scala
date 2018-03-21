package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

import scala.util.Try

trait SVDJsonComponent extends JsonPluginComponent {

  class SVDJson extends JsonPlugin[OldSVDParam] {
    override def fromOld(json: JSONObject): OldSVDParam = {
      val segment = if (json.containsKey("segment")) Some(json.getDoubleValue("segment")) else None
      val dimension = if (json.containsKey("dimension")) Some(json.getString("dimension")) else None
      val accumulateRate = if (json.containsKey("accumulateRate")) {
        val v = Try(json.getJSONObject("accumulateRate").toString).getOrElse(json.getString("accumulateRate"))
        Some(v)
      } else None
      val K = if (json.containsKey("K")) {
        val v = Try(json.getJSONObject("K").toString).getOrElse(json.getString("K"))
        Some(v)
      } else None

      val dimColumns = if (json.containsKey("dimColumns")) Some(json.getJSONArray("dimColumns")) else None
      val categorySplit = if (json.containsKey("categorySplit")) Some(json.getBooleanValue("categorySplit")) else None
      OldSVDParam(segment, dimension, accumulateRate, K, dimColumns, categorySplit)
    }

    override def toNew(oldObj: OldSVDParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.dimColumns.map {
        v =>
          val selectedColumnsJson = toSelectedColumnsJson(v)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.segment.map(v => newJson.put("segment", v))
      oldObj.dimension.map(v => newJson.put("dimension", v))
      oldObj.accumulateRate.map {
        v =>
          if (v.contains("eleVarId")) {
            newJson.put("accumulateRate", JSON.parseObject(v))
          } else newJson.put("accumulateRate", v.toDouble)
      }

      oldObj.K.map {
        v =>
          if (v.contains("eleVarId")) {
            newJson.put("K", JSON.parseObject(v))
          } else newJson.put("K", Try(v.toInt).getOrElse(v.toDouble))
      }

      oldObj.categorySplit.map(v => newJson.put("categorySplit", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }

  }
  case class OldSVDParam(segment: Option[Double],
                         dimension: Option[String],
                         accumulateRate: Option[String],
                         K: Option[String],
                         dimColumns: Option[JSONArray],
                         categorySplit: Option[Boolean])
}


