package dean.tools.json.transform

import dean.tools.json.transform.launch.impl.Launcher

object Main {
  def main(args: Array[String]): Unit ={
    val launcher = new Launcher
    launcher.launch()
  }
}
