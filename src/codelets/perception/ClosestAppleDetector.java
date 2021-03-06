/**
 * 
 */
package codelets.perception;



import br.unicamp.cst.core.entities.Codelet;


import org.json.JSONException;
import org.json.JSONObject;

import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Collections;
import memory.CreatureInnerSense;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import ws3dproxy.model.Thing;

/**
 * @author klaus
 *
 */
public class ClosestAppleDetector extends Codelet {

	private MemoryObject knownMO;
	private MemoryObject closestAppleMO;
	private MemoryObject innerSenseMO;
	
        private List<Thing> known;

	public ClosestAppleDetector() {

	}


	@Override
	public void accessMemoryObjects() {
		this.knownMO=this.getInput("KNOWN_APPLES");
		this.innerSenseMO=this.getInput("INNER");
		this.closestAppleMO=this.getOutput("CLOSEST_APPLE");	
	}
	@Override
	public void proc() {
                Thing closest_apple=null;
                double selfX;
	        double selfY;
	        String objectName;
	        double closestAppleX=0;
	        double closestAppleY=0;
	        String closestAppleName;
	        double appleX;
	        double appleY;
                //System.out.println("Processing ClosestAppleDetector Codelet");
            
                known = Collections.synchronizedList((List<Thing>) knownMO.getI());
                //System.out.println("known: "+known);
                //System.out.println(vision.toString());
                CreatureInnerSense cis = (CreatureInnerSense) innerSenseMO.getI();
                //System.out.println(closestAppleMO);
                //System.out.println("closestAppleMO proc");
                synchronized(known) {
		if(known.size() != 0){
			//Extracting self position
			selfX = cis.position.getX();
			selfY = cis.position.getY();
                        //System.out.println("self: "+selfX+","+selfY);
			//Point self = new Point();

			//Iterate over objects in vision, looking for the closest apple
			closestAppleName=null;
                        CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
                         for (Thing t : myknown) {
				objectName=t.getName();
				if(objectName.contains("PFood") && !objectName.contains("NPFood")){ //Then, it is an apple
					appleX=t.getX1();
					appleY=t.getY1();
					if(closestAppleName==null){
                                                closest_apple = t;
						closestAppleName=objectName;
						closestAppleX=appleX;
						closestAppleY=appleY;
					}else{
						double Dnew=Math.sqrt(Math.pow(appleX-selfX, 2)+Math.pow(appleY-selfY, 2));
						double Dclosest=Math.sqrt(Math.pow(closestAppleX-selfX, 2)+Math.pow(closestAppleY-selfY, 2));
						if(Dnew<Dclosest){
							closestAppleName=objectName;
							closestAppleX=appleX;
							closestAppleY=appleY;
                                                        closest_apple = t;
						}
					}
				}
			 }
                        
                        //System.out.println("Achou: "+closestAppleName+","+closestAppleX+","+closestAppleY);
			if(closestAppleName!=null){
				JSONObject jsonInfo=new JSONObject();	
				try {
					jsonInfo.put("NAME", closestAppleName);
					jsonInfo.put("X", closestAppleX);
					jsonInfo.put("Y", closestAppleY);
					if(!closestAppleMO.getInfo().equals(jsonInfo.toString())){
						closestAppleMO.updateInfo(jsonInfo.toString());
                                                closestAppleMO.setI(closest_apple);
						//System.out.println("Achou: "+closestAppleMO.getInfo());
					}
                                        //else System.out.println("closestAppleMO: "+closestAppleX+","+closestAppleY+" self:"+selfX+","+selfY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				closestAppleMO.updateInfo(""); //couldn't find any nearby apples
                                closest_apple = null;
                                closestAppleMO.setI(closest_apple);
                                //System.out.println("Naoachou: "+ known);
			}

//						System.out.println(closestAppleMO.getInfo());
                
		}else{
			closestAppleMO.updateInfo("");
                        closest_apple = null;
                        closestAppleMO.setI(closest_apple);
                        //System.out.println("Naoachou2: "+closestAppleMO.getInfo()+" known:"+known);
		}
                }
//		System.out.println("closestAppleMO: "+closestAppleMO);
                //System.out.println("Closest Apple: "+closest_apple+" "+closestAppleMO.getInfo());
	}//end proc

@Override
        public void calculateActivation() {
        
        }

}
