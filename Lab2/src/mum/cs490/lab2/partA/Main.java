package mum.cs490.lab2.partA;
import java.util.Random;

public class Main {

    static Random random = new Random();
    static final int producers = 5;
    static final int consumers = 5;

    public static final int max = 5;
    public static int count = 0;
    public static int buffer[] = new int[max];

    static public void main(String[] arg)  {

	for (int i=0; i<producers; i++)  {
	    Producer c = new Producer(i);
	    Thread t = new Thread(c);  // create thread for each producer
	    t.start();
	}

	for (int i=0; i<consumers; i++)  {
	    Consumer c = new Consumer(i);
	    Thread t = new Thread(c);  // create thread for each consumer
	    t.start();
	}

    }

    static public void pause(int milliseconds)  {
	try{Thread.sleep(random.nextInt()%milliseconds);}catch(Exception e){}
    }

}
