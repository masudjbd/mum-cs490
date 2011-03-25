package mum.cs490.lab4.partB;
import java.util.Random;

public class Reader implements Runnable  {
    private int id;

    public Reader(int id)  {
	this.id = id;
    }

    public void run()  {// this is where each reader or writer thread starts.
	while (true) {		// repeat forever
	    RW.mutex.down();			// enter critical region
	    RW.rc++;				// record one more reader
	    if (RW.rc == 1) {
		System.out.println("R" + id + " is blocking writers");
		RW.db.down();			// block readers
	    }
	    RW.mutex.up();			// exit critical region
	    read_data_base();			// access data
	    RW.mutex.down();			// enter critical region
	    RW.rc--;				// record one less reader
	    if (RW.rc == 0) {
		System.out.println("R" + id + " is allowing writers");
		RW.db.up();			// allow writers to access DB
	    }
	    RW.mutex.up();			// exit critical region
	    use_data_read();			// process/use the data
	}
    }
    void read_data_base()  {
	System.out.println("R" + id + " is reading");
	RW.pause(1000);
    }	
    void use_data_read()  {
	System.out.println("R" + id + " is using data");
	RW.pause(1000);
    }	

}
