/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeseriesclustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ClusterNode {

    private String clusterID = "cID";
    private SortedSet<Variable> members;
    private boolean split = false;
    private int seed1 = 0;
    private int seed2 = 0;
    private double clusterDiameter = Double.MIN_VALUE;

    ClusterNode(String id) {
        clusterID = id;
        members = new TreeSet<>();
    }

    public Set<Variable> getMembers() {
        return members;
    }

    public String getClusterID() {
        return clusterID;
    }

    public double getClusterDiameter() {
        return clusterDiameter;
    }

    public void addMember(Variable x) {
        members.add(x);
        //updateClusterDiameter();
    }

    public void updateClusterDiameter() {
        Variable[] mems = new Variable[members.size()];
        members.toArray(mems);
        Arrays.sort(mems);

        int n = members.size();
        double[][] proximity = new double[n][];
        for (int i = 0; i < n; i++) {
            proximity[i] = new double[i + 1];
            for (int k = 0; k < i; k++) {
                proximity[i][k] = IncrementalDissimilarityLinkage.calculateDistance(mems[i].getObservations(), mems[k].getObservations());
                if (proximity[i][k] > clusterDiameter) {
                    clusterDiameter = proximity[i][k];
                    seed1 = i;
                    seed2 = k;
                }
            }
        }
    }

    public boolean testSplit() {
        Variable[] mems = new Variable[members.size()];
        members.toArray(mems);
        Arrays.sort(mems);
        int n = mems.length;
        double newClusterDiameter = Double.MIN_VALUE;
        double[][] proximity = new double[n][];
        for (int i = 0; i < n; i++) {
            proximity[i] = new double[i + 1];
            for (int k = 0; k < i; k++) {
                proximity[i][k] = IncrementalDissimilarityLinkage.calculateDistance(mems[i].getObservations(), mems[k].getObservations());
                if (proximity[i][k] > newClusterDiameter) {
                    newClusterDiameter = proximity[i][k];
                    seed1 = i;
                    seed2 = k;
                }
            }
        }

        // Check if the new cluster diameter is more than the threshold
        if (newClusterDiameter > IncrementalDissimilarityLinkage.clusterDiameterThreshold) {
            split=true;
            return testAggregate();
        } else {
            return false;
        }
    }

    private boolean testAggregate() {
        if (split) {
            List<ClusterNode> subClusters = split();
            Iterator<ClusterNode> itr = subClusters.iterator();
            while (itr.hasNext()) {
                double diff_a = this.getClusterDiameter() - itr.next().getClusterDiameter();
                double diff_b = this.getClusterDiameter() - itr.next().getClusterDiameter();
                if ((diff_a > IncrementalDissimilarityLinkage.afterSplitInterClusterDistance)
                        && (diff_b > IncrementalDissimilarityLinkage.afterSplitInterClusterDistance)) {
                    split = false;
                    return true;
                } else {
                    split = false;
                    return false;
                }
            }
        }
        return split;
    }

    public List<ClusterNode> split() {
        ClusterNode a = new ClusterNode(this.clusterID + "_1");
        ClusterNode b = new ClusterNode(this.clusterID + "_2");

        Variable[] mems = new Variable[members.size()];
        members.toArray(mems);
        Arrays.sort(mems);

        int n = mems.length;
        for (int i = 0; i < n; i++) {
            if (i == seed1) {
                a.addMember(mems[i]);
                continue;
            }

            if (i == seed2) {
                b.addMember(mems[i]);
                continue;
            }

            if ((i != seed1) || (i != seed2)) {
                double disa = IncrementalDissimilarityLinkage.calculateDistance(mems[seed1].getObservations(), mems[i].getObservations());
                double disb = IncrementalDissimilarityLinkage.calculateDistance(mems[seed2].getObservations(), mems[i].getObservations());
                if (disa < disb) {
                    a.addMember(mems[i]);
                } else {
                    b.addMember(mems[i]);
                }
            }
        }

        List<ClusterNode> subclusters = new ArrayList<ClusterNode>();
        a.updateClusterDiameter();
        b.updateClusterDiameter();
        subclusters.add(a);
        subclusters.add(b);

        return subclusters;
    }

    public ClusterNode aggregate(ClusterNode ck) {
        ClusterNode newCluster = new ClusterNode(clusterID + "_" + ck.getClusterID());
        Iterator<Variable> itr = ck.getMembers().iterator();
        while (itr.hasNext()) {
            newCluster.getMembers().add(itr.next());
        }

        itr = this.getMembers().iterator();
        while (itr.hasNext()) {
            newCluster.getMembers().add(itr.next());
        }

        return newCluster;
    }
    //returns a string format of the class or the object OR the object ID
    public String toString(){
        Variable[] mems = new Variable[members.size()];
        members.toArray(mems);
        Arrays.sort(mems);
        String str="{  ";

        int n = mems.length;
        for (int i = 0; i < n; i++) {
            str+=mems[i].getID()+"  ";
        }
        str+="}";
        
        return str;
        
        //return this.clusterID;
    }
    
//used for comparison bw 2 classes
    @Override 
    public boolean equals(Object obj) {
        ClusterNode cn = (ClusterNode) obj;
        if(this.clusterID.equalsIgnoreCase(cn.getClusterID())){
            return true;
        }else{
            return false;
        }
    }
}
