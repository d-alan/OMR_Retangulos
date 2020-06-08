/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omr_retangulos;

import Catalano.Imaging.Color;
import Catalano.Imaging.FastBitmap;

/**
 *
 * @author aluno
 */
public class FiltroCorte {
    
    private int x,y, width, height;
    
    public FiltroCorte(int x,int y, int width, int height ){
        this.x= x;
        this.y= y;
        this.height= height;
        this.width= width;
    }
    
    public FastBitmap Aplicar(FastBitmap fb){
        
        
        if((x+height)>fb.getHeight()||(y+width)>fb.getWidth())
            throw new IllegalArgumentException("Não encaixa na imagem");
        
        FastBitmap fb2;
        
        if(fb.isRGB()){
            
            fb2 = new FastBitmap(width,height);
            
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    int r=fb.getRed(x+i,y+j);
                    int g=fb.getGreen(x+i,y+j);
                    int b=fb.getBlue(x+i, y+j);
                
                    fb2.setRGB(i,j, r,g,b);
                }
            }
        }
        else{
            
            fb2 = new FastBitmap(width,height, FastBitmap.ColorSpace.Grayscale);
            
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    int gray=fb.getGray(x+i,y+j);
                    
                    fb2.setGray(i,j, gray);
                }
            }
        }
        
        return fb2;
            
    }
}
