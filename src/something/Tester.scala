package something

import java.util.{Date, Locale}
import java.text.DateFormat
import java.text.DateFormat._
import edu.arizona.biosemantics.oto.full.OTOClient

object Tester {

  def main(args: Array[String]): Unit = {
	    var client = new OTOClient("asdf")
	    val now = new Date
	    val df = getDateInstance(LONG, Locale.FRANCE)
	    println(df format now)
	    println("Hello, world!")
    
  }

}