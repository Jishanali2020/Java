import java .net.*;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.SwingConstants;
//import javax.swing.BorderFactory;
//import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;
import java.util.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

class Client extends JFrame
{

	Socket socket;
	BufferedReader br;
	PrintWriter pw;

	private JLabel heading = new JLabel("Client Area");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto",Font.BOLD,20);


	public Client(){

		try{

			System.out.println("Sending request to server");
			socket = new Socket("127.0.0.1",7777); 
			System.out.println("Connection done..");

			// Data Read.. 
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// Data Write..
			pw = new PrintWriter(socket.getOutputStream());

			createGUI();
			handleEvent();

			startReading();
			// startWriting();

		}
		catch(Exception e){

			e.printStackTrace();
		}
	}

	private void createGUI(){

		this.setTitle("Client Message");
		this.setSize(600,700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);

		heading.setIcon(new ImageIcon("chat.png"));
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));

		messageArea.setEditable(false);
		this.setLayout(new BorderLayout());

		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane = new JScrollPane(messageArea);
		this.add(jScrollPane,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);

		this.setVisible(true);
	}

	private void handleEvent(){

		messageInput.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			/* (non-Javadoc)
			 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

				System.out.println("key Released "+e.getExtendedKeyCode());
				if(e.getKeyCode() == 10){
					String contentToSend = messageInput.getText();
					messageArea.append("Me : "+contentToSend+"\n");
					pw.println(contentToSend);
					pw.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}

		});

	}

	public void startReading(){

		// Lamda Expresson
		Runnable r1=()->{

			System.out.println("Reader Started...");

			try{
				while(true){

					String msg = br.readLine();
					if(msg.equals("exit")){

						System.out.println("Server terminate the chat");
						JOptionPane.showMessageDialog(this,"Server Terminated the chat");
						messageInput.setEnabled(false);
						socket.close();
						break;
					}
					// System.out.println("Server : "+msg);
					messageArea.append("Server :"+msg+"\n");
				}
			}
			catch(Exception e){
				System.out.println("Connection is close");
			}

		};

		new Thread(r1).start();
	}

	public void startWriting(){

		Runnable r2=()->{

			System.out.println("writer started...");

			try{

				while(true && !socket.isClosed()){

					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br1.readLine();
					pw.println(content);
					pw.flush();

					if(content.equals("exit")){

						socket.close();
						break;
					}
				}
				System.out.println("Connection is close");
			}
			catch(Exception e){
				System.out.println("Connection is  close");
			}
		};

		new Thread(r2).start();
	}

	public static void main(String args[]){
		
		System.out.println("This is client..");

		new Client();
	}
}