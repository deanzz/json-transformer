package dean.tools.json.transform.launch.impl

import dean.tools.json.transform.db.mongo.{MongoDBComponent, MongoWorkFlowParam}
import dean.tools.json.transform.launch.LaunchComponent
import dean.tools.json.transform.log.impl.Log4jLoggerComponent
import org.mongodb.scala.BulkWriteResult
import scala.concurrent.Await
import scala.concurrent.duration._

class Launcher extends LaunchComponent[MongoWorkFlowParam, BulkWriteResult]
  with MongoDBComponent with Log4jLoggerComponent {
  override val db: DBPlugin[MongoWorkFlowParam, BulkWriteResult] = new MongoDB
  override val logger = new Log4jLogger(this.getClass)

  override def workResultHandler(seq: Seq[(MongoWorkFlowParam, String)]): Unit = {
    val start = System.currentTimeMillis()
    val future = db.bulkUpdateParam(seq)
    val result = Await.result(future, 300 second)
    val finished = System.currentTimeMillis()
    logger.info(s"Elapsed time = ${finished - start}ms, MatchedCount = ${result.getMatchedCount}, ModifiedCount = ${result.getModifiedCount}, DeletedCount = ${result.getDeletedCount}, InsertedCount = ${result.getInsertedCount}")
  }
}
