package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import models.Audio_Filter;
import models.StdAudio;
import models.PlayAudio;
import views.View;

public class AudioController {
	private View view;
	private String audio;
	double[] audioFile;
	int maxChar = 40000;
	private PlayAudio pa = new PlayAudio();
	
	public boolean writeToFile(String fileName,String message)
	{
		try{
			FileWriter out = new FileWriter(new File(fileName));
			out.write(message);
			out.flush();
			out.close();
			return true;
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return false;		
	}
	public boolean encode(String fileName)
	{
		boolean success = false;
		char [] contents = new char[maxChar];
		
		try{
			File file = new File(fileName);
			contents = new Scanner(file).useDelimiter("\\Z").next().toCharArray();		
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "Please select Audio file you want the message to be hidden in:\n MUST be 'WAV' file ","File prompt",JOptionPane.INFORMATION_MESSAGE);
		int x = JOptionPane.showConfirmDialog(view, "IS MUSIC PLAYING?","WARNING",JOptionPane.YES_NO_CANCEL_OPTION);
		
		if(x == JOptionPane.YES_OPTION){
			pa.endAudio();
		}
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new Audio_Filter());
		int returnVal = chooser.showOpenDialog(view);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
		
			File selectedFile = chooser.getSelectedFile();
			audio = selectedFile.getAbsolutePath();
			
			pa.play(audio);
			
		}
		audioFile = getAudioFile(audio);
		
		int audioCounter = 0; 
		for (int i = 0; i < contents.length; i++) {
			while ((audioFile[i+audioCounter] + (double)(contents[i])/10000.0) >= 1.0) {audioCounter++;} 
			audioFile[i+audioCounter] += ((double)(contents[i]))/10000.0;				
		}
		
		String outfile = JOptionPane.showInputDialog(view,"Enter a name for the output audio file with hidden message","Output audio file prompt", JOptionPane.YES_NO_CANCEL_OPTION);
		if(outfile.equals("") || outfile == null){
			
			JOptionPane.showMessageDialog(view, "No file name was entered","Empty prompt", JOptionPane.ERROR_MESSAGE);
			
			pa.endAudio();
			
		}else if(!outfile.equals("") || outfile != null){
			if (!outfile.endsWith(".wav")) 
				outfile += ".wav";
			
			success = StdAudio.save(outfile, audioFile);
			StdAudio.close();
		}			
		
	return success;	
	}
	
	public static double[] getAudioFile(String input) {
		double[] tempAudio;
		
			if (!input.endsWith(".wav")) 
				input += ".wav";

			tempAudio = StdAudio.read(input);
		
		return tempAudio;
	}
	
	public boolean decode(String fileName)
	{
		System.gc();
		String outAudio = null;
		if (!fileName.endsWith(".wav")) 
			fileName += ".wav";
		
		JOptionPane.showMessageDialog(null,"Select audio file that was used to encode the message(i.e the original audio file)","Orginal audio file prompt",JOptionPane.INFORMATION_MESSAGE);
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new Audio_Filter());
		int returnVal = chooser.showOpenDialog(view);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
		
			File selectedFile = chooser.getSelectedFile();
			outAudio = selectedFile.getAbsolutePath();
			
		}
		if (!outAudio.endsWith(".wav")) 
			outAudio += ".wav";
		
		double[] audioEncoded = StdAudio.read(fileName);
		StdAudio.play(fileName);
		double[] key = StdAudio.read(outAudio);
		char [] contents = new char[maxChar];
		
		int contentCounter = 0; 
		for (int i = 0; i < key.length; i++) {
			if (!((audioEncoded[i] - key[i] == 0))) { 
				contents[contentCounter] = (char) (Math.round((float) (10000 * ( audioEncoded[i] - key[i] )))); 

				contentCounter++;
			} 
		}	
		String outputName = "outputfile.txt";
		if (!outputName.endsWith(".txt"))
			outputName += ".txt";
			
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputName));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			for (int i = 0; i < contentCounter+1; i++) {
				 writer.write(contents[i]);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.close();
			StdAudio.close();
			 return true;
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return false;
	}
	public String readFile() throws IOException {

		String pathname = "outputfile.txt";
	    File file = new File(pathname);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	}
	
	
		
	
}

