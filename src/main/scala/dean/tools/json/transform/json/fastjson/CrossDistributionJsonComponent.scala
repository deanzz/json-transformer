package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

trait CrossDistributionJsonComponent extends JsonPluginComponent{
  class CrossDistributionJson extends JsonPlugin[OldCrossDistributionParam]{
    override def fromOld(json: JSONObject): OldCrossDistributionParam = {
      val cdBase = if(json.containsKey("cdBase")) Some(json.getString("cdBase")) else None
      val columns = if(json.containsKey("columns")) Some(json.getJSONArray("columns")) else None

      OldCrossDistributionParam(cdBase, columns)
    }

    override def toNew(oldObj: OldCrossDistributionParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.columns.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.cdBase.map(v => newJson.put("cdBase", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }

  case class OldCrossDistributionParam(cdBase: Option[String],
                                       columns: Option[JSONArray])

}


