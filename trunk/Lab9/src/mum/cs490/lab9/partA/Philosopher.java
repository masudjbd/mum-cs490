package mum.cs490.lab9.partA;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Philosopher implements Runnable {
	static final int N = 5; // number of philosophers
	static final int THINKING = 1;
	static final int EATING = 2;

	static Object token = new Object();

	static int state[] = new int[N]; // array to keep track of states
	static Lock mutex = new Lock(); // for critical regions
	static BlockingQueue<Object> newMutex = new LinkedBlockingQueue<Object>();
	// static Lock mutex2 = new Lock(); // for critical regions
	static Lock forks[] = new Lock[N];// one Lock per fork
	static BlockingQueue<Object>[] newForks = new LinkedBlockingQueue[N];
	static ConditionQ waitQ[] = new ConditionQ[N];// one ConditionQ per fork
	static BlockingQueue<Object>[] newPhilosophers = new LinkedBlockingQueue[N];
	static int pnum = 0;
	static Random random = new Random();
	static int charCount = 0; // for pretty-printing

	static public void main(String[] arg) throws Exception {
		new Philosopher();
	}

	Philosopher() throws Exception {
		newMutex.put(token);
		for (int i = 0; i < N; i++) {
			// fork[i] = new Lock();
			newForks[i] = new LinkedBlockingQueue<Object>();
			newForks[i].put(token);
			// waitQ[i] = fork[i].newCondition(i + "");
			newPhilosophers[i] = new LinkedBlockingQueue<Object>();
			// newPhilosophers[i].put(token);
		}
		for (int i = 0; i < N; i++) {
			new Thread(this).start(); // create thread and start
		}
	}

	public void run() {// this is where each philosopher thread starts.
		int i = pnum; // assign number to philosopher
		pnum++; // next philosopher number
		while (true) // repeat forever
		{
			think(); // philosopher is thinking
			takeForks(i); // acquire two forks or block
			eat(i); // use the forks to eat
			putForks(i); // put both forks back on the table
		}
	}

	void takeForks(int i) { // i: which philosopher (0 to N-1)
		// acquire the two forks one at a time in a loop

		// if the first fork is unavailable, then block and wait
		// if the second is unavailable,
		// then release the first fork, block, and wait for a signal
		// before trying again
		// loop until both forks have been acquired

		// fork[i].lock();
		try {
			newForks[i].take();
		} catch (InterruptedException e) {
		}
		synchronized (newForks) {
			while (newForks[rightFork(i)].peek() == null) {

			}
		}
		while (!forks[rightFork(i)].tryLock()) {
			waitQ[i].await();
		}
		mutex.lock();
		state[i] = EATING; // record that philosopher is eating
		mutex.unlock();
	}

	void putForks(int i) { // i: which philosopher (0 to N-1)
	// mutex.lock();
	// state[i] = THINKING; // philosopher has finished eating
	// mutex.unlock();
	// fork[i].lock();
	// waitQ[i].signal();
	// fork[i].unlock();
	// fork[right(i)].lock();
	// waitQ[right(i)].signal();
	// fork[right(i)].unlock();
		try {
			newMutex.take();
			state[i] = THINKING;
			newMutex.put(token);
			
			newForks[leftFork(i)].put(token);
			newForks[rightFork(i)].put(token);
			newPhilosophers[leftPhil(i)].put(token);
			newPhilosophers[rightPhil(i)].put(token);
		} catch (InterruptedException e) {
		}

	}

	/**
	 * Finds the index of the right philosopher.
	 * 
	 * @param i
	 *            Philosopher index
	 * @return Right philosopher index
	 */
	static int rightPhil(int i) { // index of right philosopher
		int r = i - 1;
		if (r < 0)
			r = 4;
		return r;
	}

	/**
	 * Finds the index of the left philosopher.
	 * 
	 * @param i
	 *            Philosopher index
	 * @return Left philosopher index
	 */
	static int leftPhil(int i) { // index of left philosopher
		int l = (i + 1) % 5;
		return l;
	}

	int leftFork(int i) { // index of left fork
		return i;
	}

	int rightFork(int i) { // index of right fork
		return (i + 1) % N;
	}

	void eat(int i) {
		System.out.print(i + " "); // print the number of philosopher
		charCount++;
		if ((charCount % 30) == 0) // print rows of 30 numbers each
			System.out.println(); // start a new row
		pause();
		checkForConflict(i);
	}

	void checkForConflict(int i) {
		int leftPhilosopher = (i == 0) ? N - 1 : i - 1;
		if (state[leftPhilosopher] == EATING) {
			System.out.println("Conflict between " + leftPhilosopher + " and "
					+ i);
		}
		if (state[rightFork(i)] == EATING) {
			System.out
					.println("Conflict between " + i + " and " + rightFork(i));
		}
	}

	void think() {
		pause();
	}

	void pause() {
		try {
			Thread.sleep(random.nextInt() % 100);
		} catch (Exception e) {
		}
	}

}
