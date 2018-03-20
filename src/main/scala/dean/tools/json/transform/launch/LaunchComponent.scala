package dean.tools.json.transform.launch

import dean.tools.json.transform.NodeType
import dean.tools.json.transform.conf.{ConfigurationFactory, FilterNode}
import dean.tools.json.transform.db.DBPluginComponent
import dean.tools.json.transform.db.mongo.WorkFlowParam
import dean.tools.json.transform.job.JobComponent

import collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

trait LaunchComponent[R <: WorkFlowParam, U] {
  this: DBPluginComponent =>
  val db: DBPlugin[R, U]

  def launch: Future[Seq[(NodeType, U)]] = {
    val filterNodesConfMap: Map[NodeType, String] = ConfigurationFactory.get.getConfigList("filter.nodes").asScala.map(FilterNode.apply).toMap
    val nodeJobMap = new mutable.HashMap[NodeType, JobComponent[R, U]]
    val filterNodeTypes = filterNodesConfMap.keys.toSeq
    val filterNodes = Await.result(db.queryByNodeTypes(filterNodeTypes), 10 second)
    println(s"filterNodes.size = ${filterNodes.size}")

    val futureSeq = filterNodes.map {
      n =>
        val job = nodeJobMap.getOrElse(n.nodeType, {
          val instance = this.getClass.getClassLoader.loadClass(filterNodesConfMap(n.nodeType)).newInstance().asInstanceOf[JobComponent[R, U]]
          nodeJobMap += (n.nodeType -> instance)
          instance
        })
        val f = job.work(n)
        f.onComplete {
          case Success(_) => println(s"${n.nodeType.name()} succeed")
          case Failure(e) => println(s"${n.nodeType.name()} failed, ${e.toString}")
        }
        f.map((n.nodeType, _))
    }

    Future.sequence(futureSeq)
  }
}
