package rs.ac.bg.etf.rm2.projekat2021.v1;

import java.awt.Component;
import java.awt.Font;
import java.util.Enumeration;
import java.util.function.IntUnaryOperator;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

public class GUIUtils {
	private GUIUtils() {
	}
	
	public static void setCrossPlatformLookAndFeel() {
		setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	}
	
	public static void setSystemLookAndFeel() {
		setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
	
	public static void setLookAndFeel(String className) {
		try {
			UIManager.setLookAndFeel(className);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			showError("Cannot set " + className.substring(className.lastIndexOf('.') + 1));
		}
	}
	
	public static void setDefaultFont(String name, int style, IntUnaryOperator sizeTransform) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
        	Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                if (name == null) {
                	name = orig.getName();
                }
                if (style < 0) {
                	style = orig.getStyle();
                }
                int size = orig.getSize();
                if (sizeTransform != null) {
                	size = sizeTransform.applyAsInt(size);
                }
                Font font = new Font(name, style, size);
                UIManager.put(key, new FontUIResource(font));
            }
        }
	}
	
	public static void showMessage(String message) {
		showMessage(null, message);
	}
	
	public static void showMessage(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message);
	}
	
	public static void showError(String message) {
		showError(null, message);
	}
	
	public static void showError(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showInformation(String message) {
		showInformation(null, message);
	}
	
	public static void showInformation(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showQuestion(String message) {
		showQuestion(null, message);
	}
	
	public static void showQuestion(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Question", JOptionPane.QUESTION_MESSAGE);
	}
	
	public static void showWarning(String message) {
		showWarning(null, message);
	}
	
	public static void showWarning(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}
}
