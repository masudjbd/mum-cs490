package mum.cs490.lab3.partA;

public class Lock {
    private boolean locked = false;
    private Thread owner = null;
    private int threadCount = 0;

    public Lock() {
    }

    /** if locked
     *		then wait 
     *  	else lock the Lock and continue
     */
    public void lock() {
	synchronized (this) {
	    while (locked && owner != Thread.currentThread()) {
		try {
		    this.wait();
		} catch (Exception e) {
		}
	    }
	    owner = Thread.currentThread();
	    threadCount++;
	    locked = true;
	}
    }

    /** wake up waiting threads and unlock the Lock
     */
    public void unlock() {
	synchronized (this) {
	    if (Thread.currentThread() != owner) {
		throw new Error("Attempt to release Lock " 
				+ "by non-owner thread\n");
	    }
	    threadCount--;
	    if (threadCount == 0) {
		locked = false;	// set to unlocked
		this.notify();	// wake up a waiting thread
		owner = null;
	    }
	}
    }

    /** is the Lock locked
     */
    public boolean isLocked() {
	synchronized (this) {
	    return locked;
	}
    }

} // end of class Lock
