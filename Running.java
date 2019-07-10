package runningThreads;

/**
 * 201713074 �ӿ���
 * ������-�޸��� ���α׷�
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.InterruptedException;
import java.util.Random;
import java.lang.Thread;


public class Running extends JFrame {	
	static int player=-1;
	static int rank=0;
	static String [] ranking;
	Running() {		
		setTitle("�޸���!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c=getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
		
		//���� ������� ����� �Է�
		String p=JOptionPane.showInputDialog("�� ���̼� �޸����?(�����Է�)");
		player=Integer.parseInt(p);
		
		//�̸� �ʱ�ȭ..
		for(int i=0;i<this.player;i++) {	MyPanel.x[i]=15; }
		MyPanel.y[0]=80;
		for(int i=1;i<this.player;i++) {	MyPanel.y[i]=MyPanel.y[i-1]+80; }
		this.ranking=new String[this.player];

		//������ ����
		Thread [] t;
		t=new Thread[Running.player];
		ChangeDistance [] runTask=new ChangeDistance[this.player];
		for(int i=0;i<this.player;i++) {
			runTask[i]=new ChangeDistance(i);
			t[i]=new Thread(runTask[i]);
		}
		
		//�޸��� ����
		setContentPane(new MyPanel());
		for(int i=0;i<this.player;i++) {
			t[i].start();
		}

		setSize(800,950);
		setVisible(true);		
	}
	
	synchronized public static void increment(String threadName) {
		Running.ranking[Running.rank]=threadName;
		Running.rank++;
	}

	public static void main(String[] args) {	
		new Running();
		
		//��� ���
		while(true) {
			System.out.println("check");
			if(rank==player) {
				String result="";
				for(int i=0;i<player;i++) {
					result=result.concat((i+1)+"��: ");
					result=result.concat(ranking[i]);
					result=result.concat("\n");		
				}
				JOptionPane.showMessageDialog(null, result, "����", JOptionPane.INFORMATION_MESSAGE);
				break;
			}
		}
		
	}
}



class MyPanel extends JPanel {	
	static int x[]=new int[Running.player];
	static int y[]=new int[Running.player];	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//��� �׸���
		g.setColor(Color.BLACK);
		g.drawString("0m", 85, 75);
		g.drawString("100m", 690, 75);
		g.drawLine(0, 80, 800, 80);//����
		g.drawLine(100, 80, 100, 950);//��߼�
		g.drawLine(700, 80, 700, 950);//��¼�
		
		//����, ����
		for(int i=0;i<Running.player;i++) {
			g.drawLine(0, y[i]+80, 800, y[i]+80);
			g.drawOval(x[i], y[i]+10, 60, 60);
		}		
		repaint();
	}	
}



class ChangeDistance extends Thread {
	int index;
	ChangeDistance(int i){	this.index=i; }

	public void run() {
		int distance=0, go=0;
		Random rand=new Random();
		while(true) {
			try {	Thread.sleep(500); } 
			catch (InterruptedException e) {	e.printStackTrace(); }
			
			//1~10m �����̵�
			go=rand.nextInt(10)+1;			
			
			if((distance+go)<100) {
				distance+=go;
				MyPanel.x[this.index]=(15+distance)*6; //6px=1m (ȭ�����)
			}
			else {
				MyPanel.x[this.index]=710;
				Running.increment(Thread.currentThread().getName());
				//System.out.println(Thread.currentThread().getName()+"�� ��¼��� ��� | "+Running.rank+" | "+Running.player);
				break;
			}
		}
		return;
	}
	
}	
