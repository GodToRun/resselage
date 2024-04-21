package yongbi.client;
import java.awt.BorderLayout;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import pw.kit.GameManager;
import yongbi.protocol.GameProtocol;
import yongbi.protocol.Packet;
import yongbi.protocol.SigninPacket;
import yongbi.protocol.SignupPacket;
public class GameLauncher extends GameManager {
	private static final long serialVersionUID = 6974892L;
	JPanel newpanel, loginpanel;
	Socket client = null;
	protected GameLauncher() {
		super("세시 왕국 게임 런처", new Point(640 + 16, 480 + 38), false);
		splashScreen = false;
		client = new Socket();
		try {
			client.connect(new InetSocketAddress("localhost", GameProtocol.PORT));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ResourceLoader.load();
		Font font = null;
		GraphicsEnvironment ge = null;
	    try{
	      ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	      font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(ResourceLoader.game + "font.ttf"));
	      font = font.deriveFont(18f);
	      ge.registerFont(font);
	    } catch(FontFormatException e){} catch (IOException e){}
		//setCursorIcon(ResourceLoader.get(ResourceLoader.CURSOR_PREFIX), null);
		newpanel = Dialog.createDialog(320-155, 10, 310, 200).getPanel();
		loginpanel = Dialog.createDialog(320-155, 100, 310, 150).getPanel();
		Font ft = font;
		ArrayList<String> btns = new ArrayList<String>();
		ArrayList<ActionListener> lis = new ArrayList<ActionListener>();
		JTextField signup_id = new JTextField(12);
		JTextField signup_name = new JTextField(12);
		JPasswordField signup_pw = new JPasswordField(12);
		JLabel signup_idl = new JLabel("아이디");
		JLabel signup_namel = new JLabel(" 이름");
		JLabel signup_pwl = new JLabel(" 암호");
		JButton signup = new JButton("등록");
		signup_pw.setEchoChar('*');
		signup.setFont(ft);
		signup_id.setFont(ft);
		signup_name.setFont(ft);
		signup_namel.setFont(ft);
		signup_pw.setFont(ft);
		signup_idl.setFont(ft);
		signup_pwl.setFont(ft);
		signup.setBounds(155-50, 145, 100, 30);
		signup_id.setBounds(155-50, 25, 100, 30);
		signup_idl.setBounds(155-50-50, 25, 100, 30);
		signup_name.setBounds(155-50, 65, 100, 30);
		signup_namel.setBounds(155-50-50, 65, 100, 30);
		signup_pw.setBounds(155-50, 105, 100, 30);
		signup_pwl.setBounds(155-50-50, 105, 100, 30);
		signup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Charset.forName("US-ASCII").newEncoder().canEncode(signup_id.getText())) return;
				MessageDigest digest;
				try {
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
					
					
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
				getPanel().remove(newpanel);
			}
		});
		newpanel.add(signup);
		newpanel.add(signup_id);
		newpanel.add(signup_idl);
		newpanel.add(signup_name);
		newpanel.add(signup_namel);
		newpanel.add(signup_pw);
		newpanel.add(signup_pwl);
		
		JTextField signin_id = new JTextField(12);
		JPasswordField signin_pw = new JPasswordField(12);
		JLabel signin_idl = new JLabel("아이디");
		JLabel signin_pwl = new JLabel(" 암호");
		JButton signin = new JButton("로그인");
		JFrame frame = (JFrame)this;
		/*signin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Charset.forName("US-ASCII").newEncoder().canEncode(signin_id.getText())) return;
				
				} catch (IOException | NoSuchAlgorithmException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}
				
				getPanel().remove(loginpanel);
			}
		});*/
		signin_pw.setEchoChar('*');
		signin.setFont(ft);
		signin_id.setFont(ft);
		signin_pw.setFont(ft);
		signin_idl.setFont(ft);
		signin_pwl.setFont(ft);
		signin.setBounds(155-50, 105, 100, 30);
		signin_id.setBounds(155-50, 25, 100, 30);
		signin_idl.setBounds(155-50-50, 25, 100, 30);
		signin_pw.setBounds(155-50, 65, 100, 30);
		signin_pwl.setBounds(155-50-50, 65, 100, 30);
		
		loginpanel.add(signin);
		loginpanel.add(signin_id);
		loginpanel.add(signin_idl);
		loginpanel.add(signin_pw);
		loginpanel.add(signin_pwl);
		
		btns.add("새로하기");
		btns.add("계속하기");
		btns.add("그만하기");
		lis.add(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getPanel().add(newpanel);
			}
		});
		lis.add(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						getPanel().add(loginpanel);
					}
				});
		lis.add(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		int i = 0;
		int y = 0;
		for (String str : btns) {
			JButton newPlay = new JButton(str);
			newPlay.setFont(ft);
			newPlay.setBounds(250, 250 + y, 140, 30);
			//newPlay.setPreferredSize(new Dimension(100, 30));
			newPlay.addActionListener(lis.get(i));
			getPanel().add(newPlay);
			y += 35;
			i++;
		}
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		new GameLauncher();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void MainLoop() {
		super.MainLoop();
	}
	@Override
	public void DrawScreen(Graphics g) {
		g.drawImage(ResourceLoader.get("ui.init"), 0, 0, null);
	}
}
