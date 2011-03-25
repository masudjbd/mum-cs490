package mum.cs490.lab2.partC;

public class Counter implements Runnable  {
    static long count = 0;
    static final int threadCnt = 10;
    static Thread threads[] = new Thread[threadCnt];
    static Mutex theMutex = new Mutex();

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

    public Counter(int tid) {
	this.tid = tid;
    }
    public void run()  {
	for (int i=0; i<100; i++)  {
		theMutex.acquire();

		System.out.print(tid + " "); // print pid followed by a space
		count = count + 1;
		if (count % 25 == 0) {    // print 50 characters per line
		    System.out.println(); 
		}
		theMutex.release();

	}

    }

}
