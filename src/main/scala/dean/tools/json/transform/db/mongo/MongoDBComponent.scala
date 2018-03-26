package dean.tools.json.transform.db.mongo

import dean.tools.json.transform.NodeType
import dean.tools.json.transform.conf.ConfigurationFactory
import dean.tools.json.transform.db.DBPluginComponent
import org.bson.codecs.configuration.CodecRegistries.{fromCodecs, fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.{BulkWriteResult, MongoClient}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.model.Filters.{and, in, notEqual, equal}
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model.{UpdateOneModel, WriteModel}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait MongoDBComponent extends DBPluginComponent {

  class DB extends DBPlugin[MongoWorkFlowParam, BulkWriteResult] {
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

    //val log = org.slf4j.LoggerFactory.getLogger(this.getClass)

    override def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[MongoWorkFlowParam]] = {
      if (nodeTypes.isEmpty) Future(Seq.empty[MongoWorkFlowParam])
      else {
        collection.find(
          and(in("nodeType", nodeTypes: _*),
            notEqual("param", ""),
            notEqual("param", "[]"),
            notEqual("param", null),
            notEqual("param", "{}"))).projection(include("id" ,"nodeType", "param")).toFuture()
      }
    }

    override def bulkUpdateParam(workflowWithParamSeq: Seq[(MongoWorkFlowParam, String)]): Future[BulkWriteResult] = {

      /*val seq = workflowWithParamSeq.filterNot(_._2.contains("specifiedColumns")).map{
        case (w, s) =>
          s"${w.nodeType} ${w._id}: $s"
      }.mkString("\n")
      log.info(s"workflowWithParamSeq:\n$seq")*/

      val writes: Seq[WriteModel[_ <: MongoWorkFlowParam]] = workflowWithParamSeq.map{
        case (w, newJson) =>
          UpdateOneModel(equal("_id", w._id), set("param", newJson))
      }

      collection.bulkWrite(writes).toFuture()
    }
  }

}


