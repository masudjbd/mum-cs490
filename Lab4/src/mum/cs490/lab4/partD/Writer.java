package mum.cs490.lab4.partD;
import java.util.Random;

public class Writer implements Runnable  {
    private int id;

    public Writer(int id)  {
	this.id = id;
    }

    public void run()  {// this is where each writer thread starts.
	while (true) {			// repeat forever
	    create_data ();		// create new data

	    System.out.println("W" + id + " is requesting data access to DB");

	    RW.db.down();		// get exclusive access to DB
	    write_to_data_base();	// access data
	    RW.db.up();			// release exclusive access to DB
	}
    }
    void create_data()  {
	System.out.println("W" + id + " is creating data");
	RW.pause(1000);
    }	
    void write_to_data_base()  {
	System.out.println("W" + id + " is writing data");
	RW.pause(1000);
    }	

}
