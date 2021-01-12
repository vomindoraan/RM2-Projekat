package rs.ac.bg.etf.rm2.dk140414d.projekat;

import com.ireasoning.protocol.snmp.SnmpTableModel;
import org.netbeans.swing.outline.*;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.InterfaceStatus;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.InterfaceTreeNode;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.RouterTreeNode;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.SNMPTreeNode;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;

import static rs.ac.bg.etf.rm2.dk140414d.projekat.Main.HOSTNAMES;

public class SNMPTreeTable extends Outline {
    private final List<SnmpTableModel> tableModels;
    private final OutlineModel model;

    public SNMPTreeTable(List<SnmpTableModel> tableModels) {
        super();
        this.tableModels = tableModels;

        DefaultTreeModel treeModel = new DefaultTreeModel(createNodes());
        model = DefaultOutlineModel.createOutlineModel(treeModel, new SNMPRowModel());
        setModel(model);

        setDefaultRenderer(InterfaceStatus.class, new InterfaceStatusCellRenderer());
    }

    public void expandAll() {
        SNMPTreeNode root = (SNMPTreeNode) model.getRoot();
        for (Enumeration e = root.children(); e.hasMoreElements(); ) {
            SNMPTreeNode child = (SNMPTreeNode) e.nextElement();
            expandPath(new TreePath(child.getPath()));
        }
    }

    public void tableChanged() {
        tableChanged(new TableModelEvent(model));
    }

    private SNMPTreeNode createNodes() {
        SNMPTreeNode root = new SNMPTreeNode("Routers");
        for (int i = 0; i < HOSTNAMES.length; i++) {
            RouterTreeNode router = new RouterTreeNode(i + 1, HOSTNAMES[i]);
            root.add(router);
            for (int j = 0; j < tableModels.get(i).getRowCount(); j++) {
                String valueStr = (String) tableModels.get(i).getValueAt(j, 0);
                int ifIndex = Integer.parseInt(valueStr);
                router.add(new InterfaceTreeNode(ifIndex));
            }
        }
        return root;
    }

    private class SNMPRowModel implements RowModel {
        // ifDescr, ifType, ifMtu, ifSpeed, ifPhysAddress, ifAdminStatus, ifOperStatus
        // ifLastChange, ifInOctets, ifOutOctets

        @Override
        public int getColumnCount() {
            return 10;
        }

        @Override
        public Class getColumnClass(int column) {
            switch (column) {
//                case 2:  // ifMtu
//                    return Integer.class;
//                case 3:  // ifSpeed
//                    return Long.class;
                case 5:  // ifAdminStatus
                case 6:  // ifOperStatus
                    return InterfaceStatus.class;
                default:
                    return String.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            if (column == 9) column += 5;  // Show ifOutOctets instead of ifInUcastPkts
            return tableModels.get(0).getColumnName(column + 1);
        }

        @Override
        public Object getValueFor(Object node, int column) {
            if (column == 9) column += 5;  // Show ifOutOctets instead of ifInUcastPkts
            return ((SNMPTreeNode) node).getValue(tableModels, column + 1);
        }

        @Override
        public void setValueFor(Object node, int column, Object value) {
            throw new UnsupportedOperationException("Setting values through SNMPRowModel is not supported");
        }

        @Override
        public boolean isCellEditable(Object node, int column) {
            return false;
        }
    }

    private static class InterfaceStatusCellRenderer extends DefaultOutlineCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel ret = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof InterfaceStatus) {
                ret.setText("⬤");
                ret.setForeground(value == InterfaceStatus.UP ? Color.GREEN : Color.RED);
            }
            return ret;
        }
    }
}
