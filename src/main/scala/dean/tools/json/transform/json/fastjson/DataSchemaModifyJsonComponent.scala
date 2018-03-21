package dean.tools.json.transform.json.fastjson

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import dean.tools.json.transform.json.JsonPluginComponent

import scala.util.Try


trait DataSchemaModifyJsonComponent extends JsonPluginComponent {

  class DataSchemaModifyJson extends JsonPlugin[Option[JSONArray]] {
    override def fromOld(jsonStr: String): Option[JSONArray] = {
      Try(Some(JSON.parseArray(jsonStr))).getOrElse(None)
    }

    override def toNew(oldObj: Option[JSONArray]): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.map{
        seq =>
          val selectedColumnsJson = toSelectedColumnsJson(seq)
          newJson.put("selectedColumns", selectedColumnsJson)
      }
      rootJson.add(newJson)
      rootJson.toJSONString
    }

  }

}