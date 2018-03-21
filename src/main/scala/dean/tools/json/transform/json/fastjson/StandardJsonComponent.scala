package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

trait StandardJsonComponent extends JsonPluginComponent{
  class StandardJson extends JsonPlugin[OldStandardParam]{
    override def fromOld(json: JSONObject): OldStandardParam = {
      val method = if(json.containsKey("method")) Some(json.getString("method")) else None
      val param = if(json.containsKey("param")) Some(json.getJSONObject("param")) else None
      val columns = if(json.containsKey("columns")) Some(json.getJSONArray("columns")) else None
      val remainOriginCol = if(json.containsKey("remainOriginCol")) Some(json.getBooleanValue("remainOriginCol")) else None

      OldStandardParam(method, param, columns, remainOriginCol)
    }

    override def toNew(oldObj: OldStandardParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.columns.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
      }

      oldObj.method.map(v => newJson.put("method", v))
      oldObj.param.map(v => newJson.put("param", v))
      oldObj.remainOriginCol.map(v => newJson.put("remainOriginCol", v))

      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }

  case class OldStandardParam(method: Option[String],
                              param: Option[JSONObject],
                              columns: Option[JSONArray],
                              remainOriginCol: Option[Boolean])

}


