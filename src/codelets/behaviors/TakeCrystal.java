/*
 * Copyright (C) 2015 barbara.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import memory.CreatureInnerSense;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Thing;

/**
 *
 * @author barbara
 */
public class TakeCrystal extends Codelet {
    private MemoryObject closestGreenCrystalMO;
	private MemoryObject innerSenseMO;
        private MemoryObject knownGreenCrystalsMO;
	private int reachDistance;
	private MemoryObject handsMO;
        Thing closestGreenCrystal;
        CreatureInnerSense cis;
        List<Thing> known;

	public TakeCrystal(int reachDistance) {
		this.reachDistance=reachDistance;
	}

	@Override
	public void accessMemoryObjects() {
		closestGreenCrystalMO=this.getInput("CLOSEST_GCRYSTAL");
		innerSenseMO=this.getInput("INNER");
		handsMO=this.getOutput("HANDS");
                knownGreenCrystalsMO = this.getOutput("KNOWN_GCRYSTALS");
	}

	@Override
	public void proc() {
                String gcrystalName="";
                closestGreenCrystal = (Thing) closestGreenCrystalMO.getI();
                cis = (CreatureInnerSense) innerSenseMO.getI();
                known = (List<Thing>) knownGreenCrystalsMO.getI();
		//Find distance between closest green crystal and self
		//If closer than reachDistance, eat the apple
		
		if(closestGreenCrystal != null)
		{
			double gcrystalX=0;
			double gcrystalY=0;
			try {
				gcrystalX=closestGreenCrystal.getX1();
				gcrystalY=closestGreenCrystal.getY1();
                                gcrystalName = closestGreenCrystal.getName();
                                

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			double selfX=cis.position.getX();
			double selfY=cis.position.getY();

			Point2D pGCrystal = new Point();
			pGCrystal.setLocation(gcrystalX, gcrystalY);

			Point2D pSelf = new Point();
			pSelf.setLocation(selfX, selfY);

			double distance = pSelf.distance(pGCrystal);
			JSONObject message=new JSONObject();
			try {
				if(distance<reachDistance){ //take it						
					message.put("OBJECT", gcrystalName);
					message.put("ACTION", "PICKUP");
					handsMO.updateInfo(message.toString());
                                        DestroyClosestGreenCrystal();
				}else{
					handsMO.updateInfo("");	//nothing
				}
				
//				System.out.println(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			handsMO.updateInfo("");	//nothing
		}
        //System.out.println("Before: "+known.size()+ " "+known);
        
        //System.out.println("After: "+known.size()+ " "+known);
	//System.out.println("EatClosestApple: "+ handsMO.getInfo());	

	}
        
        @Override
        public void calculateActivation() {
        
        }
        
        public void DestroyClosestGreenCrystal() {
           int r = -1;
           int i = 0;
           synchronized(known) {
             CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);  
             for (Thing t : known) {
              if (closestGreenCrystal != null) 
                 if (t.getName().equals(closestGreenCrystal.getName())) r = i;
              i++;
             }   
             if (r != -1) known.remove(r);
             closestGreenCrystal = null;
           }
        }
}
