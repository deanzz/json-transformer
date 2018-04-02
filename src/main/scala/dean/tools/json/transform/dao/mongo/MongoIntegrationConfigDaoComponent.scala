package dean.tools.json.transform.dao.mongo

import com.mongodb.bulk.{BulkWriteResult, WriteRequest}
import dean.tools.json.transform.dao.mongo.codec.NodeTypeCodec
import dean.tools.json.transform.dao.mongo.param.{IntegrationConfig, IntegrationConfigParam, MiniGraph}
import dean.tools.json.transform.db.mongo.MongoDBComponent
import dean.tools.json.transform.enums.NodeType
import org.bson.codecs.configuration.CodecRegistries.{fromCodecs, fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model.{UpdateOneModel, WriteModel}
import org.mongodb.scala.{BulkWriteResult, MongoCollection}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait MongoIntegrationConfigDaoComponent extends MongoDaoComponent with MongoDBComponent {

  class IntegrationConfigDao(cName: Option[String] = None) extends MongoAbstractDao[IntegrationConfigParam, BulkWriteResult] with AbstractDB {
    val collectionName = cName.getOrElse("integrationConfig")

    import org.mongodb.scala.bson.codecs.Macros._

    implicit val codecRegistry: CodecRegistry = fromRegistries(
      DEFAULT_CODEC_REGISTRY,
      fromCodecs(new NodeTypeCodec),
      fromProviders(classOf[IntegrationConfig], classOf[MiniGraph])
    )

    val collection: MongoCollection[IntegrationConfig] = db.getCollection[IntegrationConfig](collectionName).withCodecRegistry(codecRegistry)

    //val log = org.slf4j.LoggerFactory.getLogger(this.getClass)

    override def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[IntegrationConfigParam]] = {
      if (nodeTypes.isEmpty) Future(Seq.empty[IntegrationConfigParam])
      else {
        val f = collection.find(
          and(equal("type", "CUSTOM"),
            elemMatch("miniGraph",
              and(in("nodeType", nodeTypes: _*),
                // 对于数组中对象的字段，下列条件没起作用，不知为何，有空研究一下
                notEqual("param", ""),
                not(equal("param", "[]")),
                notEqual("param", "{}"),
                notEqual("param", null),
                not(regex("param", "isSpecified")))))).projection(include("miniGraph") /*Projections.elemMatch("miniGraph")*/).toFuture().map {
          seq =>
            seq.flatMap {
              integration =>
                integration.miniGraph.filter(
                  g => g.nodeType.isDefined &&
                    nodeTypes.contains(g.nodeType.get) &&
                    g.param.isDefined &&
                    g.param.get != "[]" &&
                    g.param.get != "{}" &&
                    g.param.get != "" &&
                    g.param.get != null &&
                    !g.param.get.contains("isSpecified")
                ).map {
                  miniGraph =>
                    IntegrationConfigParam(integration._id, miniGraph.key, miniGraph.nodeType.get, miniGraph.param.get)
                }
            }
        }
        f
      }
    }

    override def bulkUpdateParam(paramSeq: Seq[(IntegrationConfigParam, String)]): Future[BulkWriteResult] = {

      /*val seq = workflowWithParamSeq.filterNot(_._2.contains("specifiedColumns")).map{
        case (w, s) =>
          s"${w.nodeType} ${w._id}: $s"
      }.mkString("\n")
      log.info(s"workflowWithParamSeq:\n$seq")*/

      val writes: Seq[WriteModel[_ <: IntegrationConfig]] = paramSeq.map {
        case (w, newJson) =>
          UpdateOneModel(
            and(equal("_id", w._id),
              elemMatch("miniGraph",
                equal("key", w.key)))
            , set("miniGraph.$.param", newJson))
      }

      if(writes.nonEmpty)
        collection.bulkWrite(writes).toFuture()
      else
        Future(BulkWriteResult.acknowledged(WriteRequest.Type.UPDATE, 0, 0, null))
    }
  }

}


