/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import codelets.behaviors.EatClosestApple;
import codelets.behaviors.Escape;
import codelets.behaviors.Forage;
import codelets.behaviors.GoToBank;
import codelets.behaviors.GoToClosestApple;
import codelets.behaviors.GoToClosestCrystal;
import codelets.behaviors.TakeCrystal;
import codelets.motor.HandsActionCodelet;
import codelets.motor.LegsActionCodelet;
import codelets.perception.AppleDetector;
import codelets.perception.ClosestAppleDetector;
import codelets.perception.ClosestGreenCrystalDetector;
import codelets.perception.ClosestRedCrystalDetector;
import codelets.perception.CrystalDetector;
import codelets.sensors.InnerSense;
import codelets.perception.LowEnergyDetector;
import codelets.sensors.BagInspector;
import codelets.sensors.Vision;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import memory.CreatureInnerSense;
import support.MindView;
import ws3dproxy.model.Bag;
import ws3dproxy.model.Thing;

/**
 *
 * @author rgudwin
 */
public class AgentMind extends Mind {
    
    private static int creatureBasicSpeed=1;
    private static int reachDistance=50;
    
    public AgentMind(Environment env) {
                super();
                // Create RawMemory and Coderack
                //Mind m = new Mind();
                //RawMemory rawMemory=RawMemory.getInstance();
	        //CodeRack codeRack=CodeRack.getInstance();
                
                // Declare Memory Objects
	        MemoryObject legsMO;
	        MemoryObject handsMO;
                MemoryObject visionMO;
                MemoryObject innerSenseMO;
                MemoryObject closestAppleMO;
                MemoryObject knownApplesMO;
                MemoryObject hungerMO;
                MemoryObject fearMO;
                MemoryObject knownGreenCrystalsMO;
                MemoryObject closestGreenCrystalMO;
                MemoryObject bagMO;
                
                //Initialize Memory Objects
                legsMO=createMemoryObject("LEGS");
		handsMO=createMemoryObject("HANDS");
                List<Thing> vision_list = Collections.synchronizedList(new ArrayList<Thing>());
		visionMO=createMemoryObject("VISION",vision_list);
                CreatureInnerSense cis = new CreatureInnerSense();
		innerSenseMO=createMemoryObject("INNER", cis);
                Thing closestApple = null;
                closestAppleMO=createMemoryObject("CLOSEST_APPLE", closestApple);
                List<Thing> knownApples = Collections.synchronizedList(new ArrayList<Thing>());
                knownApplesMO=createMemoryObject("KNOWN_APPLES", knownApples);
                Double x = 0.0;
                hungerMO=createMemoryObject("HUNGER", x);
                Double f = 0.0;
                fearMO=createMemoryObject("FEAR", f);
                List<Thing> knownGreenCrystals = Collections.synchronizedList(new ArrayList<Thing>());
                knownGreenCrystalsMO=createMemoryObject("KNOWN_GCRYSTALS", knownGreenCrystals);
                Thing closestGreenCrystal = null;
                closestGreenCrystalMO=createMemoryObject("CLOSEST_GCRYSTAL", closestGreenCrystal);
                bagMO=createMemoryObject("BAG");
                
                
                // Create and Populate MindViewer
                MindView mv = new MindView("MindView");
                mv.addMO(knownApplesMO);
                mv.addMO(visionMO);
                mv.addMO(closestAppleMO);
                mv.addMO(innerSenseMO);
                mv.addMO(handsMO);
                mv.addMO(legsMO);
                mv.addMO(hungerMO);
                mv.addMO(fearMO);
                mv.addMO(knownGreenCrystalsMO);
                mv.addMO(closestGreenCrystalMO);
                mv.addMO(bagMO);
                mv.StartTimer();
                mv.setVisible(true);
		
		// Create Sensor Codelets	
		Codelet vision=new Vision(env.c);
		vision.addOutput(visionMO);
                insertCodelet(vision); //Creates a vision sensor
		
		Codelet innerSense=new InnerSense(env.c);
		innerSense.addOutput(innerSenseMO);
                insertCodelet(innerSense); //A sensor for the inner state of the creature
                Codelet bag=new BagInspector(env.c);
		bag.addOutput(bagMO);
                insertCodelet(bag); //Creates a bag sensor
                
		// Create Actuator Codelets
		Codelet legs=new LegsActionCodelet(env.c);
		legs.addInput(legsMO);
                insertCodelet(legs);

		Codelet hands=new HandsActionCodelet(env.c);
		hands.addInput(handsMO);
                insertCodelet(hands);
		
		// Create Perception Codelets
                Codelet ad = new AppleDetector();
                ad.addInput(visionMO);
                ad.addOutput(knownApplesMO);
                insertCodelet(ad);
                
		Codelet closestAppleDetector = new ClosestAppleDetector();
		closestAppleDetector.addInput(knownApplesMO);
		closestAppleDetector.addInput(innerSenseMO);
		closestAppleDetector.addOutput(closestAppleMO);
                insertCodelet(closestAppleDetector);
                
                Codelet lowEnergyDetector=new LowEnergyDetector(env.c);
                lowEnergyDetector.addOutput(hungerMO);
                lowEnergyDetector.addInput(innerSenseMO);
                insertCodelet(lowEnergyDetector);
                
                Codelet cd = new CrystalDetector();
                cd.addInput(visionMO);
                cd.addOutput(knownGreenCrystalsMO);
                insertCodelet(cd);
                
                Codelet closestRedCristalDetector = new ClosestRedCrystalDetector();
		closestRedCristalDetector.addInput(innerSenseMO); //por que?
		closestRedCristalDetector.addInput(visionMO);
                closestRedCristalDetector.addOutput(fearMO);
                insertCodelet(closestRedCristalDetector);
                
                Codelet closestGreenCristalDetector = new ClosestGreenCrystalDetector();
		closestGreenCristalDetector.addInput(knownGreenCrystalsMO);
		closestGreenCristalDetector.addInput(innerSenseMO); //por que?
		closestGreenCristalDetector.addOutput(closestGreenCrystalMO);
                insertCodelet(closestGreenCristalDetector);
                
		
		// Create Behavior Codelets
		Codelet goToClosestApple = new GoToClosestApple(creatureBasicSpeed,reachDistance);
		goToClosestApple.addInput(closestAppleMO);
		goToClosestApple.addInput(innerSenseMO);
		goToClosestApple.addOutput(legsMO);
                insertCodelet(goToClosestApple);
		
		Codelet eatApple=new EatClosestApple(reachDistance);
		eatApple.addInput(closestAppleMO);
		eatApple.addInput(innerSenseMO);
		eatApple.addOutput(handsMO);
                eatApple.addOutput(knownApplesMO);
                insertCodelet(eatApple);
                
                Codelet forage=new Forage();
		forage.addInput(knownApplesMO);
                forage.addInput(knownGreenCrystalsMO);
                forage.addOutput(legsMO);
                insertCodelet(forage);
                
                Codelet goToClosestCrystal = new GoToClosestCrystal(creatureBasicSpeed,reachDistance);
		goToClosestCrystal.addInput(closestGreenCrystalMO);
		goToClosestCrystal.addInput(innerSenseMO);
                goToClosestCrystal.addInput(bagMO);
		goToClosestCrystal.addOutput(legsMO);
                insertCodelet(goToClosestCrystal);
                
                Codelet escape= new Escape();
                escape.addInput(knownApplesMO);
                escape.addInput(fearMO);
                escape.addOutput(legsMO);
                insertCodelet(escape);
                
                Codelet goToBank=new GoToBank(creatureBasicSpeed,reachDistance);
                goToBank.addInput(bagMO);
                goToBank.addInput(innerSenseMO);
                goToBank.addOutput(legsMO);
                insertCodelet(goToBank);
                
                Codelet takeCristal=new TakeCrystal(reachDistance);
		takeCristal.addInput(closestGreenCrystalMO);
		takeCristal.addInput(innerSenseMO);
		takeCristal.addOutput(handsMO);
                //takeCristal.addOutput(bagMO);
                takeCristal.addOutput(knownGreenCrystalsMO);
                insertCodelet(takeCristal);
                
                
		
		// Start Cognitive Cycle
		start(); 
    }             
    
}
