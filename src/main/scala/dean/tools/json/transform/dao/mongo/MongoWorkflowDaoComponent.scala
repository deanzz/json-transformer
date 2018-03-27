package dean.tools.json.transform.dao.mongo

import dean.tools.json.transform.dao.mongo.codec.NodeTypeCodec
import dean.tools.json.transform.dao.mongo.param.MongoWorkFlowParam
import dean.tools.json.transform.db.mongo.MongoDBComponent
import dean.tools.json.transform.enums.NodeType
import org.bson.codecs.configuration.CodecRegistries.{fromCodecs, fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.model.Filters.{and, equal, in, notEqual}
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model.{UpdateOneModel, WriteModel}
import org.mongodb.scala.{BulkWriteResult, MongoCollection}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait MongoWorkflowDaoComponent extends MongoDaoComponent with MongoDBComponent{

  class WorkflowDao extends MongoAbstractDao[MongoWorkFlowParam, BulkWriteResult] with AbstractDB{

    import org.mongodb.scala.bson.codecs.Macros._

    private implicit val codecRegistry: CodecRegistry = fromRegistries(
      DEFAULT_CODEC_REGISTRY,
      fromCodecs(new NodeTypeCodec),
      fromProviders(classOf[MongoWorkFlowParam])
    )
    val collectionName = "workflow"
    val collection: MongoCollection[MongoWorkFlowParam] = db.getCollection[MongoWorkFlowParam](collectionName).withCodecRegistry(codecRegistry)

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

    override def bulkUpdateParam(paramSeq: Seq[(MongoWorkFlowParam, String)]): Future[BulkWriteResult] = {

      /*val seq = workflowWithParamSeq.filterNot(_._2.contains("specifiedColumns")).map{
        case (w, s) =>
          s"${w.nodeType} ${w._id}: $s"
      }.mkString("\n")
      log.info(s"workflowWithParamSeq:\n$seq")*/

      val writes: Seq[WriteModel[_ <: MongoWorkFlowParam]] = paramSeq.map{
        case (w, newJson) =>
          UpdateOneModel(equal("_id", w._id), set("param", newJson))
      }

      collection.bulkWrite(writes).toFuture()
    }
  }

}


