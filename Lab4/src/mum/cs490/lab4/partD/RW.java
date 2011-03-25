package mum.cs490.lab4.partD;
import java.util.Random;

public class RW {
    // mutex is for critical regions
    public static final Semaphore mutex = new Semaphore(1);

    // controls write access to the DB
    public static final Semaphore db = new Semaphore(1);

    public static int rc = 0;		// number of threads reading the DB

    static Random random = new Random();

    public static void main(String[] arg)  {
	for (int i=0; i<50; i++)  { 
	    Reader reader = new Reader(i);
	    new Thread(reader).start();  // create thread for each reader 
	}
	for (int i=0; i<3; i++)  {
	    Writer writer = new Writer(i);
	    new Thread(writer).start();  // create thread for each writer
	}
    }

    static public void pause(int milliseconds)  {
	try{Thread.sleep(random.nextInt()%milliseconds);}catch(Exception e){}
    }

}
