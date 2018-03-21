package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent


trait LogDataModuleJsonComponent extends JsonPluginComponent{
  class LogDataModuleJson extends JsonPlugin[OldLogDataModuleParam]{
    override def fromOld(json: JSONObject): OldLogDataModuleParam = {
      val andOr = if(json.containsKey("andOr")) Some(json.getString("andOr")) else None
      val idColumn = if (json.containsKey("idColumn")) Some(json.getJSONArray("idColumn")) else None
      val typeColumn = if (json.containsKey("typeColumn")) Some(json.getJSONArray("typeColumn")) else None
      val calculateRuls = if(json.containsKey("calculateRuls")) Some(json.getJSONArray("calculateRuls")) else None
      val dataFilters = if(json.containsKey("dataFilters")) Some(json.getJSONArray("dataFilters")) else None
      val wholeCombination = if(json.containsKey("wholeCombination")) Some(json.getBooleanValue("wholeCombination")) else None
      OldLogDataModuleParam(andOr, idColumn, typeColumn, calculateRuls, dataFilters, wholeCombination)
    }

    override def toNew(oldObj: OldLogDataModuleParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.idColumn.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("idColumns", selectedColumnsJson)
      }

      oldObj.typeColumn.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("typeColumns", selectedColumnsJson)
      }

      oldObj.andOr.map(v => newJson.put("andOr", v))
      oldObj.calculateRuls.map(v => newJson.put("calculateRuls", v))
      oldObj.dataFilters.map(v => newJson.put("dataFilters", v))
      oldObj.wholeCombination.map(v => newJson.put("wholeCombination", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }

  case class OldLogDataModuleParam(andOr: Option[String],
                                   idColumn: Option[JSONArray],
                                   typeColumn: Option[JSONArray],
                                   calculateRuls: Option[JSONArray],
                                   dataFilters: Option[JSONArray],
                                   wholeCombination: Option[Boolean])
}


