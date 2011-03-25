package mum.cs490.lab2.partC;

public class Consumer implements Runnable  {

    // NOT shared by other consumers (non-static fields and methods)
    private int pid;

    public Consumer(int pid) {
	this.pid = pid;
    }

    public void run()  {
	while (true) {
	    Main.fullSemaphore.down();
	    Main.mutex.acquire();
		int item = remove_item();
		Main.mutex.release();
	    Main.emptySemaphore.up();
		consume_item(item);
	}

    }

    int remove_item() {
	Main.count--;
	int item = Main.buffer[Main.count];
	System.out.println("C" + pid + " removing '" + item 
			   + "' from buffer[" + Main.count + "]");
	Main.buffer[Main.count] = -1;
	return item;
    }

    void consume_item(int item) {
	System.out.println("C" + pid + " consuming " + item); 
	Main.pause(1000);
    }

}
