package dean.tools.json.transform.db.mongo

import dean.tools.json.transform.conf.ConfigurationFactory
import dean.tools.json.transform.db.DBPluginComponent
import org.mongodb.scala.{MongoClient, MongoDatabase}

trait MongoDBComponent extends DBPluginComponent {
  trait AbstractDB extends DBPlugin[MongoClient] {
    private val uri = ConfigurationFactory.get.getString("db.mongo.uri")
    private val dbName = ConfigurationFactory.get.getString("db.mongo.dbName")
    val client = MongoClient(uri)
    val db: MongoDatabase = client.getDatabase(dbName)
  }

}
