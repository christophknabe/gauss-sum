Scala Sample for Parallel Computation by Akka Actors
====================================================

Christoph Knabe, 2022-06-10

This sample program computes the sum of integers from 1 up to but excluding an upper limit n.
Although this could be done quickly by the so-called Gauss summation formula (n-1)*n/2,
here it is done by sequential loops in order to benchmark the sequential vs. parallel solutions.
As even a sequential loop is not enough work for nowadays computers,
the workload is artificially augmented by converting each Long number
to Double, squaring it, and drawing its radix in floating point operations. 

It computes the Gauß number in two manners.

1) The total sum is computed by one sequential loop.

2) The task is distributed to so many SumUpActor-s as are available cores in the JVM. 
   The actors return their partial results to the ManagerActor, which sums them up and returns the total sum to the TestSuite.
   
The acceleration with parallelization is thereby demonstrated.

Note: See [Gauss Summation Formula](https://letstalkscience.ca/educational-resources/backgrounders/gauss-summation)