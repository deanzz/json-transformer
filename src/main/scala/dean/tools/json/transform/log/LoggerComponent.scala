package dean.tools.json.transform.log

trait LoggerComponent {
  trait Logger {
    def info(s: String)
    def warn(s: String)
    def warn(s: String, e: Throwable)
    def error(s: String)
    def error(s: String, e: Throwable)
    def debug(s: String)
  }
}
