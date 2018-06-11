/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeseriesclustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;

import javax.swing.JTree;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Collections;

/**
 *
 * @author surendra
 */
public class ODAClustering {

    List<List<ClusterNode>> clusterTree;

    public double[][] previousProximity;

    ClusterNode root;

    public ODAClustering(double[][] x) {

        clusterTree = new ArrayList<List<ClusterNode>>();
        ClusterNode root = new ClusterNode("root");
        for (int i = 0; i < x.length; i++) {
            root.addMember(new Variable(i, x[i]));
        }
        List<ClusterNode> clusterList = new ArrayList<ClusterNode>();
        root.updateClusterDiameter();
        clusterList.add(root);

        clusterTree.add(clusterList);
    }

    public void updateClusterData(double[][] x) {
        // Update new observed data to the variables of the clusters
        for (int i = 0; i < clusterTree.size(); i++) {
            List<ClusterNode> clusterList = clusterTree.get(i);
            for (int j = 0; j < clusterList.size(); j++) {
                ClusterNode cluster = clusterList.get(j);
                Iterator<Variable> itr = cluster.getMembers().iterator();
                while (itr.hasNext()) {
                    Variable v = itr.next();
                    v.setObservations(x[v.getID()]);
                }
            }
        }

        List<ClusterNode> newClusterList = new ArrayList<ClusterNode>();

        // Add new list of clusters if split occurs
        List<ClusterNode> clusterList = clusterTree.get(clusterTree.size() - 1);
        for (int j = 0; j < clusterList.size(); j++) {
            ClusterNode cluster = clusterList.get(j);
            if (cluster.testSplit()) {
                newClusterList.addAll(cluster.split());
            } else {
                newClusterList.add(cluster);
            }
        }

        if (newClusterList.size() > 0) {
            clusterTree.add(newClusterList);
        }

    }

    public List<List<ClusterNode>> getClusterTree() {
        return this.clusterTree;
    }

    public void printTree() {
        for (int i = 0; i < clusterTree.size(); i++) {
            System.out.println("\nTime Interval : " + i);
            List<ClusterNode> clusterList = clusterTree.get(i);
            for (int j = 0; j < clusterList.size(); j++) {
                ClusterNode cluster = clusterList.get(j);
                System.out.println("\tCluster : " + cluster.getClusterID());
                Iterator<Variable> itr = cluster.getMembers().iterator();

                System.out.print("\t{");
                while (itr.hasNext()) {
                    Variable v = itr.next();
                    System.out.print(v.getID() + ",");
                }
                System.out.println("}");
            }
        }

    }

    public DefaultTreeModel getTree() {

        List<ClusterNode> uniqueClusterList = new ArrayList<>();
        Collections.sort(uniqueClusterList, new Comparator() {

            @Override
            public int compare(Object n1, Object n2) {

                return n2.toString().compareTo(n1.toString());
            }

        });

        for (int i = 0; i < clusterTree.size(); i++) {
            List<ClusterNode> clusterList = clusterTree.get(i);
            for (int j = 0; j < clusterList.size(); j++) {
                ClusterNode cluster = clusterList.get(j);
                if (!uniqueClusterList.contains(cluster)) {
                    uniqueClusterList.add(cluster);
                }

            }
        }
        
        TreeNode treeRoot = new TreeNode(clusterTree.get(0).get(0));
        
        for(int i=1;i<uniqueClusterList.size();i++){
            treeRoot = insertNode(treeRoot,uniqueClusterList.get(i));
            
        }
        
        return new DefaultTreeModel(treeRoot);

    }
    
    private TreeNode insertNode(TreeNode t, ClusterNode c){
        if(t == null){
            return new TreeNode(c);
        }else{
            if(c.getClusterID().contains(t.id+"_1")){
                t.left = insertNode(t.left,c);
                t.add(t.left);

            }else if(c.getClusterID().contains(t.id+"_2")){
                t.right = insertNode(t.right, c);
                t.add(t.right);
            }
        }
        
        return t;
    }

    class TreeNode extends DefaultMutableTreeNode{

        String id = null;
        String value = null;
        TreeNode left = null;
        TreeNode right = null;
        
        public TreeNode(ClusterNode val){
            id = val.getClusterID();
            value = val.toString();
        }
        
        public String toString(){
            return value;
        }
        
        public String getID(){
            return id;
        }
        
        
        
    }

}
