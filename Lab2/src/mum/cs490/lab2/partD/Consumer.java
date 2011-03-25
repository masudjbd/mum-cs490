package mum.cs490.lab2.partD;

public class Consumer implements Runnable {

	// NOT shared by other consumers (non-static fields and methods)
	private int pid;

	public Consumer(int pid) {
		this.pid = pid;
	}

	@Override
	public void run() {
		while (true) {
			Main.emptyMutex.acquire();
			while (Main.emptyCount == 5) {
				Main.emptyMutex.release();
				Main.emptyMutex.acquire();
			}
			Main.fullMutex.acquire();
			Main.fullCount--;
			Main.itemMutex.acquire();
			int item = remove_item();
			Main.itemMutex.release();
			Main.emptyCount++;
			Main.fullMutex.release();
			Main.emptyMutex.release();
			consume_item(item);
		}

	}

	int remove_item() {
		Main.count--;
		int item = Main.buffer[Main.count];
		System.out.println("C" + pid + " removing '" + item + "' from buffer["
				+ Main.count + "]");
		Main.buffer[Main.count] = -1;
		return item;
	}

	void consume_item(int item) {
		System.out.println("C" + pid + " consuming " + item);
		Main.pause(1000);
	}

}
