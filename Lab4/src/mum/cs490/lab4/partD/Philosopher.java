package mum.cs490.lab4.partD;
import java.util.Random;

class Philosopher implements Runnable  {
    static final int N = 6;		// number of philosophers
    static final int THINKING = 1;
    static final int EATING = 2;
	
    static int state[] = new int[N];  // array to keep track of states
    static Lock mutex = new Lock(); // for critical regions
    static Lock mutex2 = new Lock(); // for critical regions
    static ConditionQ f[] = new ConditionQ[N];// one cond. Q per fork
    static int pnum = 0;
    static Random random = new Random();
    static int charCount= 0;			// for pretty-printing	
	
    static public void main(String[] arg)  {
	new Philosopher();
    }
    Philosopher()  {
	mutex.lock();
	for (int i=0; i<N; i++)  {
	    f[i] = mutex.newCondition("Q"+i);	// allocate semaphore for each fork 
	    f[i].signal(); // initialize forks to available
	}
	mutex.unlock();
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
	// acquire the two forks one at a time
	state[i] = EATING;		// record that philosopher is eating
    }
    void putForks(int i)   {		// i: which philosopher (0 to N-1)
	state[i] = THINKING;		// philosopher has finished eating
    }	

    int right(int i)  { // index of right fork
	return i;
    }
    int left(int i)  { // index of left fork
	return (i+1)%N;
    }
    void eat(int i)  {
	System.out.print(i+" ");	// print the number of philosopher
	charCount++;
	if ((charCount % 30) == 0)	// print rows of 30 numbers each
	    System.out.println();	// start a new row
	pause();
	checkForConflict(i);
    }
    void checkForConflict(int i) {
	int leftPhilosopher = left(i);
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
