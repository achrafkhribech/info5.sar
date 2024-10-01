public class EchoServer {
    public static void main(String[] args) {
        BrokerImpl broker = new BrokerImpl("Server");

        // Server accepts connections in a separate thread
        new Thread(() -> {
            while (true) {
                Channel channel = broker.accept(12345);
                if (channel != null) {
                    new Task(broker, () -> {
                        byte[] buffer = new byte[256];
                        int bytesRead;
                        try {
                            while ((bytesRead = channel.read(buffer, 0, buffer.length)) != -1) {
                                channel.write(buffer, 0, bytesRead);  // Echo back the data
                            }
                        } finally {
                            channel.disconnect();
                        }
                    }).start();
                }
            }
        }).start();
    }
}
