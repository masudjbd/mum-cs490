package mum.cs490.lab5.partD;
public class ThreadSafeInt {
    private int value = 0;

    public ThreadSafeInt(int initVal) {
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

    /** set the value of ThreadSafeInt
     */
    public void setValue(int newVal) {
	value = newVal;
    }
}
