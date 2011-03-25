package mum.cs490.lab5.partB;

public class ThreadSafeInt {
	private int value = 0;

	public ThreadSafeInt(int initVal) {
		synchronized (this) {
			value = initVal;
		}
	}

	/**
	 * decrement value
	 */
	public void dec() {
		synchronized (this) {
			value--;
		}
	}

	/**
	 * increment value
	 */
	public void inc() {
		synchronized (this) {
			value++; // increment value
		}
	}

	/**
	 * get the value of ThreadSafeInt
	 */
	public int getValue() {
		synchronized (this) {
			return value;
		}
	}

	/**
	 * set the value of ThreadSafeInt
	 */
	public void setValue(int newVal) {
		synchronized (this) {
			value = newVal;
		}
	}
}
