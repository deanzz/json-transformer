package dean.tools.json.transform.log.impl

import dean.tools.json.transform.log.LoggerComponent

trait Log4jLoggerComponent extends LoggerComponent {

  class Log4jLogger(clazz: Class[_]) extends Logger {

    private val log = org.slf4j.LoggerFactory.getLogger(clazz)

    override def info(s: String): Unit = log.info(s)

    override def warn(s: String): Unit = log.warn(s)

    override def warn(s: String, e: Throwable): Unit = log.warn(s, e)

    override def error(s: String): Unit = log.error(s)

    override def error(s: String, e: Throwable): Unit = log.error(s, e)

    override def debug(s: String): Unit = log.debug(s)
  }

}
