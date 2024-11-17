package messages;

import Broker;
import IBroker;
import IChannel;
import IQueueBroker;

public class QueueBroker implements IQueueBroker {
	
	private String name;
	private Broker broker;
	
	public QueueBroker(String string) {
		this.name = string;
		this.broker = new Broker(string);
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public boolean unbind(int port) {
		return broker.unbind(port);
	}

	@Override
	public boolean bind(int port, AcceptListener listener) {
		MyAcceptListener acceptListener = new MyAcceptListener(listener);
		return broker.bind(port, acceptListener);
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		MyConnectListener connectListener = new MyConnectListener(listener);
        return broker.connect(name, port, connectListener);
	}
	
	private class MyAcceptListener implements IBroker.AcceptListener {
		private AcceptListener listener;

		public MyAcceptListener(AcceptListener listener) {
			this.listener = listener;
		}

		@Override
		public void accepted(IChannel queue) {
			listener.accepted(new MessageQueue(queue));
		}
	}
	
	private class MyConnectListener implements IBroker.ConnectListener {
		
		private ConnectListener listener;
		
		public MyConnectListener(ConnectListener listener) {
			this.listener = listener;
		}

		@Override
		public void connected(IChannel queue) {
			listener.connected(new MessageQueue(queue));
		}

		@Override
		public void refused() {
			listener.refused();
		}
	}
	
}
