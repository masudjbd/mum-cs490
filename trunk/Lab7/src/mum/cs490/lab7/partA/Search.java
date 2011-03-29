package mum.cs490.lab7.partA;

import java.util.ArrayList;
import java.util.List;

public class Search implements Runnable {

	private static int[] numbers = { 1, 2, 4, 2, 1, 5, 8, 4, 23, 12, 45, 23,
			12, 67, 87, 45, 23, 25, 12, 1 };
	private static int queryInt = 12;
	private static List<Integer> resultList = new ArrayList<Integer>();
	private static final int NumberOfThreads = 4;
	private static int currentSlice = 0; // should start with 0
	static Thread threads[] = new Thread[NumberOfThreads];
	private static Lock l = new Lock();
	int sizeOfSlice = numbers.length / NumberOfThreads;

	public Search() {
		partitionAndRun();
		join();
		displayResult();
	}

	private void join() {
		for (int i = 0; i < NumberOfThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
			}
		}
		
	}

	public static void main(String... args) {
		new Search();
	}

	private void displayResult() {
		System.out.println(resultList);
	}

	private void partitionAndRun() {
		for (int i = 0; i < NumberOfThreads; i++) {
			threads[i] = new Thread(this);
			threads[i].run();
		}
	}

	// 0-4, 5-9, 10-14, 15-19
	//  0    1     2      3

	@Override
	public void run() {

		l.lock();
		if (currentSlice > NumberOfThreads)
			return;
		int localCurrentSlice = currentSlice;
		currentSlice++;
		l.unlock();

		int firstIndex = localCurrentSlice * sizeOfSlice; // assign it
		int lastIndex = firstIndex + sizeOfSlice - 1; // assign it
		System.out.println("FirstIndex = " + firstIndex + " LastIndex = " + lastIndex);
		for (int i = firstIndex; i <= lastIndex; i++) {
			if (numbers[i] == queryInt) {
				l.lock();
				resultList.add(i);
				l.unlock();
			}
		}
	}

}
