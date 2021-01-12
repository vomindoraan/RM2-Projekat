package rs.ac.bg.etf.rm2.dk140414d.projekat.models;

import com.ireasoning.protocol.snmp.SnmpTableModel;
import org.netbeans.swing.outline.DefaultOutlineModel;

import javax.swing.tree.DefaultTreeModel;
import java.util.List;

public class SNMPTreeTableModel extends DefaultOutlineModel {
    private final DefaultTreeModel treeModel;

    public SNMPTreeTableModel(String[] hostnames, List<SnmpTableModel> tableModels) {
        this(new DefaultTreeModel(null), new SNMPRowModel(tableModels));
        treeModel.setRoot(createTree(hostnames, tableModels));
    }

    // This ctor is a workaround for the tree model being private and inaccessible in DefaultOutlineModel
    protected SNMPTreeTableModel(DefaultTreeModel treeModel, SNMPRowModel rowModel) {
        super(treeModel, rowModel, false, null);
        this.treeModel = treeModel;
    }

    private SNMPTreeNode createTree(String[] hostnames, List<SnmpTableModel> tableModels) {
        SNMPTreeNode root = new SNMPTreeNode("Routers");
        for (int i = 0; i < hostnames.length; i++) {
            RouterTreeNode router = new RouterTreeNode(i + 1, hostnames[i]);
            root.add(router);

            SnmpTableModel tableModel = tableModels.get(i);
            addChildren(router, tableModel);

            tableModel.addTableModelListener(e -> {
                router.removeAllChildren();
                addChildren(router, tableModel);
                treeModel.nodeStructureChanged(router);
            });
        }
        return root;
    }

    private void addChildren(RouterTreeNode router, SnmpTableModel tableModel) {
        synchronized (tableModel) {
            for (int j = 0; j < tableModel.getRowCount(); j++) {
                String valueStr = (String) tableModel.getValueAt(j, 0);
                int ifIndex = Integer.parseInt(valueStr);
                router.add(new InterfaceTreeNode(ifIndex));
            }
        }
    }
}
