public class EchoClient implements Runnable {
    private String serverAddress;
    private int port;

    public EchoClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    @Override
    public void run() {
        Broker clientBroker = new Broker("Client");
        Channel channel = clientBroker.connect(serverAddress, port);
        if (channel != null) {
            byte[] dataToSend = new byte[255];
            for (int i = 0; i < 255; i++) {
                dataToSend[i] = (byte) (i + 1);
            }

            channel.write(dataToSend, 0, dataToSend.length);

            byte[] receivedData = new byte[255];
            channel.read(receivedData, 0, receivedData.length);

            for (int i = 0; i < 255; i++) {
                if (receivedData[i] != dataToSend[i]) {
                    System.out.println("Data mismatch at byte: " + i);
                }
            }

            channel.disconnect();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Launch multiple clients concurrently
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread(new EchoClient("localhost", 12345));
            clientThread.start();
        }
    }
}
