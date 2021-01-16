package rs.ac.bg.etf.rm2.projekat2021.v1.models;

import com.ireasoning.protocol.snmp.SnmpTableModel;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public class SNMPTreeNode extends DefaultMutableTreeNode {
    public SNMPTreeNode() {
    }

    public SNMPTreeNode(Object userObject) {
        super(userObject);
    }

    public Object getValue(List<SnmpTableModel> tableModels, int column) {
        return null;
    }
}
