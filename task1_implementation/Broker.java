abstract class Broker {
    // Field to store the broker's name
    protected String name;

    // Constructor to initialize the broker's name
    Broker(String name) {
        this.name = name;
    }

    // Method to get the broker's name
    String getName() {
        return name;
    }

    // Abstract method to accept a connection on a given port
    abstract Channel accept(int port);

    // Abstract method to connect to a given broker by name and port
    abstract Channel connect(String name, int port);
}

   