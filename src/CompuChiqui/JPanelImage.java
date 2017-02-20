package CompuChiqui;
/**
 *
 * @author COMPUCHIQUI
 */
import clases.HiloCamara;
import clases.WebCamInfo;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
 
public class JPanelImage extends JPanel implements MouseListener, MouseMotionListener, DropTargetListener, Runnable{     
    
    private Image imagen;// = new  ImageIcon(getClass().getResource("/Imagenes/fondo.png")).getImage();
    private boolean dibujar = false;

    public int x1,y1,x2,y2,w,h, x, y;
    
    private int camaraID = 0;
    private static VideoCapture video = new VideoCapture();    
    private Mat imagenMat = new Mat();
    
    private JPopupMenu menu = new JPopupMenu();
    
    public JPanelImage() {
        loadLibrary();
        setDropTarget(new DropTarget(this, this));
        addMouseListener( this ); 
    	addMouseMotionListener( this ); 
        menu.addMouseListener(this);
    }
 
    public JPanelImage(String nombreImagen){
        loadLibrary();
        if (nombreImagen != null) {
            imagen = new ImageIcon(getClass().getResource(nombreImagen)).getImage();
        }
    }
 
    public JPanelImage(Image imagenInicial){
        loadLibrary();
        if (imagenInicial != null) {
            imagen = imagenInicial;
        }
    }     
 
    @Override
    public void paint(Graphics g){
        if(imagen != null){
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
        }else{
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
//        g.drawString( "X,Y: [" + x + "," + y, 10, 20 );
        super.paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if(SwingUtilities.isRightMouseButton(e)){
             try{                 
                menu.removeAll();
                JMenuItem subMenu;
                int webCamCounter = 0;
                WebCamInfo camara = null;
                for (Webcam webcam : Webcam.getWebcams()){
                    camara = new clases.WebCamInfo(webcam.getName(), webCamCounter);
                    subMenu = new JMenuItem(camara.toString());
                    subMenu.setIcon(new ImageIcon(getClass().getResource("/Imagenes/camara.png")));
                    menu.add(subMenu);
                    webCamCounter++;
                }
                menu.show(this, e.getPoint().x, e.getPoint().y);
            } catch (WebcamException | HeadlessException ex){
                JOptionPane.showMessageDialog(null, "Ocurrio un error inesperado\n"+ex);
            } 
        }
        if(e.getSource() == menu){
            JOptionPane.showMessageDialog(null, menu);
        }
    }

    @Override
    public void mousePressed(MouseEvent e){
        dibujar = true;
        x1 = e.getX();
        y1 = e.getY();  
        x2=x1;
        y2=y1;
        repaint();      
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("mouseReleased");
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
    
    /*
    *@nombreImagen Asigna una imagem de tipo ImageIcon al JPanel para mostrar.
    */
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
    
    /*
    *@dibujar Habilita la opcion de repintar recuadros dibujados con el mouse.
    */
    public void setDibujable(boolean dibujar){
        this.dibujar = dibujar;
    }

    @Override
    public void run() {
        try {
            if(video.open(camaraID)){
                while(video.read(imagenMat)){
                    System.out.println("leyendo...");
                    setImagen_BufferedImage(toBufferedImage(imagenMat));
                }
            }
        }catch(Exception e){
            cerrarCamara();
        }
    }
    
    public void loadLibrary(){
        try {
            if(System.getProperty("os.arch").contains("64")){
                System.loadLibrary("/opencv/x64/opencv_java300");
                System.out.println("LIBRERIA CARGADA PARA 64 BITS");
            }else{
                System.loadLibrary("/opencv/x86/opencv_java300");
                System.out.println("LIBRERIA CARGADA PARA 86 BITS");                
            }
        } catch (Exception e){            
            ERROR(e, "NO SE ENCUENTRA EL ARCHIVO DLL PARA LA CAMARA.");
        } 
    }
    
    public static void M(String m, String i) {
        JOptionPane.showMessageDialog(null, m, "Aviso", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(JPanelImage.class.getClass().getResource("/Imagenes/"+i)));
    }
    
    public static void ERROR(Exception e, String mensaje){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        JOptionPane.showMessageDialog(null, mensaje+"\n"+sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        escribirFichero(e);
    }
    
    public static void escribirFichero(Exception e){
        File archivo = new File("ERORR.txt");
        BufferedWriter bw = null;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        if(archivo.exists()) {
            try {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(sw.toString()+"\n\n\n");
            } catch (IOException ex) {
                Logger.getLogger(JPanelImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(sw.toString()+"\n\n\n");
            } catch (IOException ex) {
                Logger.getLogger(JPanelImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(JPanelImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cerrarCamara(){
        video.release();        
    }
    
    public int getCamaraID() {
        return camaraID;
    }

    public void setCamaraID(int camaraID) {
        this.camaraID = camaraID;
    }   
    
    public static BufferedImage toBufferedImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = null;
        try{
            image = new BufferedImage(m.cols(), m.rows(), type);
            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            m.get(0, 0, b); // get all the pixels        
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);
        }catch(Exception e){
        }
        return image;
    }

}