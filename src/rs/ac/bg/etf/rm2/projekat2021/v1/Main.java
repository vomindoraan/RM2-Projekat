package rs.ac.bg.etf.rm2.projekat2021.v1;

import com.ireasoning.protocol.snmp.SnmpConst;
import com.ireasoning.protocol.snmp.SnmpSession;
import com.ireasoning.protocol.snmp.SnmpTableModel;
import rs.ac.bg.etf.rm2.projekat2021.v1.models.SNMPTreeTableModel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String TITLE = "РМ2 пројекат – варијанта 1 – dk140414d";

    public static final String[] HOSTNAMES      = {"192.168.10.1", "192.168.20.1", "192.168.30.1"};
    public static final int      SNMP_PORT      = 161;
    public static final String   SNMP_COMMUNITY = "si2019";
    public static final int      SNMP_VERSION   = SnmpConst.SNMPV2;

    public static final int POLL_INTERVAL = 10;
    public static final int TIMEOUT       = 5;

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
    private SNMPTreeTableModel treeTableModel;
    private SNMPTreeTable        treeTable;
    private JScrollPane          scrollPane;
    private JPanel               panel;

    public Main(List<SnmpTableModel> tableModels) {
        this.tableModels = tableModels;
        initComponents();
    }

    private void initComponents() {
        panel = new JPanel(new BorderLayout());

//        JTable table = new JTable(tableModels.get(0));
//        table.setAutoCreateColumnsFromModel(true);
//        JScrollPane sp = new JScrollPane();
//        sp.setViewportView(table);
//        panel.add(sp, BorderLayout.NORTH);

        treeTableModel = new SNMPTreeTableModel(HOSTNAMES, tableModels);
        treeTable = new SNMPTreeTable(treeTableModel);
        treeTable.expandAll();

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(treeTable);
        panel.add(scrollPane, BorderLayout.CENTER);
    }
}
