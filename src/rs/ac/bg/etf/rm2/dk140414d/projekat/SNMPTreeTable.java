package rs.ac.bg.etf.rm2.dk140414d.projekat;

import com.ireasoning.protocol.snmp.SnmpTableModel;
import org.netbeans.swing.outline.DefaultOutlineCellRenderer;
import org.netbeans.swing.outline.Outline;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.InterfaceStatus;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.SNMPTreeNode;
import rs.ac.bg.etf.rm2.dk140414d.projekat.models.SNMPTreeTableModel;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.tree.TreePath;
import java.awt.Color;
import java.awt.Component;
import java.util.Enumeration;
import java.util.List;

public class SNMPTreeTable extends Outline {
    private final SNMPTreeTableModel model;

    public SNMPTreeTable(List<SnmpTableModel> tableModels) {
        super();
        setModel(model = new SNMPTreeTableModel(tableModels));
        setDefaultRenderer(InterfaceStatus.class, new InterfaceStatusCellRenderer());

        for (SnmpTableModel tm : tableModels) {
            tm.addTableModelListener(e -> tableChanged());
        }
    }

    public void tableChanged() {
        tableChanged(new TableModelEvent(model));
    }

    public void expandAll() {
        SNMPTreeNode root = (SNMPTreeNode) model.getRoot();
        for (Enumeration e = root.children(); e.hasMoreElements(); ) {
            SNMPTreeNode child = (SNMPTreeNode) e.nextElement();
            expandPath(new TreePath(child.getPath()));
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
