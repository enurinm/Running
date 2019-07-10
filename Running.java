package runningThreads;

/**
 * 201713074 임예린
 * 쓰레드-달리기 프로그램
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
		setTitle("달린다!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c=getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
		
		//선수 몇명인지 사용자 입력
		String p=JOptionPane.showInputDialog("몇 명이서 달릴까요?(정수입력)");
		player=Integer.parseInt(p);
		
		//미리 초기화..
		for(int i=0;i<this.player;i++) {	MyPanel.x[i]=15; }
		MyPanel.y[0]=80;
		for(int i=1;i<this.player;i++) {	MyPanel.y[i]=MyPanel.y[i-1]+80; }
		this.ranking=new String[this.player];

		//스레드 생성
		Thread [] t;
		t=new Thread[Running.player];
		ChangeDistance [] runTask=new ChangeDistance[this.player];
		for(int i=0;i<this.player;i++) {
			runTask[i]=new ChangeDistance(i);
			t[i]=new Thread(runTask[i]);
		}
		
		//달리기 시작
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
		
		//결과 출력
		while(true) {
			System.out.println("check");
			if(rank==player) {
				String result="";
				for(int i=0;i<player;i++) {
					result=result.concat((i+1)+"등: ");
					result=result.concat(ranking[i]);
					result=result.concat("\n");		
				}
				JOptionPane.showMessageDialog(null, result, "순위", JOptionPane.INFORMATION_MESSAGE);
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
		
		//배경 그리기
		g.setColor(Color.BLACK);
		g.drawString("0m", 85, 75);
		g.drawString("100m", 690, 75);
		g.drawLine(0, 80, 800, 80);//가로
		g.drawLine(100, 80, 100, 950);//출발선
		g.drawLine(700, 80, 700, 950);//결승선
		
		//선수, 라인
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
			
			//1~10m 랜덤이동
			go=rand.nextInt(10)+1;			
			
			if((distance+go)<100) {
				distance+=go;
				MyPanel.x[this.index]=(15+distance)*6; //6px=1m (화면배율)
			}
			else {
				MyPanel.x[this.index]=710;
				Running.increment(Thread.currentThread().getName());
				//System.out.println(Thread.currentThread().getName()+"가 결승선을 통과 | "+Running.rank+" | "+Running.player);
				break;
			}
		}
		return;
	}
	
}	
