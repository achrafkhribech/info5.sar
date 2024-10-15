public class Task extends Thread {
	private BrokerImpl broker;
	private Runnable runnable;

	public Task(BrokerImpl b, Runnable r) {
		this.broker = b;
		this.runnable = r;
		this.start(); // Changed from this.run() to this.start()
	}
	
	
	@Override
	public void run() {
		this.runnable.run();
	}

	
	public BrokerImpl getBroker() {
		return this.broker;
	}
	
	public static Task getTask() {
		Thread currentThread = Thread.currentThread();
		if (currentThread instanceof Task) {
			return ((Task) currentThread);
		}
		throw new RuntimeException("Current thread is not a Task instance");
	}
}
