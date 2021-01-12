package rs.ac.bg.etf.rm2.dk140414d.projekat.models;

import com.ireasoning.protocol.snmp.SnmpTableModel;

import java.util.List;

public class InterfaceTreeNode extends SNMPTreeNode {
    private final int interfaceIndex;

    public InterfaceTreeNode(int interfaceIndex) {
        super("Interface " + interfaceIndex);
        this.interfaceIndex = interfaceIndex;
    }

    public int getInterfaceIndex() {
        return interfaceIndex;
    }

    @Override
    public Object getValue(List<SnmpTableModel> tableModels, int column) {
        RouterTreeNode parent = (RouterTreeNode) getParent();
        SnmpTableModel tableModel = tableModels.get(parent.getRouterIndex() - 1);

        synchronized (tableModel) {
            int row = -1;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String valueStr = (String) tableModel.getValueAt(i, 0);
                int ifIndex = Integer.parseInt(valueStr);
                if (ifIndex == interfaceIndex) {
                    row = i;
                    break;
                }
            }
            if (row == -1) {
                return null;
            }

            Object value = tableModel.getValueAt(row, column);
            try {
                return InterfaceStatus.valueOf(((String) value).toUpperCase());
            } catch (IllegalArgumentException | ClassCastException e) {
                return value;
            }
        }
    }
}
