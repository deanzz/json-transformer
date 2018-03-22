package dean.tools.json.transform.json

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}

import scala.util.Try


trait JsonPluginComponent {

  trait JsonPlugin[O] {
    def fromOld(jsonStr: String): O = {
      //println(s"jsonStr: $jsonStr")
      val json = Try(JSON.parseArray(jsonStr).getJSONObject(0)).getOrElse(JSON.parseObject(jsonStr))
      fromOld(json)
    }

    def fromOld(json: JSONObject): O = throw new UnsupportedOperationException

    def toNew(oldObj: O): String

    def transform(json: String): String = {
      toNew(fromOld(json))
    }

    protected def toSelectedColumnsJson(columns: JSONArray): JSONObject = {
      val json = new JSONObject()
      json.put("isSpecified", true)
      json.put("specifiedColumns", columns)
      json
    }
  }
}


