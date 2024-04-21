package yongbi.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Dialog {
	private JPanel panel;
	public static Dialog createDialog(int x, int y, int w, int h) {
		Dialog d = new Dialog();
		d.getPanel().setBackground(Color.WHITE);
		d.getPanel().setBounds(x, y, w, h);
		d.getPanel().setBorder(new LineBorder(Color.BLUE, 4));
		return d;
	}
	private Dialog() {
		setPanel(new JPanel());
	}
	public JPanel getPanel() {
		return panel;
	}
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
}
