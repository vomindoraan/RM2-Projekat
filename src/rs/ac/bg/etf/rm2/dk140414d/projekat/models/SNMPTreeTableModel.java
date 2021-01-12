package rs.ac.bg.etf.rm2.dk140414d.projekat.models;

import com.ireasoning.protocol.snmp.SnmpTableModel;
import org.netbeans.swing.outline.DefaultOutlineModel;

import javax.swing.tree.DefaultTreeModel;
import java.util.List;

import static rs.ac.bg.etf.rm2.dk140414d.projekat.Main.HOSTNAMES;

public class SNMPTreeTableModel extends DefaultOutlineModel {
    public SNMPTreeTableModel(List<SnmpTableModel> tableModels) {
        super(
                new DefaultTreeModel(createTree(tableModels)),
                new SNMPRowModel(tableModels),
                false, null);
    }

    private static SNMPTreeNode createTree(List<SnmpTableModel> tableModels) {
        SNMPTreeNode root = new SNMPTreeNode("Routers");
        for (int i = 0; i < HOSTNAMES.length; i++) {
            RouterTreeNode router = new RouterTreeNode(i + 1, HOSTNAMES[i]);
            root.add(router);

            SnmpTableModel tableModel = tableModels.get(i);
            synchronized (tableModel) {
                for (int j = 0; j < tableModel.getRowCount(); j++) {
                    String valueStr = (String) tableModel.getValueAt(j, 0);
                    int ifIndex = Integer.parseInt(valueStr);
                    router.add(new InterfaceTreeNode(ifIndex));
                }
            }
        }
        return root;
    }
}
