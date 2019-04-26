package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import controllers.AudioController;
import controllers.VideoController;
import controllers.ImageController;
import models.Audio_Filter;
import models.Image_Filter;
import models.ResizeImage;
import models.VideoFilter;
import models.PlayAudio;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JSeparator;

public class View extends JFrame {

	private JPanel contentPane;
	private View view;
	private ImageController steg = new ImageController();
	private JTextArea textAreaImage1 = new JTextArea();
	private JScrollPane sp1 = new JScrollPane();
	private JPanel panelImage;
	private JTextArea textAreaImage2 = new JTextArea();
	private JScrollPane sp2 = new JScrollPane(textAreaImage2);
	private JPanel panelAudio;
	private JTextArea textAreaAudio1 = new JTextArea();
	private JScrollPane spAudio1 = new JScrollPane();
	private JTextArea textAreaAudio2 = new JTextArea();
	private JScrollPane spAudio2 = new JScrollPane();
	private JTextArea textAreaVideo1 = new JTextArea();
	private JScrollPane spVideo1 = new JScrollPane();
	private JTextArea textAreaVideo2 = new JTextArea();
	private JScrollPane spVideo2 = new JScrollPane();
	private JPanel panelVideo;
	private JPanel panelDefault;
	private AudioController control = new AudioController();
	private String audioFile;
	private PlayAudio playAudio = new PlayAudio();
	private JButton btnDecodeAudio = new JButton("DECODE");
	private JButton btnSelectAudioFile = new JButton("Select Audio file with message");
	private JButton btnMusic = new JButton("Stop Music");
	private static int count = 0;
	
