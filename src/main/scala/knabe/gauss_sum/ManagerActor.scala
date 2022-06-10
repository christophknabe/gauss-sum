package knabe.gauss_sum

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/** The message types an actor can understand are usually defined in his companion object as case classes. */
object ManagerActor {

  /** Props method to create an actor of this type passing the main actor ref, to which the overall result should be sent. */
  def props(mainActorRef: ActorRef) = Props(classOf[ManagerActor], mainActorRef)

  /** Indicates a task to sum up the integers in interval [from,until). */
  case class Interval(from: Long, until: Long)

  /** Holds the result of a sum up task. */
  case class Result(number: BigInt)

}

/** An Actor which distributes the summing up of integers on so many actors, as are cores available in this JVM. */
class ManagerActor(mainActorRef: ActorRef) extends Actor with ActorLogging {

  import ManagerActor._

  private val cores = Runtime.getRuntime().availableProcessors();

  private var total: BigInt = 0

  private var resultCount = 0;

  def receive: Receive = {
    case Interval(from, until) => { //Received message for distributing the summing up.
      val len = (until - from) / cores
      val bounds = (0 until cores).map(_ * len).toList ++ List(until)
      log.info(s"Cores: $cores, interval bounds: ${bounds.mkString(", ")}")
      for (i <- 0 until cores) {
        val sumupActor = context.actorOf(SumupActor.props, s"sumup-$i")
        sumupActor ! Interval(bounds(i), bounds(i + 1))
      }
    }
    case Result(number) => { //Received message with a partial result.
      total += number
      resultCount += 1
      if (resultCount >= cores) {
        log.info(s"Total sum is $total")
        mainActorRef ! Result(total)
      }
    }
  }

}
