package com.google.code.datarain.tests;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
 
public class Cropping extends JPanel
{
    BufferedImage image;
    Dimension size;
    Rectangle clip;
    boolean showClip;
 
    public Cropping(BufferedImage image)
    {
        this.image = image;
        size = new Dimension(image.getWidth(), image.getHeight());
        showClip = false;
    }
 
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int x = (getWidth() - size.width)/2;
        int y = (getHeight() - size.height)/2;
        g2.drawImage(image, x, y, this);
        if(showClip)
        {
            if(clip == null)
                createClip();
            g2.setPaint(Color.red);
            g2.draw(clip);
        }
    }
 
    public void setClip(int x, int y)
    {
        // keep clip within raster
        int x0 = (getWidth() - size.width)/2;
        int y0 = (getHeight() - size.height)/2;
        if(x < x0 || x + clip.width  > x0 + size.width ||
           y < y0 || y + clip.height > y0 + size.height)
            return;
        clip.setLocation(x, y);
        repaint();
    }
 
    public Dimension getPreferredSize()
    {
        return size;
    }
 
    private void createClip()
    {
        clip = new Rectangle(140, 140);
        clip.x = (getWidth() - clip.width)/2;
        clip.y = (getHeight() - clip.height)/2;
    }
 
    private void clipImage()
    {
        BufferedImage clipped = null;
        try
        {
            int w = clip.width;
            int h = clip.height;
            int x0 = (getWidth()  - size.width)/2;
            int y0 = (getHeight() - size.height)/2;
            int x = clip.x - x0;
            int y = clip.y - y0;
            clipped = image.getSubimage(x, y, w, h);
        }
        catch(RasterFormatException rfe)
        {
            System.out.println("raster format error: " + rfe.getMessage());
            return;
        }
        JLabel label = new JLabel(new ImageIcon(clipped));
        JOptionPane.showMessageDialog(this, label, "clipped image",
                                      JOptionPane.PLAIN_MESSAGE);
    }
 
    private JPanel getUIPanel()
    {
        final JCheckBox clipBox = new JCheckBox("show clip", showClip);
        clipBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showClip = clipBox.isSelected();
                repaint();
            }
        });
        JButton clip = new JButton("clip image");
        clip.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                clipImage();
            }
        });
        JPanel panel = new JPanel();
        panel.add(clipBox);
        panel.add(clip);
        return panel;
    }
 
    public static void main(String[] args) throws IOException
    {
        File file = new File("cougar.jpg");
        Cropping test = new Cropping(ImageIO.read(file));
        ClipMover mover = new ClipMover(test);
        test.addMouseListener(mover);
        test.addMouseMotionListener(mover);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new JScrollPane(test));
        f.getContentPane().add(test.getUIPanel(), "South");
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
 
class ClipMover extends MouseInputAdapter
{
    Cropping cropping;
    Point offset;
    boolean dragging;
 
    public ClipMover(Cropping c)
    {
        cropping = c;
        offset = new Point();
        dragging = false;
    }
 
    public void mousePressed(MouseEvent e)
    {
        Point p = e.getPoint();
        if(cropping.clip.contains(p))
        {
            offset.x = p.x - cropping.clip.x;
            offset.y = p.y - cropping.clip.y;
            dragging = true;
        }
    }
 
    public void mouseReleased(MouseEvent e)
    {
        dragging = false;
    }
 
    public void mouseDragged(MouseEvent e)
    {
        if(dragging)
        {
            int x = e.getX() - offset.x;
            int y = e.getY() - offset.y;
            cropping.setClip(x, y);
        }
    }
}