package mum.cs490.lab3.partA;
import java.util.Random;

class Philosopher implements Runnable  {
    static final int N = 5;		// number of philosophers
    static final int THINKING = 1;
    static final int HUNGRY = 2;
    static final int EATING = 3;
	
    static int state[] = new int[N];  // array to keep track of states
    static Semaphore mutex = new Semaphore(1); // for critical region 1
    static Semaphore mutex2 = new Semaphore(1); // for critical region 2, if needed
    static Semaphore p[] = new Semaphore[N];// one semaphore per philosopher
    static int pnum = 0;
    static Random random = new Random();
    static int charCount= 0;			// for pretty-printing	
	
    static public void main(String[] arg)  {
	new Philosopher();
    }
    Philosopher()  {
	for (int i=0; i<N; i++)  {
	    p[i] = new Semaphore(0);	// allocate semaphore for each fork 
	}
	for (int i=0; i<N; i++)  {
	    new Thread(this).start();	// create thread and start 
	}
    }
    public void run()  {// this is where each philosopher thread starts.
	int i = pnum;			// assign number to philosopher 
	pnum++;				// next philosopher number
	while (true)			// repeat forever
	{
	    think();			// philosopher is thinking
	    takeForks(i);		// acquire two forks or block
	    eat(i);			// use the forks to eat
	    putForks(i);		// put both forks back on the table
	}
    }
    void takeForks(int i)  {		// i: which philosopher (0 to N-1)
	state[i] = HUNGRY;		// record that philosopher is hungry
	test(i);			// try to acquire two forks
    }
    void putForks(int i)   {		// i: which philosopher (0 to N-1)
	state[i] = THINKING;		// philosopher has finished eating
	test(left(i));			// see if left neighbor can now eat
	test(right(i));			// see if right neighbor can now eat
    }	
    void test(int i)	{		// i: which philosopher (0 to N-1)
	if (state[i] == HUNGRY 
	    && state[left(i)] != EATING && state[right(i)] != EATING)  {
	    state[i] = EATING;
	}
    }

    int left(int i)  {
	return (i==0) ? N-1 : i-1;
    }
    int right(int i)  {
	return (i+1)%N;
    }
    void eat(int i)  {
	System.out.print(i+" ");	// print the number of philosopher
	charCount++;
	if ((charCount % 30) == 0) {	// print rows of 30 numbers each
	    System.out.println();	// start a new row
	}
	pause();
	checkForConflict(i);
    }
    void checkForConflict(int i) {
	int leftPhilosopher = (i==0)? N-1 : i-1;
	if (state[leftPhilosopher] == EATING) {
	    System.out.println("Conflict between "+leftPhilosopher+" and "+i);
	}
	if (state[right(i)] == EATING) {
	    System.out.println("Conflict between "+i+" and "+right(i));
	}
    }
    void think()  {
	pause();
    }	
    void pause()  {
	try{Thread.sleep(random.nextInt()%100);}catch(Exception e){}
    }

}
