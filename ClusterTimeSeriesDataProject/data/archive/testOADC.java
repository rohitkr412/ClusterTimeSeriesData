/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeseriesclustering;

import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.DelimitedTextParser;

/**
 *
 * @author surendra
 */
public class testOADC {
    
    public static void main(String[] args){
        ODAC odacCluster = new ODAC(4,2,0.05);
        
        try {
            
            DelimitedTextParser parser = new DelimitedTextParser();
            parser.setRowNames(false);
            parser.setResponseIndex(new NominalAttribute("Sensor"), -1);
            AttributeDataset train = parser.parse("./data/SensorReadings_init.txt");
            double[][] x = train.toArray(new double[train.size()][]);
            
            for(int i=0;i<x.length;i++){
                odacCluster.add(x[i]);
            }
            
            odacCluster.partition(2);
            
            
            



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
