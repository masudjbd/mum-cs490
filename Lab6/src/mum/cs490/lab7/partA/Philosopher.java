package mum.cs490.lab7.partA;
import java.util.Random;

class Philosopher implements Runnable  {
    static final int N = 5;		// number of philosophers
    static final int THINKING = 1;
    static final int EATING = 2;
	
    static int state[] = new int[N];  // array to keep track of states
    static Lock mutex = new Lock(); // for critical regions
    static Lock mutex2 = new Lock(); // for critical regions
    static Lock fork[] = new Lock[N];// one Lock per fork
    static ConditionQ waitQ[] = new ConditionQ[N];// one ConditionQ per fork
    static int pnum = 0;
    static Random random = new Random();
    static int charCount= 0;			// for pretty-printing	
	
    static public void main(String[] arg)  {
	new Philosopher();
    }
    Philosopher()  {
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
	// acquire the two forks one at a time in a loop

	// if the first fork is unavailable, then block and wait
	// if the second is unavailable, 
	//     then release the first fork, block, and wait for a signal 
	//          before trying again 
	// loop until both forks have been acquired
		
	state[i] = EATING;		// record that philosopher is eating
    }
    void putForks(int i)   {		// i: which philosopher (0 to N-1)
	state[i] = THINKING;		// philosopher has finished eating
    }	

    int left(int i)  { // index of left fork
	return i;
    }
    int right(int i)  { // index of right fork
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
