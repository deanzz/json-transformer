package dean.tools.json.transform.launch

import dean.tools.json.transform.conf.{ConfigurationFactory, FilterNode}
import dean.tools.json.transform.dao.{DaoComponent, NodeTypeWithParam}
import dean.tools.json.transform.enums.NodeType
import dean.tools.json.transform.job.JobComponent
import dean.tools.json.transform.log.LoggerComponent

import scala.collection.mutable
import scala.concurrent.Await
import collection.JavaConverters._
import scala.concurrent.duration._

trait LaunchComponent[R <: NodeTypeWithParam, U] {
  this: DaoComponent with LoggerComponent =>
  val dao: AbstractDao[R, U]
  val logger: Logger

  def launch(): Unit = {
    val filterNodesConfMap: Map[NodeType, String] = ConfigurationFactory.get.getConfigList("filter.nodes").asScala.map(FilterNode.apply).toMap
    val nodeJobMap = new mutable.HashMap[NodeType, JobComponent[R]]
    val filterNodeTypes = filterNodesConfMap.keys.toSeq
    val filterNodes = Await.result(dao.queryByNodeTypes(filterNodeTypes), 60 second)
    logger.info(s"QueriedCount = ${filterNodes.size}")

    val newParamSeq = filterNodes.map {
      n =>
        val job = nodeJobMap.getOrElse(n.nodeType, {
          val instance = this.getClass.getClassLoader.loadClass(filterNodesConfMap(n.nodeType)).newInstance().asInstanceOf[JobComponent[R]]
          nodeJobMap += (n.nodeType -> instance)
          instance
        })
        job.work(n)
    }
    workResultHandler(newParamSeq)
  }

  def workResultHandler(seq: Seq[(R, String)]): Unit
}
