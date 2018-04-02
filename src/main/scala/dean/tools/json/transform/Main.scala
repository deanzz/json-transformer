package dean.tools.json.transform

import dean.tools.json.transform.launch.impl.{IntegrationConfigLauncher, IntegrationConfigVersionLauncher, WorkflowLauncher}

object Main {
  def main(args: Array[String]): Unit ={
    val integrationConfigLauncher = new IntegrationConfigLauncher
    integrationConfigLauncher.launch()
    val integrationConfigVersionLauncher = new IntegrationConfigVersionLauncher
    integrationConfigVersionLauncher.launch()
    val workflowLauncher = new WorkflowLauncher
    workflowLauncher.launch()
  }
}
