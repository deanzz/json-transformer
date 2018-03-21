package dean.tools.json.transform.db.dummy

import dean.tools.json.transform.NodeType
import dean.tools.json.transform.conf.ConfigurationFactory
import dean.tools.json.transform.db.DBPluginComponent
import dean.tools.json.transform.db.mongo.{NodeTypeCodec, WorkFlowParam}
import org.bson.codecs.configuration.CodecRegistries.{fromCodecs, fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.model.Filters.{and, in, notEqual}
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DummyDBComponent extends DBPluginComponent {

  class MongoDB extends DBPlugin[WorkFlowParam, UpdateResult] {
    private val uri = ConfigurationFactory.get.getString("db.mongo.uri")
    private val dbName = ConfigurationFactory.get.getString("db.mongo.dbName")
    private val db = MongoClient(uri).getDatabase(dbName)
    private val collectionName = "workflow"

    import org.mongodb.scala.bson.codecs.Macros._

    private implicit val codecRegistry: CodecRegistry = fromRegistries(
      DEFAULT_CODEC_REGISTRY,
      fromCodecs(new NodeTypeCodec),
      fromProviders(classOf[WorkFlowParam])
    )

    private val collection = db.getCollection[WorkFlowParam](collectionName).withCodecRegistry(codecRegistry)


    override def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[WorkFlowParam]] = {
      if (nodeTypes.isEmpty) Future(Seq.empty[WorkFlowParam])
      else {
        collection.find(and(in("nodeType", nodeTypes: _*), notEqual("param", ""), notEqual("param", "[]"), notEqual("param", null), notEqual("param", "{}"))).projection(include("id" ,"nodeType", "param"))/*.limit(1)*/.toFuture()
      }
    }

    override def updateParam(workflow: WorkFlowParam, param: String): Future[UpdateResult] = {
      Future(com.mongodb.client.result.UpdateResult.unacknowledged())
    }

  }

}


