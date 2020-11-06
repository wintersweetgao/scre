package edu.hfu.scre.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.helpers.DefaultHandler;

public class CheckFileType {
	/**
	 * 
	 * @param file
	 * @return
	 */
    public static String getMimeType(File file) {    
    	if (file.isDirectory()) {         
    		return "the target is a directory";       
    	}        
    	AutoDetectParser parser = new AutoDetectParser();        
    	parser.setParsers(new HashMap<MediaType, Parser>());        
    	Metadata metadata = new Metadata();        
    	metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());  
    	
    	try (InputStream stream = new FileInputStream(file)) {            
    		parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());        
    	}catch (Exception e){            
    		throw new RuntimeException();        
    	}        
    	return metadata.get(HttpHeaders.CONTENT_TYPE);    
    }
    /**
     * 返回图片封装的相关信息
     * @param stream
     * @return
     */
    public static BufferedImage getImgPxSize(InputStream stream){
    	BufferedImage bi = null; 
		try { 
			bi = ImageIO.read(stream); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return bi;
    }
    
    public static String getMimeType(InputStream stream,String fileName) {    
    	AutoDetectParser parser = new AutoDetectParser();        
    	parser.setParsers(new HashMap<MediaType, Parser>());        
    	Metadata metadata = new Metadata();        
    	metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, fileName);  
    	
    	try {            
    		parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());        
    	}catch (Exception e){            
    		throw new RuntimeException();        
    	}        
    	return metadata.get(HttpHeaders.CONTENT_TYPE);    
    }
    /**
     * 	 判断是否是图片     
     * @param file     * @return    
     * @throws TikaException     
     * @throws IOException     
     * @throws SAXException     
     */   
    public static boolean isImage(File file){        
    	String type = getMimeType(file);        
    	Pattern p = Pattern.compile("image/.*");       
    	Matcher m = p.matcher(type);        
    	return m.matches();    
    }
    public static boolean isImage(InputStream stream,String fileName){        
    	String type = getMimeType(stream,fileName);        
    	Pattern p = Pattern.compile("image/.*");       
    	Matcher m = p.matcher(type);        
    	return m.matches();    
    }
}
