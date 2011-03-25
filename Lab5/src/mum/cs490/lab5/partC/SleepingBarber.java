package mum.cs490.lab5.partC;
import java.util.Random;

public class SleepingBarber {
    static final int CHAIRS = 10;
    static final int MILLISECS = 500;

    // mutex is for critical regions
    public static final Semaphore mutex = new Semaphore(1);
    public static int waiting = 0;	// number of customers waiting
    public static int bid = 0;	//  the next barber to start cutting hair
    public static int cid = 0;	// the next customer to have his hair cut

    public static final Semaphore customers = new Semaphore(0);
    public static final Semaphore barbers = new Semaphore(0);

    static final Random random = new Random();

    public static void main(String[] arg)  {
	for (int i=0; i<1; i++)  {
	    Barber barber = new Barber(i);
	    new Thread(barber).start();  // create thread for each barber
	}
	for (int i=0; i<25; i++)  { 
	    Customer customer = new Customer(i);
	    new Thread(customer).start();  // create thread for each customer 
	}
    }

    static public void pause(int milliseconds)  {
	try{Thread.sleep(random.nextInt()%milliseconds);}catch(Exception e){}
    }

    static class Barber implements Runnable  {
	int bid;
	Barber(int bid) {
	    this.bid = bid;
	}
	public void run( ) {
	    while (true) {
		customers.down();    // wait for customers
		mutex.down();
		waiting --;
		barbers.up();        // signal that the barber is available
		mutex.up();
		cutHair();
	    }
	}
	void cutHair() {
	    SleepingBarber.bid = bid;  // communicate it to the customer
	    System.out.println("b" + bid 
			       + " is cutting c" + cid + "'s hair");
	    pause(MILLISECS);
	}
    }

    static class Customer implements Runnable  {
	int cid;
	Customer(int cid) {
	    this.cid = cid;
	}
	public void run( ) {
	    while (true) {
		mutex.down();
		if (waiting < CHAIRS) {
		    waiting ++;
		    customers.up();   // there are customers
		    mutex.up();
		    barbers.down();   // wait for the barber
		    getHaircut();
		} else {
		    mutex.up();
		    pause(MILLISECS);
		}
	    }
	}
	void getHaircut() {
	    SleepingBarber.cid = cid;  // communicate it to the barber
	    System.out.println("c" + cid 
			       + "'s hair is being cut by b" + bid);
	    pause(MILLISECS);
	}
    }
}
