/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Bag;
import ws3dproxy.model.Creature;
import ws3dproxy.model.World;

/**
 *
 * @author rgudwin
 */
public class Environment {
    
    public String host="localhost";
    public int port = 4011;
    public String robotID="r0";
    public Creature c = null;
    public Bag b;
    
    public Environment() {
          WS3DProxy proxy = new WS3DProxy();
          try {   
             World w = World.getInstance();
             w.reset();
             World.createFood(0, 350, 75);
             World.createFood(0, 100, 220);
             World.createFood(0, 250, 210);
             c = proxy.createCreature(100,450,0);
             b = c.getBag();
             c.updateBag();
             c.start();
             //c.setRobotID("r0");
             //c.startCamera("r0");
             
             
          } catch (CommandExecException e) {
              
          }
          System.out.println("Robot "+c.getName()+" is ready to go.");
		


	}
}
