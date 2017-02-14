import java.util.LinkedList;
import java.util.Queue;

public class WaitNotifyExp {

	public static class Producer implements Runnable {
		private final Queue<Integer> queue;
		private final int MAX_CAPACITY = 5;

		public Producer(Queue<Integer> queue) {
			super();
			this.queue = queue;
		}

		@Override
		public void run() {
			int counter = 0;
			while (true) {
				try {
					produce(counter++);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void produce(int num) throws InterruptedException {
			synchronized (queue) {
				if (queue.size() >= MAX_CAPACITY) {
					System.err.println("Queue is full");
					queue.wait();
				}
				Thread.sleep(200);
				queue.add(num);
				System.out.println("Produced " + num);
				queue.notifyAll();
			}
			Thread.yield();
		}
	}

	public static class Consumer implements Runnable {
		private final Queue<Integer> queue;

		public Consumer(Queue<Integer> queue) {
			super();
			this.queue = queue;
		}

		@Override
		public void run() {
			while (true) {
				try {
					consume();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void consume() throws InterruptedException {
			synchronized (queue) {
				if (queue.isEmpty()) {
					System.err.println("Queue is empty");
					queue.wait();
				}
				Thread.sleep(1000);
				Integer num = queue.poll();
				System.out.println("Consumed " + num);
				queue.notifyAll();
			}
			Thread.yield();
		}
	}

	public static void main(String[] args) {
		Queue<Integer> queue = new LinkedList<>();

		Thread t1 = new Thread(new Producer(queue));
		Thread t2 = new Thread(new Producer(queue));
		Thread t3 = new Thread(new Consumer(queue));
		t1.start();
		t2.start();
		t3.start();
	}
}
