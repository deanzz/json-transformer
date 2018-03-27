package dean.tools.json.transform.launch.impl

import dean.tools.json.transform.dao.mongo.MongoIntegrationConfigDaoComponent
import dean.tools.json.transform.dao.mongo.param.IntegrationConfigParam
import dean.tools.json.transform.launch.LaunchComponent
import dean.tools.json.transform.log.impl.Log4jLoggerComponent
import org.mongodb.scala.BulkWriteResult

import scala.concurrent.Await
import scala.concurrent.duration._

class IntegrationConfigVersionLauncher extends LaunchComponent[IntegrationConfigParam, BulkWriteResult]
  with MongoIntegrationConfigDaoComponent with Log4jLoggerComponent {
  override val dao = new IntegrationConfigDao(Some("integrationConfigVersion"))
  override val logger = new Log4jLogger(this.getClass)

  override def workResultHandler(seq: Seq[(IntegrationConfigParam, String)]): Unit = {
    val start = System.currentTimeMillis()
    val future = dao.bulkUpdateParam(seq)
    val result = Await.result(future, 300 second)
    val finished = System.currentTimeMillis()
    logger.info(s"IntegrationConfigVersion: Elapsed time = ${finished - start}ms, MatchedCount = ${result.getMatchedCount}, ModifiedCount = ${result.getModifiedCount}, DeletedCount = ${result.getDeletedCount}, InsertedCount = ${result.getInsertedCount}")

  }
}
