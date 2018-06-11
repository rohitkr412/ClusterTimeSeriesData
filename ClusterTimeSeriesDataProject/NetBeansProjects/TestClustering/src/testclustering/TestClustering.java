/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testclustering;

import smile.clustering.HierarchicalClustering;
import smile.clustering.linkage.CompleteLinkage;

/**
 *
 * @author Gopinath Rao.R
 */
public class TestClustering {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        double[][] proximity = new double[8][8];
        proximity[0][0]=1.0;
        proximity[0][1]=2.0;
        proximity[1][0]=2.0;
        proximity[1][1]=3.0;
        HierarchicalClustering hc = new HierarchicalClustering(new CompleteLinkage(proximity));
        
        int[] par = hc.partition(1);
        for(int i=0;i<par.length;i++){
            System.out.println(par[i]);
        }
        
        
        
    }
    
}
