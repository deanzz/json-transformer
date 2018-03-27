package dean.tools.json.transform.launch.impl

import dean.tools.json.transform.dao.mongo.MongoWorkflowDaoComponent
import dean.tools.json.transform.dao.mongo.param.MongoWorkFlowParam
import dean.tools.json.transform.launch.LaunchComponent
import dean.tools.json.transform.log.impl.Log4jLoggerComponent
import org.mongodb.scala.BulkWriteResult

import scala.concurrent.Await
import scala.concurrent.duration._

class WorkflowLauncher extends LaunchComponent[MongoWorkFlowParam, BulkWriteResult]
  with MongoWorkflowDaoComponent with Log4jLoggerComponent {
  override val dao: AbstractDao[MongoWorkFlowParam, BulkWriteResult] = new WorkflowDao
  override val logger = new Log4jLogger(this.getClass)

  override def workResultHandler(seq: Seq[(MongoWorkFlowParam, String)]): Unit = {
    val start = System.currentTimeMillis()
    val future = dao.bulkUpdateParam(seq)
    val result = Await.result(future, 300 second)
    val finished = System.currentTimeMillis()
    logger.info(s"Workflow: Elapsed time = ${finished - start}ms, MatchedCount = ${result.getMatchedCount}, ModifiedCount = ${result.getModifiedCount}, DeletedCount = ${result.getDeletedCount}, InsertedCount = ${result.getInsertedCount}")
  }
}
