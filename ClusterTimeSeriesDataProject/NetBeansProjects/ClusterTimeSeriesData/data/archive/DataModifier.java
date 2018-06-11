/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeseriesclustering;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 *
 * @author surendra
 */
public class DataModifier {

    public static void main(String args[]) {
        try {
            File classDir = new File("./data", "archive");
            if (!classDir.isDirectory()) {
                String msg = "The input is not a directory";
                System.out.println(msg); // in case exception gets lost in shell
                throw new IllegalArgumentException(msg);
            }

            String[] trainingFiles = classDir.list();
            for (int j = 0; j < trainingFiles.length; ++j) {
                //System.out.println("Extrancting instances from " + trainingFiles[j]);
                File file = new File(classDir, trainingFiles[j]);
                System.out.println("Parsing file " + trainingFiles[j]);
                Scanner scanner = new Scanner(file);


                FileWriter outStream = new FileWriter("./data/readings/SensorReadings_" + j + ".txt");
                BufferedWriter out = new BufferedWriter(outStream);
                while (scanner.hasNextLine()) {

                    String strLine = scanner.nextLine();
                    if (strLine != null) {
                        String[] words = strLine.split(" ");
                        for (int k = 0; k < words.length; k++) {
                            double val = Double.parseDouble(words[k]);
                            val = val * 50;
                            if (k < words.length - 1) {
                                out.write(String.format("%.2f", val) + " ");
                            } else {
                                out.write(String.format("%.2f", val));
                                //if (k != words.length - 1) {
                                    out.newLine();
                                //}
                            }
                        }
                    }
                }
                out.close();
                scanner.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

}
