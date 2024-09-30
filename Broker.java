import java.util.*;

class Broker {
    private String name;
    private Map<Integer, RDV> acceptConnections = new HashMap<>();
    
    public Broker(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public synchronized Channel accept(int port) {
        RendezVous rdv = acceptConnections.computeIfAbsent(port, k -> new RDV());
        return rdv.accept();
    }

    public synchronized Channel connect(String name, int port) {
        RendezVous rdv = acceptConnections.computeIfAbsent(port, k -> new RDV());
        return rdv.connect();
    }
}