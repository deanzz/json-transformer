package dean.tools.json.transform

import dean.tools.json.transform.launch.impl.MongoLauncher

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main {

  def main(args: Array[String]): Unit ={
    val launcher = new MongoLauncher
    val futures = launcher.launch

    futures.foreach{
      results =>
        results.foreach{
          case (nodeType, r) =>
            //println(s"${nodeType.name()}: matchedCount = ${r.getMatchedCount}, modifiedCount = ${r.getModifiedCount}")
        }
    }

    Await.result(futures, 60 second)
  }
}
