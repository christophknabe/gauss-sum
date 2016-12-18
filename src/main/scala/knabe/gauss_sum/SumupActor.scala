package knabe.gauss_sum

import akka.actor.{Actor, ActorLogging, Props}

object SumupActor {

  /** Props object to create an actor of this type. */
  val props = Props[SumupActor]()

  /** Returns the sum of the integer numbers including 'from' up to but excluding 'until'. */
  def sumInterval(from: Int, until: Int): Long = {
    var result: Long = 0
    for (i <- from until until) {
      result += i
    }
    result
  }

}

/** An Actor which will sum up an interval of integer numbers. It accepts messages of type ManagerActor.Interval and answers by ManagerActor.Result. */
class SumupActor extends Actor with ActorLogging {

  import SumupActor._

  def receive: Receive = {
    case interval: ManagerActor.Interval => {
      val sum = sumInterval(interval.from, interval.until)
      log.info(s"Sum[${interval.from}..${interval.until}) is $sum")
      sender ! ManagerActor.Result(sum)
    }
    case illegal => log.error(s"Illegal message $illegal")
  }

}
