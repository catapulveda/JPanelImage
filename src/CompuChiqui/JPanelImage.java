package CompuChiqui;

/**
 *
 * @author AUXPLANTA
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
 
public class JPanelImage extends JPanel implements MouseListener, MouseMotionListener{
 
    private Image imagen;// = new ImageIcon(getClass().getResource("/Imagenes/fondo.png")).getImage();
    private boolean dibujar = false;
    private int x1,y1,x2,y2,w,h;
    private int x,y;
 
    public JPanelImage() {
        
        setDropTarget(new DropTarget(this, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                setBorder(new LineBorder(new Color(221, 75, 57, 200), 2));
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
                
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                setBorder(javax.swing.BorderFactory.createEtchedBorder());
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    if (dtde.getDropAction() == 2) {
                        dtde.acceptDrop(dtde.getDropAction());
                        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            dtde.acceptDrop(DnDConstants.ACTION_COPY);
                            List<File> lista = (List<File>)dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                            if(lista.size()==1){                              
                                setImagen_BufferedImage(ImageIO.read(new File(lista.get(0).getAbsolutePath())));
                                setBorder(javax.swing.BorderFactory.createEtchedBorder());
                            }else if(lista.size()>1){
                                JOptionPane.showMessageDialog(null, "SÓLO UNA IMGAEN A LA VEZ.");
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "ÉSTE TIPO DE ARCHIVO NO ES SOPORTADO.");
                        }
                    }
                } catch (Exception e) {
                    
                }
            }
        }));
        
    }
 
    public JPanelImage(String nombreImagen){
        if (nombreImagen != null) {
            imagen = new ImageIcon(getClass().getResource(nombreImagen)).getImage();
        }
    }
 
    public JPanelImage(Image imagenInicial) {
        if (imagenInicial != null) {
            imagen = imagenInicial;
        }
    }
 
    public void setImagen_ImageIcon(ImageIcon nombreImagen) {
        imagen = nombreImagen.getImage();
        repaint();
    }
 
    public void setImagen_Image(Image nuevaImagen) {
        imagen = nuevaImagen;
        repaint();
    }
    
    public void setImagen_BufferedImage(BufferedImage nuevaImagen){
        imagen = nuevaImagen;
        repaint();
    }
    
    public Image getImagen(){
        return imagen;
    }
 
    @Override
    public void paint(Graphics g){
        super.paint(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
        } else {
            setOpaque(true);
        }
        if(dibujar){
            int width = this.x1 - this.x2;
            int height = this.y1 - this.y2;
            w = Math.abs( width );
            h = Math.abs( height );
            x = width < 0 ? x1 : x2;            
            y = height < 0 ? y1 : y2;
            g.drawRect( x, y , w, h );
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dibujar = true;
        x1 = e.getX();
        y1 = e.getY();  
        x2=x1;
        y2=y1;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dibujar = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();     
        x = e.getX();
        y = e.getY();       
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();        
        repaint();
    }

}