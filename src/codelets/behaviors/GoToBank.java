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

/**
 *
 * @author barbara
 */

/*deve ver qual os cristais ele possui a fim de saber qual leaflet pode ser cumprido
            não considerar os leaflets que possuem cristais vermelhos
            
*/
public class GoToBank extends Codelet {
        private MemoryObject bagMO;
	private MemoryObject selfInfoMO;
	private MemoryObject legsMO;
         private MemoryObject innerSenseMO;
	private int creatureBasicSpeed;
	private double reachDistance;

	public GoToBank(int creatureBasicSpeed, int reachDistance) {
		this.creatureBasicSpeed=creatureBasicSpeed;
		this.reachDistance=reachDistance;
	}

	@Override
	public void accessMemoryObjects() {
		bagMO=this.getInput("BAG");
		selfInfoMO=this.getInput("INNER");
		legsMO=this.getOutput("LEGS");
	}

	@Override
	public void proc() {
		// Find distance between creature and bank and go there
	
                String selfInfo= selfInfoMO.getInfo();
                CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();
                //System.out.println("GoToClosestApple: "+appleInfo+" "+selfInfo);
                       
                double bankX=0;
		double bankY=0;
		
		String[] selfInfoArray=selfInfo.split(" ");

		String selfName=selfInfoArray[0];
		double selfX=cis.position.getX();
		double selfY=cis.position.getY();

		Point2D pBank = new Point();
		pBank.setLocation(bankX, bankY);

		Point2D pSelf = new Point();
		pSelf.setLocation(selfX, selfY);

		double distance = pSelf.distance(pBank);
		JSONObject message=new JSONObject();
		try {
    
    //testar aqui se o leaflet está completo
			if((distance>reachDistance)){ //Go to it
                                message.put("ACTION", "GOTO");
				message.put("X", (int)bankX);
				message.put("Y", (int)bankY);
                                message.put("SPEED", creatureBasicSpeed);	

				}else{//Stop
					message.put("ACTION", "GOTO");
					message.put("X", (int)bankX);
					message.put("Y", (int)bankY);
                                        message.put("SPEED", 0.0);	
				}
				legsMO.updateInfo(message.toString());
//				System.out.println(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
         //System.out.println("Command: "+legsMO.getInfo());

	}//end proc
        
        @Override
        public void calculateActivation() {
        
        }
    
}
