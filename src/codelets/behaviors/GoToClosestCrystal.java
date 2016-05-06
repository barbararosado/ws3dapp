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
import memory.CreatureInnerSense;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Thing;

/**
 *
 * @author barbara
 */
public class GoToClosestCrystal extends Codelet {
        private MemoryObject closestGreenCrystalMO;
	private MemoryObject selfInfoMO;
        private MemoryObject bagMO;
	private MemoryObject legsMO5;
	private int creatureBasicSpeed;
	private double reachDistance;

	public GoToClosestCrystal(int creatureBasicSpeed, int reachDistance) {
		this.creatureBasicSpeed=creatureBasicSpeed;
		this.reachDistance=reachDistance;
	}

	@Override
	public void accessMemoryObjects() {
		closestGreenCrystalMO=this.getInput("CLOSEST_GCRYSTAL");
		selfInfoMO=this.getInput("INNER");
		legsMO5=this.getOutput("LEGS5");
	}

	@Override
	public void proc() {
		// Find distance between creature and closest green crystal
		//If far, go towards it
		//If close, stops

		//String appleInfo = closestAppleMO.getInfo();
                Thing closestGreenCrystal = (Thing) closestGreenCrystalMO.getI();
		String selfInfo= selfInfoMO.getInfo();
                CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();
                //System.out.println("GoToClosestApple: "+appleInfo+" "+selfInfo);
                //legsMO5.setEvaluation(0.0);

		if(closestGreenCrystal != null)
		{
			JSONObject jsonGCrystalInfo=null;
			String gcrystalName="";
			double gcrystalX=0;
			double gcrystalY=0;
			try {
				//jsonAppleInfo = new JSONObject(appleInfo);
				//appleName=jsonAppleInfo.getString("NAME");
				//appleX=jsonAppleInfo.getDouble("X");
				//appleY=jsonAppleInfo.getDouble("Y");
                                gcrystalName = closestGreenCrystal.getName();
                                gcrystalX = closestGreenCrystal.getX1();
                                gcrystalY = closestGreenCrystal.getY1();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String[] selfInfoArray=selfInfo.split(" ");

			String selfName=selfInfoArray[0];
			double selfX=cis.position.getX();
			double selfY=cis.position.getY();

			Point2D pGCrystal = new Point();
			pGCrystal.setLocation(gcrystalX, gcrystalY);

			Point2D pSelf = new Point();
			pSelf.setLocation(selfX, selfY);

			double distance = pSelf.distance(pGCrystal);
			JSONObject message=new JSONObject();
			try {
				if(distance>reachDistance){ //Go to it
                                        message.put("ACTION", "GOTO");
					message.put("X", (int)gcrystalX);
					message.put("Y", (int)gcrystalY);
                                        message.put("SPEED", creatureBasicSpeed);	

				}else{//Stop
					message.put("ACTION", "GOTO");
					message.put("X", (int)gcrystalX);
					message.put("Y", (int)gcrystalY);
                                        message.put("SPEED", 0.0);	
				}
				legsMO5.updateInfo(message.toString());
                                legsMO5.setEvaluation(5.0);
//				System.out.println(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
                        legsMO5.setEvaluation(0.0);
//			JSONObject message=new JSONObject();
//			try {
//				message.put("ACTION", "GOTO");
//				message.put("X", "0");
//				message.put("Y", "0");
//                                message.put("SPEED", 0.0);
//				legsMO.updateInfo(message.toString());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
         //System.out.println("Command: "+legsMO.getInfo());

	}//end proc
        
        @Override
        public void calculateActivation() {
        
        }
}
