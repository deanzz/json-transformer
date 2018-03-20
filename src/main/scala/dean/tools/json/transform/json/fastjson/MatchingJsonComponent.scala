package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent


trait MatchingJsonComponent extends JsonPluginComponent{
  class MatchingJson extends JsonPlugin[OldMatchingParam]{
    override def fromOld(json: JSONObject): OldMatchingParam = {
      val uniqueIdName = if(json.containsKey("uniqueIdName")) Some(json.getString("uniqueIdName")) else None
      val matchColumns = if(json.containsKey("matchColumns")) Some(json.getJSONArray("matchColumns")) else None
      val relationshipIdHistoryColumn = if(json.containsKey("relationshipIdHistoryColumn")) Some(json.getString("relationshipIdHistoryColumn")) else None
      val relationshipIdColumn = if(json.containsKey("relationshipIdColumn")) Some(json.getString("relationshipIdColumn")) else None

      OldMatchingParam(uniqueIdName, matchColumns, relationshipIdHistoryColumn, relationshipIdColumn)
    }

    override def toNew(oldObj: OldMatchingParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.matchColumns.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.uniqueIdName.map(v => newJson.put("uniqueIdName", v))
      oldObj.relationshipIdHistoryColumn.map(v => newJson.put("relationshipIdHistoryColumn", v))
      oldObj.relationshipIdColumn.map(v => newJson.put("relationshipIdColumn", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }

  case class OldMatchingParam(uniqueIdName: Option[String],
                              matchColumns: Option[JSONArray],
                              relationshipIdHistoryColumn: Option[String],
                              relationshipIdColumn: Option[String])
}

