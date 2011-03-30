package mum.cs490.lab7.partB.busyWaiting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Philosopher implements Runnable {
	static final int N = 5; // number of philosophers

	/* states */
	static final int THINKING = 1;
	static final int EATING = 2;
	static final int HUNGRY = 3;

	/* boolean flags */
	boolean isLeftHungry;
	boolean isLeftLeftEating;
	boolean isRightHungry;
	boolean isRightRightEating;

	/* misc */
	static int state[] = new int[N]; // array to keep track of states
	static int pnum = 0;
	static Random random = new Random();
	static int charCount = 0; // for pretty-printing

	/* Semaphores */
	static Semaphore[] forkRequest = new Semaphore[N];
	static Semaphore forkRequestHandler = new Semaphore(0);

	/* Mutexes */
	static Semaphore mutex = new Semaphore(1); // for critical regions
	static Semaphore currentPhilosopherMutex = new Semaphore(1);
	static Semaphore characterCountMutex = new Semaphore(1);

	/**
	 * Main method
	 * 
	 * @param arg
	 */
	static public void main(String[] arg) {
		for (int i = 0; i < N; i++) {
//			System.out.print("Left of " + i + " is " + left(i));
//			System.out.println("---- And Right of " + i + " is " + right(i));
		}
		new Philosopher();
	}

	/**
	 * Constructor
	 */
	Philosopher() {
		for (int i = 0; i < N; i++) {
			forkRequest[i] = new Semaphore(0);
		}
		mutex = new Semaphore(1);
		new Thread(new Daemon()).start();
		for (int i = 0; i < N; i++) {
			new Thread(this).start(); // create thread and start
		}
	}

	public void run() {// this is where each philosopher thread starts.
		int i = pnum; // assign number to philosopher
		pnum++; // next Philosopher index
		while (true) // repeat forever
		{
			think(); // philosopher is thinking
//			System.out.println("Philosopher " + i + " has finished thinking.");
			takeForks(i); // acquire two forks or block
			eat(i); // use the forks to eat
			putForks(i); // put both forks back on the table
		}
	}

	/**
	 * Adds the current philosopher into the daemon's list, sets the state of
	 * this philosopher to HUNGRY, wakes up the deamon and waits till it gets
	 * signal from the deamon.
	 * 
	 * @param i
	 *            Philosopher index
	 */
	void takeForks(int i) { // i: which philosopher (0 to N-1)

		currentPhilosopherMutex.down();
		{
			Daemon.currentPhilosopher.add(i);
//			System.out.println("Philosopher " + i + " is now on queue.");
		}
		currentPhilosopherMutex.up();

		mutex.down();
		{
			state[i] = HUNGRY;
//			System.out.println("Philosopher " + i + " is now hungry.");
		}
		mutex.up();

		forkRequestHandler.up();
//		System.out.println("Philosopher " + i
//				+ " has now waken up the daemon and waiting for turn.");
		forkRequest[i].down();
	}

	/**
	 * Changes the state of this philosopher to THINKING and checks if the
	 * philosophers on the left and the right are hungry or not. If they are
	 * hungry and they are not surrounded by eating philosophers, then it wakes
	 * them up. (Actually, this is not the job of putForks, it should be done by
	 * the deamon, but we could not do this here.)
	 * 
	 * @param i
	 *            Philosopher index
	 */
	void putForks(int i) { // i: which philosopher (0 to N-1)
		mutex.down();
		{
			state[i] = THINKING; // philosopher has finished eating
//			System.out.println("Philosopher " + i + " has now put down forks.");

			isLeftHungry = (state[left(i)] == HUNGRY);
			isLeftLeftEating = (state[left(left(i))] == EATING);
			isRightHungry = (state[right(i)] == HUNGRY);
			isRightRightEating = (state[right(right(i))] == EATING);

//			System.out.println("Left Hungry = " + isLeftHungry);
//			System.out.println("LeftLeftEating = " + isLeftLeftEating);
//			System.out.println("Right Hungry = " + isRightHungry);
//			System.out.println("RightRightEating = " + isRightRightEating);
		}
		mutex.up();
		if (isLeftHungry && !isLeftLeftEating) {
//			System.out.println("Philosopher " + i + " has waken up" + left(i));
			forkRequest[left(i)].up();
		}
		if (isRightHungry && !isRightRightEating) {
//			System.out.println("Philosopher " + i + " has waken up" + right(i));
			forkRequest[right(i)].up();
		}
	}

	/**
	 * Pauses for some time and checks whether there is a conflict.
	 * 
	 * @param i
	 *            Philosopher index
	 */
	void eat(int i) {
//		System.out.println("Philosopher " + i + " is now eating.");
		 System.out.print(i + " "); // print the number of philosopher
		 characterCountMutex.down();
		 charCount++;
		 if ((charCount % 30) == 0) // print rows of 30 numbers each
		 System.out.println(); // start a new row
		 characterCountMutex.up();
		pause();
		checkForConflict(i);
	}

	/**
	 * Finds the index of the right philosopher.
	 * 
	 * @param i
	 *            Philosopher index
	 * @return Right philosopher index
	 */
	static int right(int i) { // index of right philosopher
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
	static int left(int i) { // index of left philosopher
		int l = (i + 1) % 5;
		return l;
	}

	/**
	 * Finds whether there is a conflict between the left and the right
	 * philosophers with the current one.
	 * 
	 * @param i
	 *            Philosopher index
	 */
	void checkForConflict(int i) {
		mutex.down();
		if (state[left(i)] == EATING) {
			System.out.println("Conflict between " + left(i) + " and " + i);
		}
		if (state[right(i)] == EATING) {
			System.out.println("Conflict between " + i + " and " + right(i));
		}
		mutex.up();
	}

	/**
	 * Do the philosophers actually think? If yes, then this method does that.
	 */
	void think() {
		pause();
	}

	/**
	 * When they have nothing to do, they just call this method.
	 */
	void pause() {
		try {
			Thread.sleep(random.nextInt() % 100);
		} catch (Exception e) {
		}
	}

	/**
	 * Static inner class for implementation of Daemon.
	 * 
	 * @author ashish and pradip
	 * 
	 */
	static private class Daemon implements Runnable {

		/**
		 * List of hungry philosophers.
		 */
		private static List<Integer> currentPhilosopher = new ArrayList<Integer>();

		@Override
		public void run() {
			while (true) {
				forkRequestHandler.down();
				currentPhilosopherMutex.down();
				int local_currentPhilosopher = currentPhilosopher.remove(0);
				currentPhilosopherMutex.up();
				mutex.down();
				if (state[left(local_currentPhilosopher)] != EATING
						&& state[right(local_currentPhilosopher)] != EATING) {
					state[local_currentPhilosopher] = EATING;
					forkRequest[local_currentPhilosopher].up();
				}
				mutex.up();
			}
		}
	}
}
