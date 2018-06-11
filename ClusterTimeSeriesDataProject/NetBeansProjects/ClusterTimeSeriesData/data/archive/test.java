/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeseriesclustering;

import java.io.File;
import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.DelimitedTextParser;

/**
 *
 * @author surendra
 */
public class test {
    
    public static void main(String[] args) {
        
        String msg = "";
        String distancemsg = "";
        ODAClustering odac = null;
        IncrementalDissimilarityLinkage linkage = null;

        // Initialize the cluster with all variables in one cluster
        // generate maximum allowed cluster diameter (for splitting) and
        // minimum allowed parent and child cluster diameter (for aggregation)
        try {
            
            DelimitedTextParser parser = new DelimitedTextParser();
            parser.setRowNames(false);
            parser.setResponseIndex(new NominalAttribute("Sensor"), -1);
            AttributeDataset train = parser.parse("./data/SensorReadings_init.txt");
            double[][] x = train.toArray(new double[train.size()][]);
            
            int n = x.length;
            double[][] proximity = new double[n][];
            for (int i = 0; i < n; i++) {
                proximity[i] = new double[i + 1];
                for (int k = 0; k < i; k++) {
                    proximity[i][k] = IncrementalDissimilarityLinkage.calculateDistance(x[i], x[k]);
                }
            }
            linkage = new IncrementalDissimilarityLinkage(proximity);
            odac = new ODAClustering(x);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Start clustering new data
        File classDir = new File("./data", "readings");
        if (!classDir.isDirectory()) {
            String msgs = "The input is not a directory";
            System.out.println(msgs); // in case exception gets lost in shell
            throw new IllegalArgumentException(msgs);
        }
        
        String[] trainingFiles = classDir.list();
        
        for (int j = 0; j < trainingFiles.length; ++j) {
            DelimitedTextParser parser = new DelimitedTextParser();
            parser.setRowNames(false);
            parser.setResponseIndex(new NominalAttribute("Sensor"), -1);
            
            try {
                
                AttributeDataset train = parser.parse("./data/readings/SensorReadings_" + j + ".txt");
                double[][] x = train.toArray(new double[train.size()][]);
                
                odac.updateClusterData(x);
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        }
        odac.printTree();
    }
}
