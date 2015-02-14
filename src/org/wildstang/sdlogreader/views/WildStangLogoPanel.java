package org.wildstang.sdlogreader.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class WildStangLogoPanel extends JPanel {
	
	private static final int PREFERRED_HEIGHT = 100; 
	
	Image wsLogo;
	
	public WildStangLogoPanel() {
		try {
            wsLogo = ImageIO.read(getClass().getResourceAsStream("/wildstang-logo.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		
		setPreferredSize(new Dimension((int) ((double) wsLogo.getWidth(null) * ((double) PREFERRED_HEIGHT / (double) wsLogo.getHeight(null))), PREFERRED_HEIGHT)); 
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Scale the logo to fit in the panel
		double scaleFactor = (double) getHeight() / (double) wsLogo.getHeight(null);
		int imageWidth = (int) ((double) wsLogo.getWidth(null) * scaleFactor);
		int imageHeight = (int) ((double) wsLogo.getHeight(null) * scaleFactor);
		g.drawImage(wsLogo, 0, 0, imageWidth, imageHeight, null);
		System.out.println("panel height: " + getHeight());
	}

}
