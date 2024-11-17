import Task;
import IBroker.AcceptListener;
import IBroker.ConnectListener;
import IChannel;

public class Binder {
	private Task task;
	private ConnectionAccpter connectionAccepter;
	Channel acceptChannel;
	Channel connectChannel;	
	boolean alreadyAccepted;
	AcceptListener listener;
	
	public Binder(int port, AcceptListener listener) {
		task = new Task("Accept Task on " + port);
		alreadyAccepted = false;
		this.listener = listener;
		
		acceptRunnable = new AcceptRunnable(this);
	}
	
	public void bind() {
		task.post(connectionAccepter);
	}
	
	public void kill() {
		IChannel.DisconnectListener disconnectListener = new IChannel.DisconnectListener() {
			@Override
			public void disconnected() {
				// Do nothing
			}
		};
		
		acceptChannel.disconnect( disconnectListener);
		connectChannel.disconnect( disconnectListener);
		task.kill();
	}
	
	private void createChannel() {
        acceptChannel = new Channel();
        connectChannel = new Channel();
		acceptChannel.connect(connectChannel);
		connectChannel.connect(acceptChannel);
	}
	
	void _acceptConnection(ConnectListener listener) {
		if (alreadyAccepted) {
			listener.refused();
		} else {
			alreadyAccepted = true;
			createChannel();
			listener.connected(connectChannel);
		}
	} 
	
	void acceptConnection() {
		this.listener.accepted(acceptChannel);
	}
	
	
}
