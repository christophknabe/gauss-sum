package knabe.gauss_sum

import akka.actor.ActorSystem
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike, Matchers}
import akka.testkit.{TestKit, TestProbe}

import scala.concurrent.duration._

/** Tests the speed of sequential versus actor-parallel computation.
  * The sum of numbers 1 until count is at first computed sequentially, at second parallelized on so many actors, as there are cores available.
  *
  * @author Christoph Knabe, Beuth University Berlin, Germany
  * @since 2016-12-18
  */
class TestSuite extends TestProbe(ActorSystem("SumUp"), "TestSuite") with FunSuiteLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    println(s"Shutting down $system")
    TestKit.shutdownActorSystem(system)
  }

  /** Returns the time elapsed while executing the block in milliseconds. */
  private def measureMillis(block: => Any): Long = {
    val start = System.currentTimeMillis
    block
    val stop = System.currentTimeMillis
    stop - start
  }

  test("sum up functionality") {
    SumupActor.sumInterval(4, 11) shouldBe 49
    SumupActor.sumInterval(3, 11) shouldBe 52
    SumupActor.sumInterval(2, 11) shouldBe 54
    SumupActor.sumInterval(1, 11) shouldBe 55
    SumupActor.sumInterval(1, 101) shouldBe 5050
    SumupActor.sumInterval(1, 1001) shouldBe 500500
    SumupActor.sumInterval(1, 10001) shouldBe 50005000
  }

  val count: Int = 2000000000

  /** Computes the sum of numbers 1 until without n by the gauss formula. @see https://de.wikipedia.org/wiki/Gau%C3%9Fsche_Summenformel */
  private def gaussSum(n: Int): Long = (count - 1L) * count / 2

  test("sequential sum up") {
    val millis = measureMillis {
      assertResult(gaussSum(count)) {
        SumupActor.sumInterval(1, count)
      }
    }
    println(s"Sequentially elapsed time: $millis millis")
  }

  test("parallel sum up") {
    val millis = measureMillis {
      val managerActor = system.actorOf(ManagerActor.props(testActor), "manager")
      managerActor ! ManagerActor.Interval(0, count)
      expectMsg(Duration(40, SECONDS), ManagerActor.Result(gaussSum(count)))
    }
    println(s"Parallely elapsed time: $millis millis")
  }

}
