package com.example.raphael.keyrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.ArrayList;

import static android.graphics.Bitmap.createBitmap;

public class read {

    public static String readQRcode(Bitmap bMap){
        String result ="";
        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(),intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            result = reader.decode(bitmap).getText();
        } catch (NotFoundException e) {return "";
        } catch (ChecksumException e) {return "";
        } catch (FormatException e) {return "";
        }
        return result;
    }

    public static String decript(ArrayList<String> code){
        String cle="";
        int keylen=0;
        for(int i=0; i<code.size();i++){
            keylen=keylen+code.get(i).length();
        }
        ArrayList<String> liste = new ArrayList<String>();
        for(int i=0; i<code.size();i++){
            if(code.get(i)!=""){
                liste.add(code.get(i));
            }
        }
        int compt=0;
        for(int i=0; i<keylen; i++){
            int cat = i/liste.size();
            cle=cle+liste.get(compt).charAt(cat);
            compt++;
            if(compt>=liste.size()){
                compt=0;
            }
        }
        return cle;
    }

    public static CharSequence read4QRcode(Bitmap bMap){
        CharSequence result ="";

        Bitmap bmhg= createBitmap(bMap.getWidth(), bMap.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bmhd= createBitmap(bMap.getWidth(), bMap.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bmbg= createBitmap(bMap.getWidth(), bMap.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bmbd= createBitmap(bMap.getWidth(), bMap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas can1 = new Canvas();
        Canvas can2 = new Canvas();
        Canvas can3 = new Canvas();
        Canvas can4 = new Canvas();
        can1.setBitmap(bmhg);
        can2.setBitmap(bmhd);
        can3.setBitmap(bmbg);
        can4.setBitmap(bmbd);

        Rect dst = new Rect(0,0,bMap.getWidth()/2,bMap.getHeight()/2);

        Rect rect1 = new Rect(0,0,bMap.getWidth()/2,bMap.getHeight()/2);
        Rect rect2 = new Rect(bMap.getWidth()/2,0, bMap.getWidth(),bMap.getHeight()/2);
        Rect rect3 = new Rect(0,bMap.getHeight()/2,bMap.getWidth()/2, bMap.getHeight());
        Rect rect4 = new Rect(bMap.getWidth()/2,bMap.getHeight()/2, bMap.getWidth(), bMap.getHeight());

        can1.drawBitmap(bMap, rect1, dst, null);
        can2.drawBitmap(bMap, rect2, dst, null);
        can3.drawBitmap(bMap, rect3, dst, null);
        can4.drawBitmap(bMap, rect4, dst, null);

        String hg = readQRcode(bmhg);
        String hd = readQRcode(bmhd);
        String bg = readQRcode(bmbg);
        String bd = readQRcode(bmbd);

        ArrayList<String> liste = new ArrayList<String>();
        liste.add(hg);
        liste.add(hd);
        liste.add(bg);
        liste.add(bd);

        result = decript(liste);

        return result;
    }




}
