package mum.cs490.lab2.partD;

public class Mutex {
	private boolean locked = false;

	public Mutex() {
	}

	public Mutex(boolean locked) {
		this.locked = locked;
	}

	/**
	 * if locked then wait else lock the mutex and continue
	 */
	public void acquire() {
		synchronized (this) {
			while (locked) {
				try {
					this.wait();
				} catch (Exception e) {
				}
			}
			locked = true;
		}
	}

	/**
	 * wake up waiting threads and unlock the mutex
	 */
	public void release() {
		synchronized (this) {
			locked = false; // set to unlocked
			this.notify(); // wake up a waiting thread
		}
	}

	/**
	 * is the mutex locked
	 */
	public boolean isLocked() {
		synchronized (this) {
			return locked;
		}
	}

} // end of class Mutex
