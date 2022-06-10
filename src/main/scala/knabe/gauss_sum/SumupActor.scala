package knabe.gauss_sum

import akka.actor.{Actor, ActorLogging, Props}

object SumupActor {

  /** Props object to create an actor of this type. */
  val props = Props[SumupActor]()

  /** Returns the sum of the integer numbers including 'from' up to but excluding 'until'. */
  def sumInterval(from: Long, until: Long): Long = {
    var result: Long = 0
    var i: Long = from
    while (i < until) {
      result += computeIntensiveIdentity(i)
      i += 1
    }
    result
  }

  private def sumOf(accu: Long, n: Long): Long = {
    var result = accu
    var i = 0;
    while(i < n){
      result += 1
      i += 1
    }
    result
  }

  /** This identity function for Long values is, by purpose, very compute-intensive. */
  def computeIntensiveIdentity(n: Long): Long = {
    val square = n.toDouble * n.toDouble
    val radix = math.sqrt(square)
    math.round( radix )
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
