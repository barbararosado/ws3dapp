package codelets.behaviors;


import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Thing;

/** 
 * 
 * @author klaus
 * 
 */

public class Forage extends Codelet {
    
        private MemoryObject knownGreenCrystalsMO;
        private List<Thing> known;
        private MemoryObject legsMO3;


	/**
	 * Default constructor
	 */
	public Forage(){
		
	}

	@Override
	public void proc() {
            known = (List<Thing>) knownGreenCrystalsMO.getI();
            //legsMO3.setEvaluation(0.0);
            if (known.size() == 0) {
		JSONObject message=new JSONObject();
			try {
				message.put("ACTION", "FORAGE");
				legsMO3.updateInfo(message.toString());
                                legsMO3.setEvaluation(5.0);
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            }            
		
	}

	@Override
	public void accessMemoryObjects() {
            knownGreenCrystalsMO = this.getInput("KNOWN_GCRYSTALS");
            legsMO3=this.getOutput("LEGS3");

		// TODO Auto-generated method stub
		
	}
        
        @Override
        public void calculateActivation() {
            
        }


}
