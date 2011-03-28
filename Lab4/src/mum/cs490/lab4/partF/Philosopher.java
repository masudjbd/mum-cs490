package mum.cs490.lab4.partF;

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

	static Lock countLock = new Lock();
	static Lock mutexLock = new Lock();
	static Lock forkLock = new Lock();
	static ConditionQ[] conditionQs = new ConditionQ[N];

	static public void main(String[] arg) {
		new Philosopher();
	}

	Philosopher() {
		for (int i = 0; i < N; i++) {
			conditionQs[i] = forkLock.newCondition(i + " ");
		}
		for (int i = 0; i < N; i++) {
			new Thread(this).start(); // create thread and start
		}
		for (int i = 0; i < N; i++) {
			forkLock.lock();
			conditionQs[i].signal();
			forkLock.unlock();
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
		// acquire the two forks one at a time
		pause();
		int even = ((i % 2) == 0) ? i : i + 1;
		int odd = ((i % 2) == 1) ? i : i + 1;
		if (i == 4)
			odd = 0;
		forkLock.lock();
		conditionQs[even].await();
		conditionQs[odd].await();
		forkLock.unlock();
		mutexLock.lock();
		state[i] = EATING; // record that philosopher is eating
		mutexLock.unlock();
	}

	void putForks(int i) { // i: which philosopher (0 to N-1)
		int even = ((i % 2) == 0) ? i : i + 1;
		int odd = ((i % 2) == 1) ? i : (i + 1) % 5;
		mutexLock.lock();
		state[i] = THINKING; // philosopher has finished eating
		mutexLock.unlock();
		forkLock.lock();
		conditionQs[even].signal();
		conditionQs[odd].signal();
		forkLock.unlock();
	}

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
		countLock.lock();
		charCount++;
		if ((charCount % 30) == 0) // print rows of 30 numbers each
			System.out.println(); // start a new row
		countLock.unlock();
		pause();
		checkForConflict(i);
	}

	void checkForConflict(int i) {
		int leftPhilosopher = left(i);
		mutexLock.lock();
		if (state[leftPhilosopher] == EATING) {
			System.out.println("Conflict between " + leftPhilosopher + " and "
					+ i);
		}
		if (state[right(i)] == EATING) {
			System.out.println("Conflict between " + i + " and " + right(i));
		}
		mutexLock.unlock();
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
