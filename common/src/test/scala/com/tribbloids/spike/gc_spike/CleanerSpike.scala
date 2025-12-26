package com.tribbloids.spike.gc_spike

import ai.acyclic.prover.commons.testlib.BaseSpec

import java.lang.ref.Cleaner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

object CleanerSpike {

  private object ScarcePermit {

    private val cleaner: Cleaner = Cleaner.create()

    final private class State(
        sem: Semaphore
    ) extends Runnable {
      private val cleaned: AtomicBoolean = new AtomicBoolean(false)
      override def run(): Unit = {
        if (cleaned.compareAndSet(false, true)) {
          sem.release()
        }
      }
    }

    final class Permit(
        sem: Semaphore
    ) extends AutoCloseable {

      private val acquired: Boolean = sem.tryAcquire()
      require(acquired, "no permit available")

      private val state = new State(sem)
      private val cleanable: Cleaner.Cleanable = cleaner.register(this, state)

      override def close(): Unit = cleanable.clean()
    }

    final private class StateWithSignal(
        sem: Semaphore,
        signal: CountDownLatch
    ) extends Runnable {
      private val cleaned: AtomicBoolean = new AtomicBoolean(false)
      override def run(): Unit = {
        if (cleaned.compareAndSet(false, true)) {
          sem.release()
          signal.countDown()
        }
      }
    }

    final class PermitWithSignal(
        sem: Semaphore,
        signal: CountDownLatch
    ) {

      private val acquired: Boolean = sem.tryAcquire()
      require(acquired, "no permit available")

      private val state = new StateWithSignal(sem, signal)
      private val cleanable: Cleaner.Cleanable = cleaner.register(this, state)

      def cleanNow(): Unit = cleanable.clean()
    }
  }
}

class CleanerSpike extends BaseSpec {

  import CleanerSpike._

  it("Cleaner can be used for eager cleanup of a scarce resource") {

    val sem = new Semaphore(1)

    val permit1 = new ScarcePermit.Permit(sem)
    assert(sem.availablePermits() == 0)

    permit1.close()
    assert(sem.availablePermits() == 1)

    permit1.close()
    assert(sem.availablePermits() == 1)

    val permit2 = new ScarcePermit.Permit(sem)
    assert(sem.availablePermits() == 0)

    permit2.close()
    assert(sem.availablePermits() == 1)
  }

  it("Cleaner can also reclaim a scarce resource automatically when the object becomes unreachable") {

    val sem = new Semaphore(1)
    val cleaned = new CountDownLatch(1)

    def allocateAndForget(): Unit = {
      val permit = new ScarcePermit.PermitWithSignal(sem, cleaned)
      assert(sem.availablePermits() == 0)
      assert(permit != null)
    }

    allocateAndForget()

    var i = 0
    while (cleaned.getCount > 0 && i < 200) {
      System.gc()
      System.runFinalization()
      Thread.sleep(10)
      i += 1
    }

    assert(cleaned.await(5, TimeUnit.SECONDS))
    assert(sem.availablePermits() == 1)
  }
}
