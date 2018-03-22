package dean.tools.json.transform

import dean.tools.json.transform.launch.impl.MongoLauncher

object Main {
  def main(args: Array[String]): Unit ={
    val launcher = new MongoLauncher
    launcher.launch()
  }
}
