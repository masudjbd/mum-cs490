package cookiejar;

import java.util.Random;

public class CookieJar {

	static Object o = new Object();
	static int tinaCount = 0;
	static Random random = new Random();

	public static void main(String... args) {
		new Thread(new Tina()).start();
		new Thread(new Judy()).start();
	}

	private static void randomWait() {
		int millisecs = random.nextInt(2);
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
		}
	}

	static class Tina implements Runnable {

		@Override
		public void run() {
			while (true) {
				randomWait();
				synchronized (o) {
					tinaCount++;
					System.out.println("TinaCount = " + tinaCount);
					o.notifyAll();
				}
				 System.out.println("Tina is eating cookie.");
			}

		}

	}

	static class Judy implements Runnable {

		@Override
		public void run() {
			while (true) {
				randomWait();
				synchronized (o) {
					System.out.println("Judy tries to get a cookie.");
					while (tinaCount < 2) {
						System.out.println("Judy is waiting since Tina has not eaten 2 times.");
						try {
							o.wait();
							System.out.println("Judy is now awake and again checking for tinaCount.");
						} catch (InterruptedException e) {
						}
					}
				}
				System.out.println("Judy is eating cookie.");
				synchronized (o) {
					tinaCount = 0;
					System.out.println("Judy has now reset the tinaCount.");
				}
			}

		}

	}

}
