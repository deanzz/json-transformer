package dean.tools.json.transform.launch.impl

import dean.tools.json.transform.NodeType
import dean.tools.json.transform.conf.{ConfigurationFactory, FilterNode}
import dean.tools.json.transform.db.dummy.DummyDBComponent
import dean.tools.json.transform.db.mongo.{MongoDBComponent, MongoWorkFlowParam}
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.launch.LaunchComponent
import dean.tools.json.transform.log.impl.Log4jLoggerComponent
import org.mongodb.scala.BulkWriteResult

import scala.collection.mutable
import scala.concurrent.Await
import collection.JavaConverters._
import scala.concurrent.duration._

class MongoLauncher extends LaunchComponent[MongoWorkFlowParam, BulkWriteResult]
  with MongoDBComponent with Log4jLoggerComponent {
  override val db: DBPlugin[MongoWorkFlowParam, BulkWriteResult] = new MongoDB
  override val logger = new Log4jLogger(this.getClass)

  override def launch(): Unit = {
    val filterNodesConfMap: Map[NodeType, String] = ConfigurationFactory.get.getConfigList("filter.nodes").asScala.map(FilterNode.apply).toMap
    val nodeJobMap = new mutable.HashMap[NodeType, JobComponent[MongoWorkFlowParam, BulkWriteResult]]
    val filterNodeTypes = filterNodesConfMap.keys.toSeq
    val filterNodes = Await.result(db.queryByNodeTypes(filterNodeTypes), 60 second)
    logger.info(s"QueriedCount = ${filterNodes.size}")

    val newParamSeq = filterNodes.map {
      n =>
        val job = nodeJobMap.getOrElse(n.nodeType, {
          val instance = this.getClass.getClassLoader.loadClass(filterNodesConfMap(n.nodeType)).newInstance().asInstanceOf[JobComponent[MongoWorkFlowParam, BulkWriteResult]]
          nodeJobMap += (n.nodeType -> instance)
          instance
        })

        //log.info(s"Start process ${n.nodeType} ${n._id}")
        job.work(n)
    }

    val future = db.updateManyParam(newParamSeq)
    val result = Await.result(future, 120 second)
    logger.info(s"MatchedCount = ${result.getMatchedCount}, ModifiedCount = ${result.getModifiedCount}, DeletedCount = ${result.getDeletedCount}, InsertedCount = ${result.getInsertedCount}")
    val updatedFailedIds = result.getUpserts.asScala.map {
      u => u.getId
    }.mkString(", ")
    logger.info(s"updatedFailedIds: $updatedFailedIds")
  }
}
