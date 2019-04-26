 package controllers;

import java.io.File;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ImageController
{
	public ImageController()
	{
		
	}
	
	public boolean encode(String path, String original, String ext1, String stegan, String message)
	{
		String file_name = image_path(path,original,ext1);
		BufferedImage image_orig	= getImage(file_name);
		BufferedImage image = user_space(image_orig);
		image = add_text(image,message);
		
		return(setImage(image,new File(image_path(path,stegan,"png")),"png"));
	}
	
	public String decode(String path, String name)
	{
		byte[] decode;
		try
		{
			BufferedImage image  = user_space(getImage(image_path(path,name,"png")));
			decode = decode_text(get_byte_data(image));
			return(new String(decode));
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
				"There is no hidden message in this image!","Error",
				JOptionPane.ERROR_MESSAGE);
			return "";
		}
	}
	
	private String image_path(String path, String name, String ext)
	{
		return path + "/" + name + "." + ext;
	}
	
	private BufferedImage getImage(String f)
	{
		BufferedImage 	image	= null;
		File 		file 	= new File(f);
		
		try
		{
			image = ImageIO.read(file);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, 
				"Image could not be read!","Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}
	
	private boolean setImage(BufferedImage image, File file, String ext)
	{
		try
		{
			file.delete();
			ImageIO.write(image,ext,file);
			return true;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
				"File could not be saved!","Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	private BufferedImage add_text(BufferedImage image, String text)
	{
		byte img[]  = get_byte_data(image);
		byte msg[] = text.getBytes();
		byte len[]   = bit_conversion(msg.length);
		try
		{
			encode_text(img, len,  0);
			encode_text(img, msg, 32); 
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
"Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}

	private BufferedImage user_space(BufferedImage image)
	{
		BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D	graphics = new_img.createGraphics();
		graphics.drawRenderedImage(image, null);
		graphics.dispose();
		return new_img;
	}
	
	private byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
	private byte[] bit_conversion(int i)
	{
		byte byte3 = (byte)((i & 0xFF000000) >>> 24);
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16); 
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); 
		byte byte0 = (byte)((i & 0x000000FF)	   );
		return(new byte[]{byte3,byte2,byte1,byte0});
	}
	
	private byte[] encode_text(byte[] image, byte[] addition, int offset)
	{
		if(addition.length + offset > image.length)
		{
			throw new IllegalArgumentException("File not long enough!");
		}
		for(int i=0; i<addition.length; ++i)
		{
			int add = addition[i];
			for(int bit=7; bit>=0; --bit, ++offset)
			{
				int b = (add >>> bit) & 1;
				image[offset] = (byte)((image[offset] & 0xFE) | b );
			}
		}
		return image;
	}
	
	private byte[] decode_text(byte[] image)
	{
		int length = 0;
		int offset  = 32;
		
		for(int i=0; i<32; ++i)
		{
			length = (length << 1) | (image[i] & 1);
		}
		
		byte[] result = new byte[length];
		
		for(int b=0; b<result.length; ++b )
		{
			for(int i=0; i<8; ++i, ++offset)
			{
				result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
			}
		}
		return result;
	}
}
