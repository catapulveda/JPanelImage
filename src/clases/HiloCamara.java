package clases;

import CompuChiqui.JPanelImage;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.JPanel;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author AUXPLANTA
 */
public class HiloCamara extends Thread{
    
    private int camaraID = 0;
    private static VideoCapture video = new VideoCapture();    
    private Mat imagen = new Mat();
    private JPanelImage panelCamara;
        
    public HiloCamara(){
    }

    public HiloCamara(JPanelImage panelCamara) {
        this.panelCamara = panelCamara;
    }
    
    public HiloCamara(JPanel panelCamara) {
        this.panelCamara = (JPanelImage) panelCamara;
    }
        
    @Override
    public void run(){
        try {
            if(video.open(camaraID)){
                int numCaras = 0;//VARIABLE QUE SE USA PARA CONTAR LAS CARAS DETECTADAS EN EL MOMENTO DE LA DETECCION
                while(video.read(imagen)){                   
                    panelCamara.setImagen_BufferedImage(toBufferedImage(imagen));
                }
            }
        }catch(Exception e){
            cerrarCamara();
        }
    }         
        
    public int getCamaraID() {
        return camaraID;
    }

    public void setCamaraID(int camaraID) {
        this.camaraID = camaraID;
    }        
    
    public BufferedImage getImage(){
        return toBufferedImage(imagen);
    }     
    
    public boolean grab(){
        return video.grab();
    }
    
    public boolean isOpened(){
        return video.isOpened();
    }    
    
    public static void cerrarCamara(){
        video.release();        
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
