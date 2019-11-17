/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clients;

import Packages.AttackReceivedPackage;
import Packages.Package;
import Packages.ChatPackage;
import Packages.DeadPackage;
import Packages.GrafoPackage;
import Packages.IDPackage;
import Packages.ShipPackage;
import Packages.TurnMesagePackage;
import Packages.TurnPackage;
import Packages.hitsPackage;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author sebasgamboa
 */
public class ServerListener extends Thread {
    
    public Client client;
    boolean hitLanded=false;
    
    public void init(Client client){
        this.client = client;
    }
    
    @Override
    public void run() {        
        try {
            while (true) {
                ObjectInputStream in = new ObjectInputStream(Client.instancia().socket.getInputStream());
                Package paq = (Package) in.readObject();                
                switch (paq.tipo) {                    
                    case "chat": 
                        ChatPackage chat = (ChatPackage) paq;
                        Client.instancia().window.addMessage(chat.mensaje);
                        break; 
                        
                    case "ID":
                        IDPackage ID = (IDPackage) paq;
                        this.client.setID(ID.id);
                        break;
                        
                    case "Turn":
                        TurnPackage turn = (TurnPackage) paq;
                        if(turn.turn==this.client.id){
                            System.out.println("es mi turno");
                            this.client.window.getEndTurn().setEnabled(true);
                        }
                        break;
                        
                    case "AttackReceived":
                        
                        AttackReceivedPackage AR=(AttackReceivedPackage) paq;
                        String res=AR.message;
                        if(AR.target==this.client.id){
                            for(Point p:AR.attacks){
                                if(this.client.LogicBoard[p.x][p.y]!=0&&this.client.LogicBoard[p.x][p.y]!=7){
                                    this.client.window.board[p.x][p.y].setIcon(
                                            new ImageIcon("/Users/sebasgamboa/Documents/GitHub/Progra Estructuras/Battle Ship/Battleship/Battleship/src/Images/explosion2.png"));
                                    res+=" en ("+p.x+","+p.y+") acertado";
                                    AR.hitLanded=true;
                                    hitsPackage paq2=new hitsPackage(AR.attacks,AR.target,AR.origin);
                                    this.client.enviarPaquete(paq2);
                                    this.client.window.board[p.x][p.y].verticeName.hits-=1;
                                    if(this.client.window.board[p.x][p.y].verticeName.hits==0){
                                        this.client.window.board[p.x][p.y].verticeName.vivo=false;
                                        System.out.println("ded");
                                        this.client.verticesDead+=1;
                                    }
                                    GrafoPackage gr=new GrafoPackage(this.client.grafo,this.client.id);
                                    this.client.enviarPaquete(gr);
                                    if(this.client.verticesDead==this.client.grafo.grafo.size()){
                                        this.client.UsusarioVivo=false;
                                        DeadPackage dead=new DeadPackage(this.client.id);
                                        client.enviarPaquete(dead);
                                    }
                                }
                                else{
                                    res+=" en ("+p.x+","+p.y+") fallido";
                                    AR.hitLanded=false;
                                }
                             }
                        }
                        
                        this.client.window.setBitacoraText(res);
                        break;
                        
                    case "TurnMessage":
                        TurnMesagePackage T=(TurnMesagePackage) paq;
                        this.client.window.setBitacoraText("Es el turno de Player "+T.turn);
                        break;
                        
                    case "ship":
                        ShipPackage s=(ShipPackage) paq;
                        if(s.origin==this.client.id){
                            for(BoardLabel bl:s.discoveries){
                                //System.out.println("si");
                                if(bl.getIcon()!=null){
                                    this.client.window.enemyBoard[bl.i][bl.j].setIcon(bl.getIcon());
                                }
                            }
                            this.client.window.enemyBoard[s.point.x][s.point.y].setIcon(new ImageIcon("/Users/sebasgamboa/Documents/GitHub/Progra Estructuras/Battle Ship/Battleship/Battleship/src/Images/ship.png") {});
                        }
                        
                    case "hits":
                        hitsPackage H=(hitsPackage) paq;
                        if(H.target==1){
                            for(Point hit: H.points){
                                this.client.hitsP1.add(hit);
                            }
                        }
                        else if(H.target==2){
                            System.out.println("entre");
                            for(Point hit: H.points){
                                this.client.hitsP2.add(hit);
                            }
                        }
                        else if(H.target==3){
                            for(Point hit: H.points){
                                this.client.hitsP3.add(hit);
                            }
                        }
                        else if(H.target==4){
                            for(Point hit: H.points){
                                this.client.hitsP4.add(hit);
                            }
                        }
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
