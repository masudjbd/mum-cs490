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
		new Thread(new Mother()).start();
		new Thread(new Tina()).start();
		new Thread(new Judy()).start();
	}

	private static void checkCookieCount() {
		if (cookieCount == 0) {
			synchronized (mother) {
				System.out.println("Oops! no cookie mother!!! Please help.");
				mother.notifyAll();
			}
			try {
				System.out
						.println("We are hungry mother. Where are other cookies? We are waiting.");
				synchronized (children) {
					children.wait();
				}
			} catch (InterruptedException e) {
			}

		}

	}

	private static void randomWait() {
		int millisecs = random.nextInt(100);
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
				boolean empty = false;
				synchronized (o) {
					if (cookieCount == 0) {
						empty = true;
						synchronized (mother) {
							System.out
									.println("Oops! no cookie mother!!! Please help.");
							mother.notifyAll();
						}
					}
				}
				if (empty) {
					try {
						System.out
								.println("We are hungry mother. Where are other cookies? We are waiting.");
						synchronized (children) {
							children.wait();
						}
					} catch (InterruptedException e) {
					}
				}
				synchronized (o) {

					cookieCount--;
					tinaCount++;
					System.out.println("TinaCount = " + tinaCount);
					o.notify();
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
				boolean empty = false;
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
					// start of method

					if (cookieCount == 0) {
						empty = true;
						synchronized (mother) {
							System.out
									.println("Oops! no cookie mother!!! Please help.");
							mother.notify();
						}
					}
				}

				if (empty) {
					try {
						System.out
								.println("We are hungry mother. Where are other cookies? We are waiting.");
						synchronized (children) {
							children.wait();
						}
					} catch (InterruptedException e) {
					}
				}
				// end of method
				synchronized (o) {

				}
				System.out.println("Judy is eating cookie.");
				synchronized (o) {
					cookieCount--;
					tinaCount = 0;
					System.out.println("Judy has now reset the tinaCount.");
				}
			}

		}

	}

	static class Mother implements Runnable {

		@Override
		public void run() {
			while (true) {
				synchronized (mother) {
					try {
						System.out
								.println("Mother is sleeping children. Wake me up when you finish your cookies.");
						mother.wait();
					} catch (InterruptedException e) {
					}
				}
				synchronized (o) {
					cookieCount = 25;
					System.out
							.println("Mother has filled the cookie jar with good cookies.");
				}
				synchronized (children) {
					System.out
							.println("Get the cookies dear! I've filled up the jar.");
					children.notifyAll();
				}
			}

		}

	}

}
