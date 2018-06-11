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
public class Variable implements Comparable<Variable>{
    
    private int id=0;
    
    private double[] observations;
    
    public Variable(int name, double[] data){
        id = name;
        observations = data;
    }
    
    public void setObservations(double[] newValues){
        observations = newValues;
    }
    
    public double[] getObservations(){
        return observations;
    }
    
    public int getID(){
        return id;
    }
    
    @Override
    public String toString(){
        return String.valueOf(id);
    }

    @Override
    public int compareTo(Variable o) {
        return this.id - o.id;
    }
    
}
