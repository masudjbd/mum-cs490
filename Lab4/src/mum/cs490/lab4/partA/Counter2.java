package mum.cs490.lab4.partA;

public class Counter2 implements Runnable {
	static private long count = 0;
	static final int threadCnt = 20;
	static Thread threads[] = new Thread[threadCnt];

	private int tid;
	private long localcount = 0;

	private static Lock c = new Lock(); // lock for static count
	private Lock l = new Lock(); // lock for local count

	static public void main(String[] arg) {

		System.out.println();
		Counter2 c1 = new Counter2(0);
		Counter2 c2 = new Counter2(0);
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < threadCnt; i++) {
				Counter2 c = i < threadCnt / 2 ? c1 : c2;
				threads[i] = new Thread(c); // create thread for each counter
				threads[i].start();
			}
			for (int i = 0; i < threadCnt; i++) {
				try {
					threads[i].join();
				} catch (Exception e) {
				}
			}

			System.out.println("Static Count=" + count);
			System.out.println("Object Count1=" + c1.localcount);
			System.out.println("Object Count2=" + c2.localcount);
			System.out.println();
		}
	}

	public Counter2(int tid) {
		this.tid = tid;
	}

	public void run() {
		long ms = 10;
		for (int i = 0; i < 100; i++) {
			l.lock();
			localcount = localcount + 1;
			l.unlock();
			pause(ms);
			c.lock();
			count++;
			c.unlock();
			pause(ms);
		}

	}

	public static void pause(long ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
		}
	}
}
