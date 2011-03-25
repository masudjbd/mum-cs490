package mum.cs490.lab5;

public class ThreadSafeInt {
	private int value = 0;
	private Lock lock = new Lock();

	public ThreadSafeInt(int initVal) {
		lock.lock();
		value = initVal;
		lock.unlock();
	}

	/**
	 * decrement value
	 */
	public void dec() {
		lock.lock();
		value--;
		lock.unlock();
	}

	/**
	 * increment value
	 */
	public void inc() {
		lock.lock();
		value++; // increment value
		lock.unlock();
	}

	/**
	 * get the value of ThreadSafeInt
	 */
	public int getValue() {
		lock.lock();
		int returnValue = value;
		lock.unlock();
		return returnValue;
	}

	/**
	 * set the value of ThreadSafeInt
	 */
	public void setValue(int newVal) {
		lock.lock();
		value = newVal;
		lock.unlock();
	}
}
