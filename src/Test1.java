import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Test1 extends JFrame implements Runnable, AWTEventListener{
	private Font defaultFont = new Font("", 0, 30);
	private ArrayList<Integer> list = new ArrayList<Integer>();
	private static final int MAXIMUM_NUMBER_LENGTH = 3;
	private static final String EXCEPTS_STRING = "EXC";
	private File file;
	private FileWriter writer;
	private Thread shakingThread;
	//status row
	protected JPanel statusRow;
	protected JLabel statusLabel;
	//row1
	protected JPanel row1;
	protected JLabel notice1, notice2;
	protected JTextField input1, input2;
	//row2
	protected JPanel row2;
	public JLabel resultLabel;
	//row3
	protected JPanel row3;
	protected JButton generate;
	protected JButton clear;
	public Test1() throws IOException {
		super("Random Frame V1.3");
		this.setSize(320,300);
		this.setMinimumSize(new Dimension(320,300));
		this.setMaximumSize(new Dimension(320,300));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);//could be ignored anyway
		this.setLayout(new GridLayout(4,1));
		this.setAlwaysOnTop(true);
		this.file = new File(this.getClass().getResource("").getPath() + "record.txt");
		this.writer = new FileWriter(file,true);
		this.writer.write("\n");
		for(int i = 0;i<50;i++) this.writer.write("#");
		this.writer.write("\n");
		this.writer.write(new Date().toString() + " START {\n");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				try {
					writer.write("}" + new Date().toString() + " PROGRAM EXITS");
					writer.close();
					System.out.println("exit");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("having errs when closing the window, exit!");
					System.exit(1);
				}finally {
					System.exit(0);
				}
			}
		});
		this.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);//add the AWT global listener instand of the KeyListener
	    //main above
		this.row1 = new JPanel();
		this.notice1 = new JLabel("FROM:");
		this.notice2 = new JLabel("TO:");
		this.input1 = new JTextField(3);
		this.input1.setFont(this.defaultFont);
		this.input2 = new JTextField(3);
		this.input2.setFont(this.defaultFont);
		this.row1.setLayout(new FlowLayout(FlowLayout.CENTER));
		row1.add(notice1);
		row1.add(input1);
		row1.add(notice2);
		row1.add(input2);
		add(row1);
		//row1 above
		this.row2 = new JPanel();
		this.resultLabel = new JLabel("result");
		this.resultLabel.setFont(new Font("hop", Font.BOLD, 55));
		this.row2.add(this.resultLabel);
		add(row2);
		//row2 above
		//py deal
		this.row3 = new JPanel() {
			@Override
			public Insets insets() {
				return new Insets(20,0,0,0);
			}
		};
		this.generate = new JButton("GENERATE");
		this.generate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Object src = e.getSource();
				if(src.equals(generate)) {
					generateAndEmployee();
				}
			}
			
		});
		this.clear = new JButton("CLEAR");
		this.clear.addActionListener(new ActionListener() {//clear the inputs

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource().equals(clear)) {
					clearInputs();
				}
			}
		});
		this.row3.add(this.generate);
		this.row3.add(this.clear);
		this.add(row3);
		//row3 above
		setVisible(true);
	}
	private void generateAndEmployee() {
		if(
			isNumberic(input1.getText()) && 
			isNumberic(input2.getText()) &&//if the inputs are number
			!input1.getText().isEmpty() &&
			!input2.getText().isEmpty() &&//is the inputs are empty
			!(Integer.valueOf(input1.getText()) >= Integer.valueOf(input2.getText()))
				) {//generate the number
				try {
					writer.write("	$" + new Date().toString() + " GENERATE" + "{\n		" + "FROM:" + input1.getText() + "\n 		" + "TO:"+ input2.getText() + "\n	}");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				resultLabel.setForeground(Color.BLACK);
				this.shakingThread = new Thread() {
					public void run() {
						input1.setEnabled(false);
						input2.setEnabled(false);
						generate.setEnabled(false);
						clear.setEnabled(false);
						try {resultLabel.setFont(new Font("hop", Font.BOLD, 40));resultLabel.setText(" GENERATING!");Thread.sleep(700);resultLabel.setFont(new Font("hop", Font.BOLD, 55));}catch(Exception e) {e.printStackTrace();}
						for(int i = Integer.valueOf(input1.getText());i<Integer.valueOf(input2.getText());i++) {
							resultLabel.setText(String.valueOf(i));
							System.out.println(i);
							try{Thread.sleep(30);}catch(Exception e) {e.printStackTrace();}
						}
						for(int i = Integer.valueOf(input2.getText());i>=Integer.valueOf(randomFunc(Integer.valueOf(input1.getText()),Integer.valueOf(input2.getText())));i--) {
							resultLabel.setText(String.valueOf(i));
							System.out.println(i);
							try{Thread.sleep(30);}catch(Exception e) {e.printStackTrace();}
						}
						input1.setEnabled(!false);
						input2.setEnabled(!false);
						generate.setEnabled(!false);
						clear.setEnabled(!false);
						generate.setSelected(false);
						clear.transferFocus();//get the first component focused after threading
						try {
							writer.write("RESULT:" + resultLabel.getText() + "\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				shakingThread.start();
		}else if(input1.getText().equals(this.EXCEPTS_STRING)) {//detect the piwai dealing 
			if(!input2.getText().isEmpty() && isNumberic(input2.getText())) {
				if(list.size()<3) {//the topline of the number of the listObjects is 3
					list.add(new Integer(Integer.valueOf(input2.getText())));
					input1.setText("");
					input2.setText("");
					resultLabel.setForeground(Color.BLACK);
					resultLabel.setText("result");
					try {
						this.writer.write("	$" + new Date().toString() + " EXCEPTS: ");
						for(Object i : this.list.toArray()) this.writer.write(i.toString() + " ");
						this.writer.write("\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					clear.transferFocus();
				}else {
					resultLabel.setForeground(Color.RED);
					try {
						this.writer.write("	$" + new Date().toString() + " EXCEPTS_FULL: ");
						for(Object i : this.list.toArray()) this.writer.write(i.toString() + " ");
						this.writer.write("\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else {
			try {
				writer.write("	$" + new Date().toString() + " ERROR{}\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultLabel.setForeground(Color.RED);
			resultLabel.setText("result");//exception occurs
			System.out.println("please retype the numbers!");
		}
	}
	private void clearInputs() {
		try {
			writer.write("	$" + new Date().toString() + " CLEAR{}\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		input1.setText("");
		input2.setText("");
		resultLabel.setForeground(Color.black);
		resultLabel.setText("result");
	}
	private String randomFunc(int min, int max) {//generate the random number
		int i = (int)(Math.random()*max)%(max-min+1) + min;
		if(list.contains(new Integer(i))) {
			long time = new Date().getTime();
			do {
				i = (int)(Math.random()*max)%(max-min+1) + min;
				if(new Date().getTime() - time > 3000) System.exit(ABORT);//if the program cannot generate the number specified in 3 seconds, then exit with the ABORT status
			}while(list.contains(new Integer(i)));
			return String.valueOf(i);
		}
		return String.valueOf(i);
	}
	private boolean isNumberic(String str){//assume whether the string is a number
		  for (int i = 0; i < str.length(); i++){
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   		}
		  }
		  return true;
	}
	@Override
	public Insets insets() {
		return new Insets(50,0,-50,0);
	}
	public static void main(String[] args) throws IOException {
		Test1 test1 = new Test1();//main runnable function
		test1.run();
	}
	@Override
	public void run() {//Check whether the character number of the input is suitable while the program is running
		// TODO Auto-generated method stub
		String 
			str1 = null,
			str2 = null;
		while(true) {		
			if(input1.getText().toCharArray().length > this.MAXIMUM_NUMBER_LENGTH) {
				input1.setText(str1);
			}else {
				str1 = this.input1.getText();
			}
			if(input2.getText().toCharArray().length > this.MAXIMUM_NUMBER_LENGTH) {
				input2.setText(str2);
			}else {
				str2 = this.input2.getText();
			}
			try {Thread.sleep(5);}catch(Exception e) {e.printStackTrace();System.exit(ABORT);};//let the program run slower to prevent it from making the device too heat
		}
	}
	@Override
	public void eventDispatched(AWTEvent e) {
		// TODO Auto-generated method stub
		System.out.println("AZIS");
		if(e.getID() == KeyEvent.KEY_PRESSED) {
			System.out.println("hop");
			KeyEvent event = (KeyEvent) e;
			if(event.getKeyCode() == KeyEvent.VK_ENTER) {//generate and employee the actions to the components if the enter key is pressed
				if(input1.hasFocus()) {
					input1.transferFocus();
				}else if(input2.hasFocus()) {
					if(!input2.getText().isEmpty() && !input1.getText().isEmpty()) {
						generateAndEmployee();
						System.out.println("hop");
						generate.setSelected(true);
					}else {
						clear.transferFocus();
					}
				}else {
					generateAndEmployee();
					System.out.println("hop");
					generate.setSelected(true);
				}
			}
			if(event.getKeyCode() == KeyEvent.VK_SPACE) { 
				clear.setSelected(true);
			}
		}
		if(e.getID() == KeyEvent.KEY_RELEASED) {//get the generate button unselected when the key is released
			KeyEvent event = (KeyEvent) e;
			if(event.getKeyCode() == KeyEvent.VK_ENTER) {
				generate.setSelected(false);
			}
			if(event.getKeyCode() == KeyEvent.VK_SPACE) {
				resultLabel.setForeground(Color.black);
				clearInputs();
				clear.setSelected(false);
				clear.transferFocus();
			}	
		}
	}
}