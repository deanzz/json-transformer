package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

trait SimilarityJsonComponent extends JsonPluginComponent{
  class SimilarityJson extends JsonPlugin[OldSimilarityParam]{
    override def fromOld(json: JSONObject): OldSimilarityParam = {
      val leftId = if(json.containsKey("leftId")) Some(json.getString("leftId")) else None
      val rightId = if(json.containsKey("rightId")) Some(json.getString("rightId")) else None
      val analysisColumns = if (json.containsKey("analysisColumns")) Some(json.getJSONArray("analysisColumns")) else None
      val similarityType = if(json.containsKey("similarityType")) Some(json.getString("similarityType")) else None
      val outputNum = if (json.containsKey("outputNum")) Some(json.getIntValue("outputNum")) else None
      val isContainType = if (json.containsKey("isContainType")) Some(json.getBooleanValue("isContainType")) else None

      OldSimilarityParam(leftId, rightId, analysisColumns, similarityType, outputNum, isContainType)
    }

    override def toNew(oldObj: OldSimilarityParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.analysisColumns.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.leftId.map(v => newJson.put("leftId", v))
      oldObj.rightId.map(v => newJson.put("rightId", v))
      oldObj.similarityType.map(v => newJson.put("similarityType", v))
      oldObj.outputNum.map(v => newJson.put("outputNum", v))
      oldObj.isContainType.map(v => newJson.put("isContainType", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }

  case class OldSimilarityParam(leftId: Option[String],
                           rightId: Option[String],
                           analysisColumns: Option[JSONArray],
                           similarityType: Option[String],
                           outputNum: Option[Int],
                           isContainType: Option[Boolean])
}