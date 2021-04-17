package individual;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.ScrollPaneConstants;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GUI_Panel extends JPanel {
	private JLabel lblYou;
	private JLabel heading;
	private JScrollPane scrollPane;
	private JTextArea msgHistory;
	private JButton sendBtn;
	private JTextField userMsg;
	private String msg = "";
	DefaultCaret caret;
	
	public GUI_Panel() {
		setBackground(new Color(255, 153, 153));
		setLayout(new MigLayout("", "[35.00px][339.00px,grow][62px]", "[36.00][210.00px][6.00,grow,center][27.00][3.00]"));
		
		heading = new JLabel("COSC 310 - Team 11 - Assignment 3");
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		msgHistory = new JTextArea();
		lblYou = new JLabel("YOU:");
		sendBtn = new JButton("Send");
		caret = (DefaultCaret)msgHistory.getCaret();
		
		panelSetup();
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	private void panelSetup() {
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		add(heading, "cell 1 0,growx,aligny center");
		
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		msgHistory.setEditable(false);
		add(scrollPane, "cell 0 1 3 1,grow");
		msgHistory.setFont(new Font("Monospaced", Font.BOLD, 13));
		msgHistory.setForeground(new Color(0, 0, 0));
		msgHistory.setBackground(new Color(255, 204, 204));
		scrollPane.setViewportView(msgHistory);
		
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		lblYou.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		add(lblYou, "cell 0 3,alignx trailing,growy");
		
		userMsg = new JTextField();
		userMsg.setBackground(new Color(255, 204, 204));
		add(userMsg, "cell 1 3,grow");
		userMsg.setColumns(10);
		
		userMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msg = userMsg.getText();
				addToMsgHistory("YOU: " + msg);
				userMsg.setText(null);
			}
		});
		
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msg = userMsg.getText();
				addToMsgHistory("YOU: " + msg);
				userMsg.setText(null);
			}
		});
		sendBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		sendBtn.setBackground(new Color(255, 102, 102));
		add(sendBtn, "cell 2 3,grow");
	}
	
	public void addToMsgHistory(String s) {
		this.msgHistory.append(s + "\n");
	}
}
