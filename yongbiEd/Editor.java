package yongbiEd;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import alchEd.ResourceLoader;
import ginkgo.ts.HeaderEntry;
import yongbi.ts.Tile;
import pw.kit.GameManager;
import yongbi.protocol.MobInfo;
import yongbi.ts.*;

public class Editor extends GameManager implements MouseListener, MouseMotionListener {
	boolean hasCollider = true;
	boolean portalMode = false;
	String selShape = ".tile";
	TsFile tsFile;
	public String mapName = "제목", mapSubname = "부제";
	String fileName, filePath = null;
	int zoom = 1;
	int mx, my;
	int posX, posY;
	int offx = 0, offy = 0;
	int dirx = 0, diry = 0;
	public static final int gap = 32;
	
	PortalInfo pinfo = new PortalInfo();
	String getEtc(String key) {
		for (EtcInfo info : tsFile.tiles.etcs) {
			if (info.key.equals(key)) {
				return info.value;
			}
		}
		return null;
	}
	void setEtc(String key, String value) {
		for (EtcInfo info : tsFile.tiles.etcs) {
			if (info.key.equals(key)) {
				info.value = value;
				return;
			}
		}
		EtcInfo info = new EtcInfo();
		info.key = key;
		info.value = value;
		tsFile.tiles.etcs.add(info);
	}
	protected Editor() {
		super("srrnEd v0.1", new Point(800, 600));
		splashScreen = false;
		ResourceLoader.load();
		tsFile = new TsFile();
		getPanel().addMouseListener(this);
		getPanel().addMouseMotionListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F1) {
			NPCInfo npc = new NPCInfo();
			npc.npcCode = JOptionPane.showInputDialog("Npc code:");
			tsFile.tiles.npcs.add(npc);
		}
		if (e.getKeyCode() == KeyEvent.VK_F2) {
			EntityInfo ei = new EntityInfo();
			ei.entName = JOptionPane.showInputDialog("Entity code:");
			ei.respawnDelay = Integer.parseInt(JOptionPane.showInputDialog("Respawn delay:"));
			ei.maxSpawn = Integer.parseInt(JOptionPane.showInputDialog("Max spawn:"));
			tsFile.tiles.entities.add(ei);
		}
		if (e.getKeyCode() == KeyEvent.VK_F3) {
			portalMode = !portalMode;
		}
		if (e.getKeyCode() == KeyEvent.VK_F9) {
			tsFile.tiles.npcs.clear();
		}
		if (e.getKeyCode() == KeyEvent.VK_F10) {
			tsFile.tiles.entities.clear();
		}
		if (e.getKeyCode() == KeyEvent.VK_F11) {
			tsFile.tiles.portals.clear();
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			if (tsFile.tiles.bg.base == null)
				tsFile.tiles.bg.base = JOptionPane.showInputDialog("background resource name: ");
			if (mapName == null || (mapName != null && mapName.equals("제목")))
				mapName = JOptionPane.showInputDialog("제목을 정해주세요.");
			if (mapSubname == null || (mapSubname != null && mapSubname.equals("부제")))
				mapSubname = JOptionPane.showInputDialog("부제목을 정해주세요.");
			tsFile.tiles.name = mapName;
			setEtc("subname", mapSubname);
			File file = null;
			JFileChooser fileChooser = new JFileChooser();
			if (filePath == null && fileChooser.showSaveDialog(getPanel()) == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				fileName = file.getName();
				filePath = file.getAbsolutePath();
			}
			else if (filePath != null) {
				JOptionPane.showMessageDialog(getPanel(), "File saved to " + filePath + ".");
				file = new File(filePath);
			}
			if (file != null) {
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(file);
					ObjectOutputStream outStream = new ObjectOutputStream(fos);
					outStream.writeObject(tsFile);
					outStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_L) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showOpenDialog(getPanel()) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				fileName = file.getName();
				filePath = file.getAbsolutePath();
				if (file != null) {
					FileInputStream fos;
					try {
						fos = new FileInputStream(file);
						ObjectInputStream inStream = new ObjectInputStream(fos);
						tsFile = (TsFile)inStream.readObject();
						mapName = tsFile.tiles.name;
						mapSubname = getEtc("subname");
						if (tsFile.tiles.bg == null) tsFile.tiles.bg = new Background();
						inStream.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			dirx = -1;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dirx = 1;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			diry = -1;
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			diry = 1;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			dirx = 0;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dirx = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			diry = 0;
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			diry = 0;
		}
	}
	@Override
	protected void MainLoop() {
		super.MainLoop();
		Point pos = getMousePosition();
		if (pos != null) {
			mx = ((pos.x-(32)) + (offx/zoom)) * zoom;
			my = ((pos.y-(32)) + (offy/zoom)) * zoom;
			posX = (mx / (gap)) * (gap);
			posY = (my / (gap)) * (gap);
		}
		offx += dirx * 8;
		offy += diry * 8;
	}
	Tile findTile(int x, int y) {
		Tile tile = null;
		for (Tile t : tsFile.tiles.tiles) {
			if (t.x == x && t.y == y) {
				tile = t;
				break;
			}
		}
		return tile;
	}
	@Override
	protected void DrawScreen(Graphics g) {
		if (tsFile == null) return;
		for (Tile tile : tsFile.tiles.tiles) {
			g.setColor(Color.WHITE);
			g.fillRect((tile.x*gap - offx) / zoom, (tile.y*gap - offy) / zoom, gap / zoom, gap / zoom);
		}
		for (PortalInfo portal : tsFile.tiles.portals) {
			g.setColor(Color.BLUE);
			int x = (portal.point.x*gap - offx);
			int y = (portal.point.y*gap - offy);
			g.fillRect(x / zoom, y / zoom, gap / zoom, gap / zoom);
			g.setColor(Color.WHITE);
			g.drawString("DEP: " + portal.depMap, x, y);
			g.drawString("ARR: " + portal.arrMap, x, y-20);
		}
		g.setColor(Color.BLACK);
		g.drawRect((posX - offx) / zoom, (posY - offy) / zoom, gap / zoom, gap / zoom);
		if (mapName != null) {
			g.drawString(mapName, 200, 35);
			g.drawString(mapName, 201, 35);
		}
		if (fileName != null) {
			g.drawString(fileName, 201, 60);
		}
		
		if (mapSubname != null) {
			g.drawString(mapSubname, 200, 49);
			g.drawString(mapSubname, 201, 49);
		}
		
	}
	void placePortal() {
		String depFile = JOptionPane.showInputDialog("Dep Map File:");
		String depID = JOptionPane.showInputDialog("Dep Map ID:");
		String arrFile = JOptionPane.showInputDialog("Arr Map File:");
		String arrID = JOptionPane.showInputDialog("Arr Map ID:");
		int xdir = Integer.parseInt(JOptionPane.showInputDialog("xdir:"));
		int ydir = Integer.parseInt(JOptionPane.showInputDialog("ydir:"));
		int reqlvl = Integer.parseInt(JOptionPane.showInputDialog("req lvl:"));
		int reqlook = Integer.parseInt(JOptionPane.showInputDialog("req look:"));
		PortalInfo pi = new PortalInfo();
		pi.depMap = depFile;
		pi.depID = depID;
		pi.arrMap = arrFile;
		pi.arrID = arrID;
		pi.xdir = xdir;
		pi.ydir = ydir;
		pi.reqLevel = reqlvl;
		pi.reqLook = reqlook;
		pi.point = new TsPoint();
		pi.point.x = posX / gap;
		pi.point.y = posY / gap;
		tsFile.tiles.portals.add(pi);
	}
	public void place() {
		int tx = posX / gap;
		int ty = posY / gap;
		if (portalMode) {
			placePortal();
			return;
		}
		if (findTile(tx, ty) != null) return;
		Tile tile = new Tile();
		tile.x = tx;
		tile.y = ty;
		tile.hasCollider = true;
		tile.type = 0;
		tsFile.tiles.tiles.add(tile);
	}
	void deletePortal() {
		PortalInfo r = null;
		for (PortalInfo p : tsFile.tiles.portals) {
			if (p.point.x == posX / gap && p.point.y == posY / gap) {
				r = p;
				break;
			}
		}
		if (r != null) {
			tsFile.tiles.portals.remove(r);
		}
	}
	public void delete() {
		if (portalMode) {deletePortal(); return;}
		int tx = posX / gap;
		int ty = posY / gap;
		Tile ft = findTile(tx, ty);
		if (ft == null) return;
		tsFile.tiles.tiles.remove(ft);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e))
			place();
		if (SwingUtilities.isRightMouseButton(e))
			delete();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e))
			place();
		if (SwingUtilities.isRightMouseButton(e))
			delete();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}
