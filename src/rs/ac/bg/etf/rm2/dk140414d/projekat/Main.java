package rs.ac.bg.etf.rm2.dk140414d.projekat;

import com.ireasoning.protocol.snmp.SnmpConst;
import com.ireasoning.protocol.snmp.SnmpSession;
import com.ireasoning.protocol.snmp.SnmpTableModel;
import org.netbeans.swing.outline.*;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.InterfaceTreeNode;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.RouterTreeNode;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.SNMPTreeNode;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String TITLE = "РМ2 пројекат – варијанта 1 – dk140414d";
    public static final String[] HOSTNAMES = {"192.168.10.1", "192.168.20.1", "192.168.30.1"};
    public static final int SNMP_PORT = 161;
    public static final String SNMP_COMMUNITY = "si2019";
    public static final int SNMP_VERSION = SnmpConst.SNMPV2;
    public static final int POLL_INTERVAL = 10;
    public static final int TIMEOUT = 5;

    public static void main(String... args) throws IOException {
//        GUIUtils.setSystemLookAndFeel();

        SnmpSession.loadMib2();

        List<SnmpTableModel> tableModels = new ArrayList<>(HOSTNAMES.length);
        for (String hostname : HOSTNAMES) {
            SnmpSession session = new SnmpSession(hostname, SNMP_PORT, SNMP_COMMUNITY, SNMP_COMMUNITY, SNMP_VERSION);
            session.setTimeout(TIMEOUT * 1000);
            SnmpTableModel tm = session.snmpGetTable("ifTable");
            tm.setTranslateValue(true);
            tm.startPolling(POLL_INTERVAL);
            tableModels.add(tm);
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new Main(tableModels).panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationByPlatform(true);
            frame.setSize(1800, 900);
//            frame.pack();
            frame.setVisible(true);
        });

        while (true) {
            try {
                Thread.sleep(POLL_INTERVAL * 1000);
            } catch (InterruptedException e) {
                break;
            }
            for (SnmpTableModel tm : tableModels) {
                synchronized (tm) {
                    tm.refreshNow();
                }
            }
        }
    }

    private List<SnmpTableModel> tableModels;
    private OutlineModel outlineModel;
    private Outline outline;
    private JScrollPane scrollPane;
    private JPanel panel;

    public Main(List<SnmpTableModel> tableModels) {
        this.tableModels = tableModels;
        for (SnmpTableModel tm : tableModels) {
            tm.addTableModelListener(e -> outline.tableChanged(new TableModelEvent(outlineModel)));
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(createNodes());
        outlineModel = DefaultOutlineModel.createOutlineModel(treeModel, new SNMPRowModel());

        initComponents();
    }

    private void initComponents() {
        panel = new JPanel(new BorderLayout());

//        JTable table = new JTable(tableModels.get(0));
//        table.setAutoCreateColumnsFromModel(true);
//        JScrollPane sp = new JScrollPane();
//        sp.setViewportView(table);
//        panel.add(sp, BorderLayout.NORTH);

        outline = new Outline(outlineModel);
        outline.setDefaultRenderer(Boolean.class, new SNMPCellRenderer());
        outline.setRootVisible(true);
        // TODO: Expand router nodes

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(outline);
        panel.add(scrollPane, BorderLayout.CENTER);
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
                    return Boolean.class;
                default:
                    return String.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            if (column == 9) column += 5;
            return tableModels.get(0).getColumnName(column + 1);
        }

        @Override
        public Object getValueFor(Object node, int column) {
            if (column == 9) column += 5;
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

    private static class SNMPCellRenderer extends DefaultOutlineCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel ret = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Boolean) {
                ret.setText("⬤");
                ret.setForeground((Boolean) value ? Color.GREEN : Color.RED);
            }
            return ret;
        }
    }
}