	public View() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1079, 622);
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		
		JButton btnNewButton_1 = new JButton("IMAGE STEGANOGRAPHY");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelImage.setVisible(true);
				panelAudio.setVisible(false);
				panelVideo.setVisible(false);
				panelDefault.setVisible(false);
			}
		});
		
		panelVideo = new JPanel();
		panelVideo.setBounds(298, 77, 714, 460);
		panelVideo.setVisible(false);
		
		panelAudio = new JPanel();
		panelAudio.setBounds(298, 77, 714, 460);
		panelAudio.setBorder(border);
		contentPane.add(panelAudio);
		panelAudio.setBackground(new Color(102, 204, 255,200));
		panelAudio.setVisible(false);
		panelAudio.setLayout(null);
		
		JLabel lblEnterTextTo_1 = new JLabel("Enter message to encode:");
		lblEnterTextTo_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterTextTo_1.setBounds(10, 11, 178, 14);
		panelAudio.add(lblEnterTextTo_1);
		
		spAudio1.setBounds(167, 11, 201, 79);
		panelAudio.add(spAudio1);
		spAudio1.setViewportView(textAreaAudio1);
		
		JButton btnEncodeAudio = new JButton("ENCODE");
		btnEncodeAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textAreaAudio1.getText().equals("")){
					
					JOptionPane.showMessageDialog(view, "Cannot encode an empty message","Empty message prompt", JOptionPane.ERROR_MESSAGE);
				
				}else{
					
					textAreaAudio2.setText(null);
					String save = "save.txt";
					boolean success = control.writeToFile(save, textAreaAudio1.getText());
					
					if(success == true)
					{
						boolean process = control.encode(save);
						if(process==true)
						{
							JOptionPane.showMessageDialog(null, "Successfully encoded message","Success Prompt",JOptionPane.INFORMATION_MESSAGE);
							textAreaAudio1.setText(null);
						}else{
	
						}
					}
				}
			}
		});
		btnEncodeAudio.setBounds(378, 24, 160, 42);
		panelAudio.add(btnEncodeAudio);
		
		JLabel lblAudioImage = new JLabel("");
		lblAudioImage.setBorder(border);
		lblAudioImage.setIcon(new ImageIcon(View.class.getResource("/images/audioImage.png")));
		lblAudioImage.setBounds(47, 101, 631, 249);
		panelAudio.add(lblAudioImage);
		
		btnDecodeAudio.setVisible(false);
		btnDecodeAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectAudioFile.setVisible(true);
				btnDecodeAudio.setVisible(false);
				
				boolean success=control.decode(audioFile);
				
				if(success == true)
				{
					String result;
					try {
						result = control.readFile();
						textAreaAudio2.setEnabled(true);
						textAreaAudio2.setText(result);
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}		
				}
			}
		});
		btnDecodeAudio.setBounds(39, 417, 160, 32);
		panelAudio.add(btnDecodeAudio);
		
		JLabel lblDecodedMessage = new JLabel("Decoded message:");
		lblDecodedMessage.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDecodedMessage.setBounds(291, 373, 135, 14);
		panelAudio.add(lblDecodedMessage);
		spAudio2.setBounds(424, 368, 213, 81);
		panelAudio.add(spAudio2);
		spAudio2.setViewportView(textAreaAudio2);
		
		textAreaAudio2.setEnabled(false);
		
		
		btnSelectAudioFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				chooser.setFileFilter(new Audio_Filter());
				chooser.setCurrentDirectory(workingDirectory);
				int returnVal = chooser.showOpenDialog(View.this);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					File selectedFile = chooser.getSelectedFile();
					audioFile = selectedFile.getAbsolutePath();
				}
				
				btnDecodeAudio.setVisible(true);
				btnSelectAudioFile.setVisible(false);
			}
		});
		btnSelectAudioFile.setBounds(39, 369, 214, 29);
		panelAudio.add(btnSelectAudioFile);
		
		btnMusic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playAudio.endAudio();
			}
		});
		btnMusic.setForeground(new Color(255, 255, 255));
		btnMusic.setBackground(new Color(255, 0, 0));
		btnMusic.setBounds(599, 7, 108, 23);
		panelAudio.add(btnMusic);
		
		JButton btnPauseMusic = new JButton("Pause Music");
		btnPauseMusic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(count == 0){
					playAudio.stop();
					count++;
					btnPauseMusic.setText("Play Music");
				}else if(count == 1){
					playAudio.continuePlaying();
					count--;
					btnPauseMusic.setText("Pause Music");
				}
			}
		});
		btnPauseMusic.setForeground(Color.WHITE);
		btnPauseMusic.setBackground(new Color(0, 191, 255));
		btnPauseMusic.setBounds(599, 43, 108, 23);
		panelAudio.add(btnPauseMusic);
		panelVideo.setBorder(border);
		
		panelVideo.setBackground(new Color(102, 204, 255,200));
		contentPane.add(panelVideo);
		panelVideo.setLayout(null);
		
		JLabel lblEnterMessageTo = new JLabel("Enter message to encode:");
		lblEnterMessageTo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterMessageTo.setBounds(22, 11, 174, 14);
		panelVideo.add(lblEnterMessageTo);
		
		spVideo1.setBounds(196, 5, 199, 79);
		panelVideo.add(spVideo1);
		spVideo1.setViewportView(textAreaVideo1);
		
		JButton btnEncodeMessage = new JButton("Select Video to ENCODE message in");
		btnEncodeMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				textAreaVideo2.setText(null);
				if(textAreaVideo1.getText().equals("")){
					JOptionPane.showMessageDialog(view, "Cannot encode an empty message","Empty message prompt", JOptionPane.ERROR_MESSAGE);
				}else{
					
					JFileChooser chooser = new JFileChooser();
					chooser.setFileFilter(new VideoFilter());
					int returnVal = chooser.showOpenDialog(view);
					if(returnVal == JFileChooser.APPROVE_OPTION)
					{
						File selectedFile = chooser.getSelectedFile();
						String filePath = selectedFile.getAbsolutePath();
						VideoController vc = new VideoController();
						boolean check = vc.encodeMessage(filePath, textAreaVideo1.getText());
						
						if(check == true){
							JOptionPane.showMessageDialog(view, "Successfully encoded message into the video","Success", JOptionPane.INFORMATION_MESSAGE);
							textAreaVideo1.setText(null);
						}else{
							
							JOptionPane.showMessageDialog(view, "Unsuccessful in encoding process", "Error", JOptionPane.ERROR_MESSAGE);
						}
						
					}
				}
				
			}
		});
		btnEncodeMessage.setBounds(437, 24, 252, 28);
		panelVideo.add(btnEncodeMessage);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(View.class.getResource("/images/videoImage.png")));
		lblNewLabel_2.setBounds(53, 96, 612, 264);
		panelVideo.add(lblNewLabel_2);
		
		JLabel lblDecodedMessage_1 = new JLabel("Decoded Message:");
		lblDecodedMessage_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDecodedMessage_1.setBounds(339, 376, 138, 14);
		panelVideo.add(lblDecodedMessage_1);
		
		textAreaVideo2.setEnabled(false);
		spVideo2.setBounds(475, 371, 199, 78);
		panelVideo.add(spVideo2);
		spVideo2.setViewportView(textAreaVideo2);
		
		JButton btnSelectVideoTo = new JButton("Select Video to DECODE message from");
		btnSelectVideoTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new VideoFilter());
				int returnVal = chooser.showOpenDialog(view);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					File selectedFile = chooser.getSelectedFile();
					String decodePath = selectedFile.getAbsolutePath();
					VideoController vc = new VideoController();
					String result = vc.decodeMessage(decodePath);
					if(!result.equals("")){
						JOptionPane.showMessageDialog(view, "Successfully decoded message","Success", JOptionPane.INFORMATION_MESSAGE);
						textAreaVideo2.setEnabled(true);
						textAreaVideo2.setText(result);
					}
				}
			}
		});
		btnSelectVideoTo.setBounds(10, 372, 268, 28);
		panelVideo.add(btnSelectVideoTo);
		
		panelImage = new JPanel();
		panelImage.setBackground(new Color(102, 204, 255,200));
		panelImage.setBounds(298, 77, 714, 460);
		panelImage.setBorder(border);
		contentPane.add(panelImage);
		panelImage.setLayout(null);
		panelImage.setVisible(false);
		
		JLabel lblEnterTextTo = new JLabel("Enter message to encode:");
		lblEnterTextTo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterTextTo.setBounds(30, 12, 168, 14);
		panelImage.add(lblEnterTextTo);
		
		sp1.setBounds(208, 6, 199, 68);
		panelImage.add(sp1);
		sp1.setViewportView(textAreaImage1);
		
		JButton btnSelectImage = new JButton("Select Image to ENCODE message in");
		btnSelectImage.setBounds(417, 22, 250, 28);
		panelImage.add(btnSelectImage);
		JLabel lblPicture = new JLabel("                                                       Image will load here");
		lblPicture.setBounds(80, 85, 573, 285);
		panelImage.add(lblPicture);
		lblPicture.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPicture.setBorder(border);
		lblPicture.setForeground(new Color(0, 0, 0));
		lblPicture.setBackground(Color.BLACK);
		sp2.setBounds(470, 381, 223, 68);
		textAreaImage2.setEnabled(false);
		panelImage.add(sp2);
		
		JLabel lblNewLabel_1 = new JLabel("Decoded message:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(333, 386, 137, 14);
		panelImage.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Select Image to DECODE message from");
		btnNewButton.setBounds(10, 390, 279, 28);
		panelImage.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fc.setFileFilter(new Image_Filter());
				int rv = fc.showOpenDialog(view);
				
				if(rv == JFileChooser.APPROVE_OPTION){
					try{
						
						File f = fc.getSelectedFile();
						String image = f.getPath();
						String statName = f.getName();
						String statPath = f.getPath();
						statPath = statPath.substring(0, statPath.length()-statName.length()-1);
						statName = statName.substring(0, statName.length()-4);
						
						BufferedImage img = ImageIO.read(fc.getSelectedFile());
						ResizeImage ri = new ResizeImage();
						Image img2 = ri.resImage(img);

						lblPicture.setEnabled(true);
						lblPicture.setIcon(new ImageIcon(img2));
						
						String message = steg.decode(statPath, statName);
						
						if(message != ""){
							
							JOptionPane.showMessageDialog(view, "Successfully decoded the message", "Successful", JOptionPane.INFORMATION_MESSAGE);
							textAreaImage2.setEnabled(true);
							textAreaImage2.setText(message);
							
						}else{
							
							JOptionPane.showMessageDialog(view, "Unsuccessful in decoding message from the image", "Unsuccessful", JOptionPane.INFORMATION_MESSAGE);
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			}
		});
		btnSelectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textAreaImage1.getText().equals("") || textAreaImage1 == null){
					JOptionPane.showMessageDialog(view, "Cannot encode an empty message","Empty message prompt", JOptionPane.ERROR_MESSAGE);
				}else{
					textAreaImage2.setText(null);
					JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					fc.setFileFilter(new Image_Filter());
					int rv = fc.showOpenDialog(view);
					
					if(rv == JFileChooser.APPROVE_OPTION)
					{
						try {
							File f = fc.getSelectedFile();
							String message = textAreaImage1.getText();
							String extension = Image_Filter.getExtension(f);
							String name = f.getName();
							String path = f.getPath();
							path = path.substring(0, path.length()-name.length()-1);
							name = name.substring(0, name.length()-4);
													
							BufferedImage img = ImageIO.read(fc.getSelectedFile());
							ResizeImage ri = new ResizeImage();
							Image img2 = ri.resImage(img);
							
							lblPicture.setEnabled(true);
							lblPicture.setIcon(new ImageIcon(img2));
							
							String output = JOptionPane.showInputDialog(view, "Enter file name for image you wish to encode the message in:", "Image name", JOptionPane.YES_NO_CANCEL_OPTION);
							if(output == null || output.equals(""))   
							{
								JOptionPane.showMessageDialog(view,"File name needs to be entered", "Prompt", JOptionPane.INFORMATION_MESSAGE);
								
							}else{
								
							boolean check = steg.encode(path, name, extension, output, message);
							
							if(check == true){
								
								JOptionPane.showMessageDialog(view,"Message was successfully encoded into the image", "Successful", JOptionPane.INFORMATION_MESSAGE);
								textAreaImage1.setText(null);
							}else if(check == false){
								
								JOptionPane.showMessageDialog(view, "Message was not successfully encoded into the image", "Unsuccessful", JOptionPane.INFORMATION_MESSAGE);
							}
							}
							
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

				}
			}
			
		});
		
		panelDefault = new JPanel();
		panelDefault.setBackground(new Color(30, 144, 255,100));
		panelDefault.setBounds(298, 77, 714, 460);
		panelDefault.setBorder(border);
		contentPane.add(panelDefault);
		btnNewButton_1.setBounds(19, 145, 186, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("AUDIO STEGANOGRAPHY");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelImage.setVisible(false);
				panelAudio.setVisible(true);
				panelVideo.setVisible(false);
				panelDefault.setVisible(false);
			}
		});
		btnNewButton_2.setBounds(19, 225, 186, 23);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("VIDEO STAGANOGRAPHY");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelVideo.setVisible(true);
				panelAudio.setVisible(false);
				panelImage.setVisible(false);
				panelDefault.setVisible(false);
			}
		});
		btnNewButton_3.setBounds(19, 305, 186, 23);
		contentPane.add(btnNewButton_3);
		
		JLabel lblSteganography = new JLabel("STEGANOGRAPHY");
		lblSteganography.setFont(new Font("Monotype Corsiva", Font.PLAIN, 50));
		lblSteganography.setForeground(Color.WHITE);
		lblSteganography.setBounds(298, 0, 509, 66);
		contentPane.add(lblSteganography);
		
		JButton btnNewButton_4 = new JButton("EXIT");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(view, "Exiting Program","Exiting",JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		});
		btnNewButton_4.setForeground(Color.WHITE);
		btnNewButton_4.setBackground(Color.RED);
		btnNewButton_4.setBounds(19, 473, 186, 23);
		contentPane.add(btnNewButton_4);
		
		JLabel lblMenu = new JLabel("MENU:");
		lblMenu.setFont(new Font("Monotype Corsiva", Font.PLAIN, 25));
		lblMenu.setForeground(Color.WHITE);
		lblMenu.setBounds(47, 92, 121, 42);
		contentPane.add(lblMenu);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 193, 212, 2);
		contentPane.add(separator);
		separator.setBackground(Color.WHITE);
		separator.setForeground(Color.WHITE);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.WHITE);
		separator_1.setBackground(Color.WHITE);
		separator_1.setBounds(10, 277, 212, 2);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.WHITE);
		separator_2.setBackground(Color.WHITE);
		separator_2.setBounds(10, 357, 212, 2);
		contentPane.add(separator_2);
		
		JButton btnClose = new JButton("CLOSE");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelImage.setVisible(false);
				panelAudio.setVisible(false);
				panelVideo.setVisible(false);
				panelDefault.setVisible(true);
			}
		});
		btnClose.setBounds(19, 384, 186, 23);
		contentPane.add(btnClose);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.WHITE);
		separator_3.setBackground(Color.WHITE);
		separator_3.setBounds(10, 442, 212, 2);
		contentPane.add(separator_3);
		
		JButton btnClear = new JButton("CLEAR");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textAreaImage1.setText(null);
				textAreaImage2.setText(null);
				textAreaImage2.setEnabled(false);
				textAreaAudio1.setText(null);
				textAreaAudio2.setText(null);
				textAreaAudio2.setEnabled(false);
				textAreaVideo1.setText(null);
				textAreaVideo2.setText(null);
				textAreaVideo2.setEnabled(false);
				lblPicture.setEnabled(false);
				btnSelectAudioFile.setVisible(true);
				btnDecodeAudio.setVisible(false);
			}
		});
		btnClear.setBackground(Color.WHITE);
		btnClear.setBounds(890, 52, 121, 23);
		contentPane.add(btnClear);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(View.class.getResource("/images/background.jpg")));
		lblNewLabel.setBounds(0, 0, 1063, 583);
		contentPane.add(lblNewLabel);
		
	}
}
