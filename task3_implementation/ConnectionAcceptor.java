import event.Task;

public class ConnectionAcceptor implements Runnable {

	private Binder binder;

	public ConnectionAcceptor(Binder binder) {
		this.binder = binder;
	}

	@Override
	public void run() {
		if (!binder.alreadyAccepted) {
			Task.task().post(this);
			return;
		}
		binder.acceptConnection();
	}

}
