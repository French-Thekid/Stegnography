package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class VideoController {
	
	public static String hide = "hidden.hid";
    public static String newLine = "/n";
	
	
	public static String parseMessage(String text){
        String[] temp = text.split("\n");
        StringBuilder stringBuilder = new StringBuilder(temp[0]);
        int i = 0;
        for(String s : temp){
            if(i > 0) {
                stringBuilder.append("/n");
                stringBuilder.append(s);
            }
            i++;
        }

        return stringBuilder.toString();
    }
	

	public boolean encodeMessage(String path, String message){ //attach a file with text to the alternate data stream of the carrier file
        boolean success = false;
        Runtime runTime = Runtime.getRuntime();
        message = parseMessage(message);
        try {
            runTime.exec(new String[]{"cmd.exe","/c","echo "+message+" > \""+path+":"+hide+"\""});
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
	
	public String decodeMessage(String path){
        File file = new File(path+":"+hide);
        System.out.println(file.exists());
        StringBuilder stringBuilder = new StringBuilder();
        if(file.exists()) {
            try {
                Scanner in = new Scanner(file);
                
                while (in.hasNextLine())
                    stringBuilder.append(in.nextLine());
            
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Error occured","Error",JOptionPane.ERROR_MESSAGE);;
        }

        return unParse(stringBuilder.toString());
    }
	
	public static String unParse(String message){
        String[] s = message.split(newLine);
        StringBuilder stringBuilder = new StringBuilder(s[0]);
        int i = 0;
        for(String st : s){
            if(i > 0) {
                stringBuilder.append("\n");
                stringBuilder.append(st);
            }
            i++;
        }
        return stringBuilder.toString();
    }

}
