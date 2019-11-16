/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Packages;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author sebasgamboa
 */
public class AttackReceivedPackage extends Package {
    public ArrayList<Point> attacks;
    public int target;
    public String message;
    public int origin;
    public boolean hitLanded;
    
    public AttackReceivedPackage(ArrayList<Point> attacks,int t,String mes,int o,boolean h){
        super("AttackReceived");
        this.attacks=attacks;
        this.target=t;
        this.message=mes;
        this.origin=o;
        this.hitLanded=h;
    }
}
