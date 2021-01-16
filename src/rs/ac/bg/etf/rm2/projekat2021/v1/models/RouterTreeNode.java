package rs.ac.bg.etf.rm2.projekat2021.v1.models;

public class RouterTreeNode extends SNMPTreeNode {
    private final int    routerIndex;
    private final String hostname;

    public RouterTreeNode(int routerIndex, String hostname) {
        super("Router " + routerIndex + " (" + hostname + ")");
        this.routerIndex = routerIndex;
        this.hostname = hostname;
    }

    public int getRouterIndex() {
        return routerIndex;
    }

    public String getHostname() {
        return hostname;
    }
}
