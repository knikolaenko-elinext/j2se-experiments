import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExp {
	public static long state = 0;

	public static class Reader implements Runnable {

		private final ReentrantReadWriteLock lock;

		public Reader(ReentrantReadWriteLock lock) {
			super();
			this.lock = lock;
		}

		@Override
		public void run() {
			while (true) {
				try {
					read();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void read() throws InterruptedException {
			lock.readLock().lock();
			try {
				System.out.println(Thread.currentThread().getName() + ": Reading " + state);
				Thread.sleep(500);
			} finally {
				lock.readLock().unlock();
			}
		}
	}

	public static class Writer implements Runnable {
		private final ReentrantReadWriteLock lock;

		public Writer(ReentrantReadWriteLock lock) {
			super();
			this.lock = lock;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					write();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void write() throws InterruptedException {
			lock.writeLock().lock();
			try {
				state++;
				System.out.println("Writing " + state);
				Thread.sleep(1000);
			} finally {
				lock.writeLock().unlock();
			}
			Thread.yield();
		}
	}

	public static void main(String[] args) {
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);

		Thread t1 = new Thread(new Reader(lock));
		Thread t2 = new Thread(new Reader(lock));
		Thread t3 = new Thread(new Writer(lock));
		t1.setPriority(10);
		t1.setPriority(10);
		t3.setPriority(1);
		t1.start();
		t2.start();
		t3.start();
	}
}
