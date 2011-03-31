package mum.cs490.lab4.partB;

public class Producer implements Runnable {

	// NOT shared by other producers (non-static fields and methods)
	private int pid;

	public Producer(int pid) {
		this.pid = pid;
	}

	public void run() {
		while (true) {
			int item = produce_item();
			// Main.emptySemaphore.down();
			// Main.mutex.acquire();
			Main.lock.lock();
			while (Main.count == Main.max)
				Main.emptyQ.await();
			insert_item(item);
			// Main.mutex.release();
			// Main.fullSemaphore.up();
			Main.fullQ.signal();
			Main.lock.unlock();
		}

	}

	int produce_item() {
		Main.pause(1000);
		System.out.println("produce=P" + pid);
		return pid;
	}

	void insert_item(int item) {
		System.out.println("P" + pid + " inserting '" + item + "' into buffer["
				+ Main.count + "]");
		Main.buffer[Main.count] = item;
		Main.count++;
	}

}
