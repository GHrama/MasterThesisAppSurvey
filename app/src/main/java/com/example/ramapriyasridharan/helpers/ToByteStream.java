package com.example.ramapriyasridharan.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by ramapriyasridharan on 03.06.16.
 */
public class ToByteStream {

    // given any object return byte stream
    static public byte[] objectToByte(Object o){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] yourBytes = null;
        try {
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(o);
            } catch (IOException e) {
                e.printStackTrace();
            }
            yourBytes = bos.toByteArray();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        return yourBytes;
    }

    // given byte stream array return the object
    static public Object byteToObject(byte[] array){

        ByteArrayInputStream bis = new ByteArrayInputStream(array);
        ObjectInput in = null;
        Object o = null;

        try {
            try {
                in = new ObjectInputStream(bis);
                o = in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        return o;
    }

}
