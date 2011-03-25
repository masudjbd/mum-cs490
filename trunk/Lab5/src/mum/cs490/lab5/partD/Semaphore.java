package mum.cs490.lab5.partD;
public class Semaphore {
    private int value = 0;

    public Semaphore(int initVal) {
	if (initVal < 0) {
	    System.err.println("initial value cannot be less than 0: "
			       + "default=0");
	    value = 0;
	} else {
	    value = initVal;
	}
    }

    /** (try) if value is zero 
     *		then wait 
     *  	else decrement value
     */
    public void down() {
	synchronized (this) {
	    while (value <= 0) {
		try {
		    wait();
		} catch (Exception e) {
		}
	    }
	    value--;
	}
    }

    /** (increase) wake up waiting threads and increment value
     */
    public void up() {
	synchronized (this) {
	    value++;		// increment value
	    notify();		// wake up a waiting thread
	}
    }

    /** (get value) get the value of the Semaphore
     */
    public int getValue() {
	synchronized (this) {
	    return value;
	}
    }

    /** (set value) set the value of the Semaphore
     */
    public void setValue(int newVal) {
	synchronized (this) {
	    if (newVal >= 0) {
		value = newVal;
		notifyAll();		// wake up any waiting threads
	    } else {
		System.err.println("a semaphore cannot have a negative value "
				   +newVal);
	    }
	}
    }

} // end of class Semaphore
