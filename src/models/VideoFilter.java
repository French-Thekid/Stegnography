package models;

import java.io.File;

public class VideoFilter extends javax.swing.filechooser.FileFilter{

	public boolean isImageFile(String ext)
	{
		return (ext.equals("mkv")|| ext.equals("avi") || ext.equals("mp4"));
	}
	
	public boolean accept(File f)
	{
	    if (f.isDirectory())
	    {
			return true;
	    }

	    String extension = getExtension(f);
		if (extension.equals("mkv") || extension.equals("avi") || extension.equals("mp4"))
		{
			return true;
		}
		return false;
	}
	
	public String getDescription()
	{
		return "Supported Video Files";
	}
	
	public static String getExtension(File f)
	{
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 &&  i < s.length() - 1) 
		  return s.substring(i+1).toLowerCase();
		return "";
	}	
}
