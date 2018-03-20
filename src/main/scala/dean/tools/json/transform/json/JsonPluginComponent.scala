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

    protected def getObjectList(json: JSONObject, key: String): Seq[JSONObject] = {
      val jsonArr = json.getJSONArray(key)
      (0 until jsonArr.size()).map {
        i => jsonArr.getJSONObject(i)
      }
    }

    protected def getStringList(json: JSONObject, key: String): Option[Seq[String]] = {
      if(json.containsKey(key)){
        val jsonArr = json.getJSONArray(key)
        val seq = (0 until jsonArr.size()).map {
          i => jsonArr.getString(i)
        }
        Some(seq)
      } else None
    }

    /*protected def toStringSelectedColumnsJson(columns: Seq[String]): JSONObject = {
      val json = new JSONObject()
      json.put("isSpecified", true)
      val specifiedColumns = new JSONArray()
      columns.indices.foreach {
        i =>
          specifiedColumns.add(columns(i))
      }
      json.put("specifiedColumns", specifiedColumns)
      json
    }*/

    /*protected def toSelectedColumnsJson(columns: Seq[T]): JSONObject = {
      val json = new JSONObject()
      json.put("isSpecified", true)
      val specifiedColumns = new JSONArray()
      columns.indices.foreach {
        i =>
          specifiedColumns.add(toOneColumnJson(columns(i)))
      }
      json.put("specifiedColumns", specifiedColumns)
      json
    }*/

    protected def toSelectedColumnsJson(columns: JSONArray): JSONObject = {
      val json = new JSONObject()
      json.put("isSpecified", true)
      json.put("specifiedColumns", columns)
      json
    }

    /*def toOneColumnJson(obj: T): JSONObject = throw new UnsupportedOperationException

    def fromOneColumnJson(json: JSONObject): T = throw new UnsupportedOperationException*/

  }

}

case class CommonSelectedColumns(isSpecified: Boolean, specifiedColumns: Seq[String])

