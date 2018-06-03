import java.time.Instant

import scala.language.implicitConversions

/**
  * Application layer's package object to hold utilities.
  */
package object application {

  implicit def timestampString2Long(timestampStr: String): Long = {
    Instant.parse(timestampStr).toEpochMilli
  }

  implicit def timestampLong2String(timestamp: Long): String = {
    Instant.ofEpochMilli(timestamp).toString
  }

}
