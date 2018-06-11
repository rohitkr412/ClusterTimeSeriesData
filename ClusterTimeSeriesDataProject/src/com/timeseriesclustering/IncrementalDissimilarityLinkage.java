/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeseriesclustering;

/**
 *
 * @author rohit
 */
public class IncrementalDissimilarityLinkage {

    double[][] proximity;

    // Maximum allowed cluster diameter (for splitting)
    public static double clusterDiameterThreshold = Double.MIN_VALUE;

    // Minimum allowed parent-child cluster diameter differece (for aggregation)
    public static double afterSplitInterClusterDistance = Double.MAX_VALUE;

    /**
     * Constructor.
     *
     * @param proximity The proximity matrix to store the distance measure of
     * dissimilarity. To save space, we only need the lower half of matrix.
     */
    public IncrementalDissimilarityLinkage(double[][] prox) {
        this.proximity = prox;

        clusterDiameterThreshold = findMinimumDistance(prox);
        afterSplitInterClusterDistance = findMaximumDistance(prox);

        // Experiment with different values
        afterSplitInterClusterDistance = clusterDiameterThreshold/2.0;
        clusterDiameterThreshold /= 2.0;
        
        System.out.println("Maximum Allowed Cluster Diameter = "+clusterDiameterThreshold);
        System.out.println("Minimum Allowed Parent-Children Diameter Difference After Split = "+afterSplitInterClusterDistance);
        
   }

    @Override
    public String toString() {
        return "Incremental Dissimilarity linkage";
    }

    private double findMinimumDistance(double[][] prox) {
        int n = proximity.length;
        double minValue = Double.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < i; k++) {
                if (proximity[i][k] < minValue) {
                    minValue = proximity[i][k];
                }
            }
        }
        return minValue;
    }

    private double findMaximumDistance(double[][] prox) {
        int n = proximity.length;
        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < i; k++) {
                if (proximity[i][k] > maxValue) {
                    maxValue = proximity[i][k];
                }
            }
        }
        return maxValue;
    }

    public static double calculateDistance(double[] a, double[] b) {
        double A = 0.0;
        double A2 = 0.0;
        double B = 0.0;
        double B2 = 0.0;
        double P = 0.0;
        double corr = 0.0; // correlation between two time series data a and b
        double RNOMCorr = 0.0; // Rooted Normalized One-Minus-Correlation
        int n = a.length;

        if (a.length == b.length) {
            // Calcualte A, A2, B, B2 and P
            for (int i = 0; i < a.length; i++) {
                A += a[i];
                A2 += java.lang.Math.pow(a[i], 2.0);
                B += b[i];
                B2 += java.lang.Math.pow(b[i], 2.0);
                P += a[i] * b[i];
            }

            // Calculate correlation between two time series data a and b
            double corr_N = (P - ((A * B) / (double) n));
            double corr_D = (java.lang.Math.sqrt((A2 - ((java.lang.Math.pow(A, 2.0)) / n)))) * (java.lang.Math.sqrt((B2 - ((java.lang.Math.pow(B, 2.0)) / n))));
            corr = corr_N / corr_D;

            // Calculate Rooted Normalized One-Minus-Correlation
            RNOMCorr = java.lang.Math.sqrt((1 - corr) / 2.0);
            return RNOMCorr;

        } else {
            System.out.println("Error: The size of time seris data a and b are not same.");
            return -1.0;
        }
    }

}
