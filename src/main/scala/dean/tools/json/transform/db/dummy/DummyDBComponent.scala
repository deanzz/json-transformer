package dean.tools.json.transform.db.dummy

import com.mongodb.bulk.BulkWriteResult
import dean.tools.json.transform.NodeType
import dean.tools.json.transform.conf.ConfigurationFactory
import dean.tools.json.transform.db.DBPluginComponent
import dean.tools.json.transform.db.mongo.{NodeTypeCodec, MongoWorkFlowParam}
import org.bson.codecs.configuration.CodecRegistries.{fromCodecs, fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.{BulkWriteResult, MongoClient}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.model.Filters.{and, in, notEqual}
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DummyDBComponent extends DBPluginComponent {

  class MongoDB extends DBPlugin[MongoWorkFlowParam, BulkWriteResult] {
    private val uri = ConfigurationFactory.get.getString("db.mongo.uri")
    private val dbName = ConfigurationFactory.get.getString("db.mongo.dbName")
    private val db = MongoClient(uri).getDatabase(dbName)
    private val collectionName = "workflow"

    import org.mongodb.scala.bson.codecs.Macros._

    private implicit val codecRegistry: CodecRegistry = fromRegistries(
      DEFAULT_CODEC_REGISTRY,
      fromCodecs(new NodeTypeCodec),
      fromProviders(classOf[MongoWorkFlowParam])
    )

    private val collection = db.getCollection[MongoWorkFlowParam](collectionName).withCodecRegistry(codecRegistry)


    override def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[MongoWorkFlowParam]] = {
      if (nodeTypes.isEmpty) Future(Seq.empty[MongoWorkFlowParam])
      else {
        collection.find(and(in("nodeType", nodeTypes: _*), notEqual("param", ""), notEqual("param", "[]"), notEqual("param", null), notEqual("param", "{}"))).projection(include("id" ,"nodeType", "param"))/*.limit(1)*/.toFuture()
      }
    }

    override def bulkUpdateParam(workflowWithParamSeq: Seq[(MongoWorkFlowParam, String)]): Future[BulkWriteResult] = {
      Future(BulkWriteResult.unacknowledged())
    }
  }

}


