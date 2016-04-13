package CompuChiqui;

import java.awt.Image;
import java.beans.*;
 /**
  * 
  * @author Nelson Castiblanco
  */
 public class JPanelImageBeanInfo extends SimpleBeanInfo{
     Image icon;
     Image icon32;
     Image iconM;
     Image icon32M;
     
     public JPanelImageBeanInfo(){
         icon = loadImage("/Imagenes/x16.gif");
         icon32 = loadImage("/Imagenes/x32.gif");
         iconM = loadImage("/Imagenes/x16M.gif");
         icon32M = loadImage("/Imagenes/x32M.gif");
     }
     
     @Override
     public Image getIcon(int i){
         switch(i)
         {
         case 1:
             return icon;
 
         case 2:
             return icon32;
 
         case 3:
             return iconM;
 
         case 4:
             return icon32M;
         }
         return null;
     }
 }