abstract class Task extends Thread {
<<<<<<< HEAD
    Task(Broker b, Runnable r);
    static Broker getBroker();
}
=======
    protected Broker broker;
    protected Runnable runnable;

    // Constructor that takes a Broker and a Runnable as parameters
    public Task(Broker b, Runnable r) {
        this.broker = b;
        this.runnable = r;
    }

    // Abstract method to get the Broker instance
    public abstract Broker getBroker();
}
>>>>>>> test.implementation
