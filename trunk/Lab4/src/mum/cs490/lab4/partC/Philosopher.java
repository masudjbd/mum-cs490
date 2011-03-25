package mum.cs490.lab4.partC;

import java.util.Random;

class Philosopher implements Runnable {
	static final int N = 5; // number of philosophers
	static final int THINKING = 1;
	static final int EATING = 2;

	static int state[] = new int[N]; // array to keep track of states
//	static Lock mutex = new Lock(); // for critical regions
//	static Lock mutex2 = new Lock(); // for critical regions
//	static ConditionQ f[] = new ConditionQ[N];// one cond. Q per fork
	static int pnum = 0;
	static Random random = new Random();
	static int charCount = 0; // for pretty-printing

	static Semaphore fork[] = new Semaphore[N]; // one semaphore per fork
	static Semaphore mtx[] = new Semaphore[N]; // for critical regions
	static Semaphore runMutex = new Semaphore(1); // for critical region in the run method
	static Semaphore countMutex = new Semaphore(1);

	static public void main(String[] arg) {
		new Philosopher();
	}

	Philosopher() {
//		mutex.lock();
		for (int i = 0; i < N; i++) {
//			f[i] = mutex.newCondition("Q" + i); // allocate semaphore for each
//												// fork
//			f[i].signal(); // initialize forks to available
			fork[i] = new Semaphore(1);
			mtx[i] = new Semaphore(1);
		}
//		mutex.unlock();
		for (int i = 0; i < N; i++) {
			new Thread(this).start(); // create thread and start
		}
	}

	public void run() {// this is where each philosopher thread starts.
//		theMutex.down();
		int i = pnum; // assign number to philosopher
		pnum++; // next philosopher number
//		theMutex.up();
		while (true) // repeat forever
		{
			think(); // philosopher is thinking
			takeForks(i); // acquire two forks or block
			eat(i); // use the forks to eat
			putForks(i); // put both forks back on the table
		}
	}

	void takeForks(int i) { // i: which philosopher (0 to N-1)
		// acquire the two forks one at a time
		int even = ((i % 2) == 0) ? i : i + 1;
		int odd = ((i % 2) == 1) ? i : i + 1;
		if (i == 4)
			odd = 0;
		fork[even].down();
		fork[odd].down();
		mtx[i].down();
		state[i] = EATING; // record that philosopher is eating
		mtx[i].up();
	}

	void putForks(int i) { // i: which philosopher (0 to N-1)
		int even = ((i % 2) == 0) ? i : i + 1;
		int odd = ((i % 2) == 1) ? i : (i + 1) % 5;
		mtx[i].down();
		state[i] = THINKING; // philosopher has finished eating
		mtx[i].up();
		fork[odd].up();
		fork[even].up();
	}

//	int right(int i) { // index of right fork
//		return i;
//	}
//
//	int left(int i) { // index of left fork
//		return (i + 1) % N;
//	}

	int right(int i) { // index of right philosopher
		int r = i - 1;
		if (r < 0)
			r = 4;
		return r;
	}

	int left(int i) { // index of left philosopher
		int l = (i + 1) % 5;
		return l;
	}

	void eat(int i) {
		System.out.print(i + " "); // print the number of philosopher
		countMutex.down();
		charCount++;
		if ((charCount % 30) == 0) // print rows of 30 numbers each
			System.out.println(); // start a new row
		countMutex.up();
		pause();
		checkForConflict(i);
	}

	void checkForConflict(int i) {
		int leftPhilosopher = left(i);
		mtx[leftPhilosopher].down();
		if (state[leftPhilosopher] == EATING) {
			System.out.println("Conflict between " + leftPhilosopher + " and "
					+ i);
		}
		mtx[leftPhilosopher].up();
		mtx[right(i)].down();
		if (state[right(i)] == EATING) {
			System.out.println("Conflict between " + i + " and " + right(i));
		}
		mtx[right(i)].up();
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
