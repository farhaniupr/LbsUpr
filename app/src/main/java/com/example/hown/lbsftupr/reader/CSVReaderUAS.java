package com.example.hown.lbsftupr.reader;

import android.content.Context;

import com.example.hown.lbsftupr.annotation.CSVAnnotation;
import com.example.hown.lbsftupr.model.JadwalUCSV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by hown on 26-Feb-18.
 */

public class CSVReaderUAS {

    private static final String SEP = ",";

    public static <T> List<JadwalUCSV> readFile(Context context, File file, Class<T> cl) throws IOException, IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException {

        List<JadwalUCSV> list = new ArrayList<JadwalUCSV>();

        List<Method> methods = new ArrayList<Method>();

        for (int i=0; i<cl.getDeclaredMethods().length; i++) {
            try {
                if (cl.getDeclaredMethods()[i].getName().startsWith("set")) {
                    methods.add(cl.getDeclaredMethods()[i]);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }


        //FileInputStream fileInputStream = new FileInputStream(file);

        //InputStream in = context.getResources().openRawResource(id);

        InputStream in = new FileInputStream(file);

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line = null;
        boolean firstLine = true;

        List<String> labelsName = new ArrayList<String>();

        while ((line=br.readLine())!=null) {
            T object = null;
            StringTokenizer st = new StringTokenizer(line, SEP);
            if (st.countTokens()>0) {
                int count = 0;
                object = cl.newInstance();
                while (st.hasMoreElements()) {
                    String elt = (String) st.nextElement();
                    if (firstLine) {
                        int indexSEP = elt.indexOf("_");
                        if (indexSEP > -1 && indexSEP<elt.length()-2) {
                            //String stringAfterSEP = elt.substring(indexSEP+1, indexSEP+2).toUpperCase() + elt.substring(indexSEP+2).toLowerCase();
                            //elt = elt.substring(0,1).toUpperCase() + elt.substring(1,indexSEP) + stringAfterSEP;
                            labelsName.add(elt);
                        }
                        else {
                            labelsName.add(elt); //.substring(0,1).toUpperCase() + elt.substring(1,elt.length()).toLowerCase());
                        }
                    }
                    else {
                        String label = labelsName.get(count++);
                        for (Method method : methods) {
                            if (/*method.getName().endsWith(*/method.getAnnotation(CSVAnnotation.CSVSetter.class).info().equals(label)) {
                                method.invoke(object, elt);
                                break;
                            }
                        }
                    }
                }
                list.add((JadwalUCSV) object);
            }
            firstLine = false;
        }
        return list.subList(1, list.size());
    }


}
