/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omr_retangulos;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.BinaryDilatation;
import Catalano.Imaging.Filters.BlobsFiltering;
import Catalano.Imaging.Filters.Invert;
import Catalano.Imaging.Filters.Mirror;
import Catalano.Imaging.Filters.Resize;
import Catalano.Imaging.Filters.Threshold;
import Catalano.Imaging.Tools.Blob;
import Catalano.Imaging.Tools.BlobDetection;
import Catalano.Imaging.Tools.QuadrilateralTransformation;
import Catalano.Math.Geometry.GrahamConvexHull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author David
 */
public class OMR_Retangulos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        FastBitmap fb = new FastBitmap("gq1.jpeg");
        Resize r = new Resize(fb.getWidth() / 4, fb.getHeight() / 4);
        r.applyInPlace(fb);
        fb.toGrayscale();
        Threshold t = new Threshold(50);
        t.applyInPlace(fb);
        Invert in = new Invert();
        in.applyInPlace(fb);
        BlobsFiltering bf = new BlobsFiltering(20);
        bf.applyInPlace(fb);
        BlobDetection blob = new BlobDetection();
        List<Blob> blobs = blob.ProcessImage(fb);

        FastBitmap fb2 = new FastBitmap("gq1.jpeg");
        r.applyInPlace(fb2);

        Collections.sort(blobs, new Comparator<Blob>() {
            @Override
            public int compare(Blob o1, Blob o2) {
                return Integer.compare(o2.getArea(), o1.getArea());
            }
        });

        ArrayList<IntPoint> pontos = new ArrayList<IntPoint>();
        for (int i = 0; i < 4; i++) {
            pontos.add(blobs.get(i).getCenter());
        }

        GrahamConvexHull gch = new GrahamConvexHull();
        pontos = gch.FindFull(pontos);

        for (IntPoint ponto : pontos) {
            ponto.Swap();
        }

        QuadrilateralTransformation qt = new QuadrilateralTransformation(pontos, 500, 300);
        FastBitmap retorno = qt.ProcessImage(fb2);

        Mirror mi = new Mirror(true, false);
        mi.applyInPlace(retorno);

        retorno.toGrayscale();
        t.setValue(100);
        t.applyInPlace(retorno);

        FiltroCorte fc = new FiltroCorte(26, 26, 179, 266);
        FastBitmap corte = fc.Aplicar(retorno);

        in.applyInPlace(corte);
        BinaryDilatation bd = new BinaryDilatation();
        bd.applyInPlace(corte);

        blobs = blob.ProcessImage(corte);

        int v[] = new int[10];
        int indice=0;
        Blob aux=null;
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                if(aux==null||aux.getArea()<blobs.get(indice).getArea()){
                    aux=blobs.get(indice);
                    v[i]=j;
                }
                indice++;
            }
            aux=null;
        }
        
        char resp[] = new char[10];
        
        for (int i = 0; i < 10; i++) {
            if(v[i]==0) resp[i]='A';
            if(v[i]==1) resp[i]='B';
            if(v[i]==2) resp[i]='C';
            if(v[i]==3) resp[i]='D';
        }
        
        for (int i = 0; i < 10; i++) {
            System.out.println("Questão "+(i+1)+" marcou : "+(resp[i]));
        }

    }

}
