package com.example.raphael.keyrcode;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Color.WHITE;



public class Create {


    public static String StoreBitmapImage(Bitmap myImage, int quality)
    {
        FileOutputStream fileOutputStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File dossier = new File(sdCard , "KeyRcode");
        File file = new File(dossier , "image.jpg");
        String path = file.getAbsolutePath();
        try{
            dossier.mkdirs();
            file.createNewFile();
        }catch (IOException e)
        {
            Log.i("debug", "StoreBitmapImage createNewfile IOException "+e.getMessage());
            return path;
        }
        try
        {
            fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            myImage.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        }
        catch (FileNotFoundException e)
        {
            Log.i("debug", "StoreBitmapImage FileNotFoundException");
            return path;
        }
        catch (IOException e)
        {
            Log.i("debug", "StoreBitmapImage IOException");
            return path;
        }

        return path;
    }



     public static Bitmap createQRcode(String Value) {
       String text=Value;
       MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Bitmap bitmap =  createBitmap(100, 100, Bitmap.Config.ARGB_8888);
         try {
             BitMatrix bitMatrix = multiFormatWriter.encode(text,BarcodeFormat.QR_CODE,200,200);
             BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
             bitmap = barcodeEncoder.createBitmap(bitMatrix);
     } catch (com.google.zxing.WriterException e) {
         e.printStackTrace();
        }
         return bitmap;
     }


    public static Bitmap create4QRcode(String fileName, String cle, boolean hg, boolean hd, boolean bg, boolean bd) throws IOException {
       String t1 = "", t2 = "", t3 = "", t4 = "";
        int c = 1;
        for (int i = 0; i < cle.length(); i++) {
            if (c == 1) {
                if (hg) {
                    t1 = t1 + cle.charAt(i);
                } else {
                    i = i - 1;
                    Random r = new Random();
                    t1 = t1 + (char) (r.nextInt(26) + 'a');
                }
            } else if (c == 2) {
                if (hd) {
                    t2 = t2 + cle.charAt(i);
                } else {
                    i = i - 1;
                    Random r = new Random();
                    t2 = t2 + (char) (r.nextInt(26) + 'a');
                }
            } else if (c == 3) {
                if (bg) {
                    t3 = t3 + cle.charAt(i);
                } else {
                    i = i - 1;
                    Random r = new Random();
                    t3 = t3 + (char) (r.nextInt(26) + 'a');
                }
            } else if (c == 4) {
                if (bd) {
                    t4 = t4 + cle.charAt(i);
                } else {
                    i = i - 1;
                    Random r = new Random();
                    t4 = t4 + (char) (r.nextInt(26) + 'a');
                }
            }


            if (c >= 4) {
                c = 1;
            } else c++;
        }
        Bitmap bmhg=createQRcode(t1);
        Bitmap bmhd=createQRcode(t2);
        Bitmap bmbg=createQRcode(t3);
        Bitmap bmbd=createQRcode(t4);

        Bitmap result = createBitmap(bmhg.getWidth() * 4, bmhg.getHeight() * 4, Bitmap.Config.ARGB_8888);
        result.eraseColor(WHITE);

        Canvas can = new Canvas();
        can.setBitmap(result);

        can.drawBitmap(bmhg,(float) bmhg.getWidth()/2,(float) bmhg.getHeight()/2 , null );
        can.drawBitmap(bmhd,(float) (5*bmhd.getWidth()/2),(float) bmhd.getHeight()/2 , null );
        can.drawBitmap(bmbg,(float) bmbg.getWidth()/2,(float) (5*bmbg.getHeight()/2) , null );
        can.drawBitmap(bmbd,(float) (5*bmbd.getWidth()/2),(float) (5*bmbd.getHeight()/2), null );




        return result;
    }

}
