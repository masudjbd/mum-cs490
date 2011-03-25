package mum.cs490.lab2.partD;

public class Producer implements Runnable {

	// NOT shared by other producers (non-static fields and methods)
	private int pid;

	public Producer(int pid) {
		this.pid = pid;
	}

	@Override
	public void run() {
		while (true) {
			int item = produce_item();
			Main.emptyMutex.acquire();
			while (Main.emptyCount == 0) {
				Main.emptyMutex.release();
				Main.emptyMutex.acquire();
			}
			Main.fullMutex.acquire();
			Main.emptyCount--;
			Main.itemMutex.acquire();
			insert_item(item);
			Main.itemMutex.release();
			Main.fullCount++;
			Main.fullMutex.release();
			Main.emptyMutex.release();
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
