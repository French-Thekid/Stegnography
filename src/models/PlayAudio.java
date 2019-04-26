package models;

	import java.io.*;
	import java.net.URL;
	import javax.sound.sampled.*;
	import javax.swing.*;
public class PlayAudio{
	private static Clip clip;
	   // Constructor
	   public PlayAudio() {       
	     
	   }
	   
	   public void play(String path){
		   try
		    {
		        clip = AudioSystem.getClip();
		        clip.open(AudioSystem.getAudioInputStream(new File(path)));
		        clip.start();
		    }
		    catch (Exception exc)
		    {
		        exc.printStackTrace();
		    }
	   }
	   
	   public void stop(){
		   clip.stop();
	   }
	   
	   public void continuePlaying(){
		   clip.start();
	   }
	   
	   public void endAudio(){
		   clip.close();
	   }
	
}
