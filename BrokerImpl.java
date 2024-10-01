import java.util.HashMap;

public class BrokerImpl  extends Broker{
    BrokerManager bm;

    HashMap<Integer, RendezVous> accepts;

    // Constructor to initialize the broker's name
    public BrokerImpl(String name) {
        super(name);
        accepts = new HashMap<Integer, RendezVous>();
        bm = BrokerManager.getInstance();
        bm.add(this);
    }

    // Method to accept a connection on a given port
    @Override
    public Channel connect(String name, int port) {
        BrokerImpl b = (BrokerImpl) bm.get(name);
        if (b == null) {
            return null;
        }
        return b._connect(this, port);
    }

    // Method to connect to a given broker by name and port
    @Override
    public Channel accept(int port) {
        RendezVous rdv = null;
        synchronized (accepts) {
            rdv = accepts.get(port);
            if (rdv != null) {
                throw new IllegalStateException("Port " + port + " already accepting ...");
            }
            rdv = new RendezVous();
            accepts.put(port, rdv);
            accepts.notifyAll();
        Channel ch;
        ch = rdv.accept(this, port);
        return ch;
        }
    }

    // Method to connect to a given broker by name and port
    private Channel _connect(BrokerImpl b, int port) {
        RendezVous rdv = null;
        synchronized (accepts) {
            rdv = accepts.get(port);
            while(rdv == null) {
                try {
                    accepts.wait();
                } catch (InterruptedException e) {
                    //nothing to do here 
                }
                rdv = accepts.get(port);
            }
            accepts.remove(port);
        }
        return rdv.connect(b, port);
    }

}
