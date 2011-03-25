package mum.cs490.lab5;

public class Lock {
	private Thread owner = null;
	private int lockCount = 0;
	private Semaphore mutex = new Semaphore(1);
	private Semaphore waitQ = new Semaphore(0);  // waiting Q for the lock
	private int waiterCount = 0;

	public Lock() {
	}

	/**
	 * if locked then wait else lock the Lock and continue
	 */
	public void lock() {
		mutex.down();
		    if (Thread.currentThread() != owner) {
			while (owner != null) {
				waiterCount++;
				mutex.up();  // release mutex 
				waitQ.down();  // wait until lock is available
				mutex.down();  // re-acquire the mutex
				waiterCount--;
		        }
			owner = Thread.currentThread();
			lockCount++;
		    }
		mutex.up();
	}

	/**
	 * wake up waiting threads and unlock the Lock
	 */
	public void unlock() {
		mutex.down();
			if (Thread.currentThread() != owner) {
				throw new Error("Attempt to release Lock "
						+ "by non-owner thread\n");
			}
			lockCount--;
			if (lockCount == 0) {
				if (waiterCount > 0) {
					waitQ.up(); // wake up a waiting thread
				}
				owner = null;
			}
		mutex.up();
	}

	/**
	 * is the Lock locked
	 */
	public boolean tryLock() {
		mutex.down();
			if (owner != null && owner != Thread.currentThread()) {
				mutex.up();
				return false;
			} else {
				owner = Thread.currentThread();
				lockCount++;
				mutex.up();
				return true;
			}
	}

	/**
	 * If the current thread owns the lock, then return true, 
	 *          otherwise return false
	 */
	private boolean ownedByCurrentThread() {

		boolean result = (owner == Thread.currentThread());

		return result;
	}

	/**
	 * Create a new ConditionQ for this lock. Has the effect of allowing
	 * multiple wait-sets for this lock.
	 */
	public ConditionQ newCondition(String name) {
		return new LockCondition(name);
	}

	private class LockCondition implements ConditionQ {
		private String name; // the name of this condition
		private int waiters = 0; // number of threads waiting on this condition
		private Semaphore waitQ = new Semaphore(0); // waiting queue for a condition

		/** the lock starts out available. */
		public LockCondition(String name) {
			this.name = name;
		}

		/**
		 * This method causes the current thread to block until 
		 * it is awakened by the signal method.
		 */
		public void await() {
			if (!ownedByCurrentThread()) {
				throw new Error("Attempt to wait on Condition " + name
						+ " by non-owner thread");
			}
			int prevLockCount = lockCount;
			// set lockCount to 1 so the lock will be released
			Lock.this.lockCount = 1;
			waiters++;
			Lock.this.unlock(); // release the associated lock
			waitQ.down();  // wait for a signal
			Lock.this.lock(); // re-acquire the lock
			// restore the lock count so the count is correct
			Lock.this.lockCount = prevLockCount;
		}

		/**
		 * If there are threads waiting on this ConditionQ,
		 * then one of the waiters is signaled
		 */
		public void signal() {
			if (!ownedByCurrentThread()) {
				throw new Error("Attempt to signal Condition " + name
						+ " by non-owner thread ");
			}
			waiters--;
			waitQ.up();

		}

		/**
		 * If there are threads waiting on this ConditionQ,
		 * then all of the waiters are signaled
		 */
		public void signalAll() {
			if (!ownedByCurrentThread()) {
				throw new Error("Attempt to release lock " + name
						+ " by non-owner thread");
			}
			while (waiters > 0) {
				waiters--;
				waitQ.up();
			}
		}

		/**
		 * If there are waiters, this method returns true, 
		 * otherwise it returns false
		 */
		public boolean hasWaiters() {

			boolean result = waiters > 0;

			return result;
		}

	}
} // end of class Lock
