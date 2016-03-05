package com.threads.prodCons.waitNotify;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WaitNotify {

	public static void main(String args[]) {
		Vector<Integer> sharedQueue = new Vector<Integer>();
		int size = 4;
		Thread prodThread = new Thread(new Producer(sharedQueue, size),	"Producer");
		Thread consThread = new Thread(new Consumer(sharedQueue, size),	"Consumer");
		prodThread.start();
		consThread.start();
	}
}

class Producer implements Runnable {

	private final Vector<Integer> sharedQueue;
	private final int SIZE;

	public Producer(Vector<Integer> sharedQueue, int size) {
		this.sharedQueue = sharedQueue;
		this.SIZE = size;
	}

	@Override
	public void run() {
		for (int i = 0; i < 7; i++) {
			
			try {
				while (sharedQueue.size() == SIZE) {
					synchronized (sharedQueue) {
						System.out.println("Queue is full "	+ Thread.currentThread().getName() 	+ " is waiting , size: " + sharedQueue.size());
						sharedQueue.wait();
					}
				}

				// producing element and notify consumers
				synchronized (sharedQueue) {
					sharedQueue.add(i);
					System.out.println("Produced: " + i);
					sharedQueue.notifyAll();
				}
				//produce(i);
			} catch (InterruptedException ex) {
				Logger.getLogger(Producer.class.getName()).log(Level.SEVERE,null, ex);
			}
		}
	}
}

class Consumer implements Runnable {

	private final Vector<Integer> sharedQueue;
	private final int SIZE;

	public Consumer(Vector<Integer> sharedQueue, int size) {
		this.sharedQueue = sharedQueue;
		this.SIZE = size;
	}

	@Override
	public void run() {
		while (true) {
			try {
				
				// wait if queue is empty
				while (sharedQueue.isEmpty()) {
					synchronized (sharedQueue) {
						System.out.println("Queue is empty " + Thread.currentThread().getName() + " is waiting , size: " + sharedQueue.size());
						sharedQueue.wait();
					}
				}

				// Otherwise consume element and notify waiting producer
				synchronized (sharedQueue) {
					System.out.println("Consumed: " + (Integer) sharedQueue.remove(0));
					sharedQueue.notifyAll();
					
				}
				
				//Thread.sleep(5);
			} catch (InterruptedException ex) {
				Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
	}

}
