package cookiejar.finitesource;

import java.util.Random;

public class CookieJar {

	static Object o = new Object();
	static Object mother = new Object();
	static Object children = new Object();

	static int tinaCount = 0;
	static int cookieCount = 25;
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
					if (cookieCount == 0) {
						synchronized (mother) {
							mother.notifyAll();
						}
						try {
							children.wait();
						} catch (InterruptedException e) {
						}
					}
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
						System.out
								.println("Judy is waiting since Tina has not eaten 2 times.");
						try {
							o.wait();
							System.out
									.println("Judy is now awake and again checking for tinaCount.");
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

	static class Mother implements Runnable {
		private static void mother() {
			synchronized (o) {
				cookieCount = 25;
				System.out
						.println("Mother has filled the cookie jar with good cookies. Hurray!!!");
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

	}

}
