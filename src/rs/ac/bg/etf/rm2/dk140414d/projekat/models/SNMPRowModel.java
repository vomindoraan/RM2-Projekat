package rs.ac.bg.etf.rm2.dk140414d.projekat.models;

import com.ireasoning.protocol.snmp.SnmpTableModel;
import org.netbeans.swing.outline.RowModel;

import java.util.List;

public class SNMPRowModel implements RowModel {
    // ifDescr, ifType, ifMtu, ifSpeed, ifPhysAddress, ifAdminStatus, ifOperStatus
    // ifLastChange, ifInOctets, ifOutOctets

    private final List<SnmpTableModel> tableModels;

    public SNMPRowModel(List<SnmpTableModel> tableModels) {
        this.tableModels = tableModels;
    }

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
