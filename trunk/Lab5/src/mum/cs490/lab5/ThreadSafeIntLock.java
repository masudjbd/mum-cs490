package mum.cs490.lab5;
public class ThreadSafeIntLock {
    private int value = 0;

    public ThreadSafeIntLock(int initVal) {
	value = initVal;
    }

    /** decrement value
     */
    public void dec() {
	    value--;
    }

    /** increment value
     */
    public void inc() {
	    value++;		// increment value
    }

    /** get the value of ThreadSafeInt
     */
    public int getValue() {
	    return value;
    }

    /** the value of ThreadSafeInt
     */
    public void setValue(int newVal) {
	value = newVal;
    }
}
