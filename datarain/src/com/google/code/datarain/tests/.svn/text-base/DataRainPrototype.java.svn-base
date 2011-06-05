package com.google.code.datarain.tests;

import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jhlabs.image.EdgeFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.ThresholdFilter;

public class DataRainPrototype {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		//create the detector
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("/tmp/real-test.jpg"));
		} catch (IOException e) {
		}
		
		GrayscaleFilter gf = new GrayscaleFilter();

		
		ThresholdFilter th = new ThresholdFilter();
		th.setLowerThreshold(100);
		BufferedImage res = th.filter(img, null);
		
		buffImg2File(res, "/tmp/a.png");
		
		EdgeFilter ef = new EdgeFilter();
		ef.filter(res, null);
		
		buffImg2File(res, "/tmp/b.png");
		
		CannyEdgeDetector detector = new CannyEdgeDetector();

		//adjust its parameters as desired
		detector.setLowThreshold(0.5f);
		detector.setHighThreshold(1f);

		//apply it to an image
		detector.setSourceImage(res);
		detector.process();
		BufferedImage edges = detector.getEdgesImage();
		
		
		
		BufferedImage finalImg = edges.getSubimage(87, 100, 20, 602);
		
		buffImg2File(finalImg, "/tmp/c.png");
		getPixelColor(finalImg);

	}

	private static void buffImg2File(BufferedImage res, String filePath) {
		try {
		    
		    File outputfile = new File(filePath);
		    ImageIO.write(res, "png", outputfile);
		} catch (IOException e) {
		    
		}
	}
	
	private static void getPixelColor(BufferedImage image){
		
		for(int i=0; i<601; i++){
			int c = image.getRGB(0,i);
			int  r = (c & 0x00ff0000) >> 16;
			int  g = (c & 0x0000ff00) >> 8;
			int  b = c & 0x000000ff;
			
			System.out.println("Y="+i+"\tR="+r+" G="+g+" B="+b);
		}
		
		
		
		// and the Java Color is ...
		//Color color = new Color(red,green,blue);
	}

}
