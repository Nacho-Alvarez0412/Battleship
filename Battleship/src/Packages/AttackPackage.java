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
public class AttackPackage extends Package {
    
    public ArrayList<Point> attacks;
    public int target;
    public String type;
    public int origin;
    
    public AttackPackage(ArrayList<Point> attacks,int t,String tipo,int o){
        super("Attack");
        this.attacks=attacks;
        this.target=t;
        this.type=tipo;
        this.origin=o;
    }
}
