package mum.cs490.lab5;

public class CounterLock implements Runnable  {
    static int count = 0;
    static final int threadCnt = 10;
    static Thread threads[] = new Thread[threadCnt];

    static public void main(String[] arg)  {

	System.out.println(); 
	for (int i=0; i<threadCnt; i++)  {
	    Counter c = new Counter (i);
	    threads[i] = new Thread(c);  // create thread for each counter
	    threads[i].start();
	}
	for (int i=0; i<threadCnt; i++)  {
	    try {
		threads[i].join();
	    } catch(Exception e) { }
	}

	System.out.println("Count="+count);
    }

    private int tid;

    public CounterLock(int tid) {
	this.tid = tid;
    }
    public void run()  {
	for (int i=0; i<100; i++)  {

		System.out.print(tid + " "); // print pid followed by a space
		count = count + 1;
		if (count % 25 == 0) {    // print 50 characters per line
		    System.out.println(); 
		}

	}

    }

}
