/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeseriesclustering;

/**
 * Leaf node.
 */
class Leaf extends Node {

    /**
     * The cluster label of the leaf node.
     */
    int y;

    /**
     * Constructor.
     */
    Leaf(double[] x) {
        n = 1;
        System.arraycopy(x, 0, sum, 0, ODAC.d);
    }

    @Override
    void add(double[] x) {
        n++;
        for (int i = 0; i < ODAC.d; i++) {
            sum[i] += x[i];
        }
    }
}
