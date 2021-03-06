package application;

//Vinh Nguyen and Maggie Maun
//Our FSE which will be a role-playing game with a developed story; Massey Quest

import java.util.*;
import java.io.*; //using serailization to input character data
import java.util.TreeMap;


//note: just add all the objects to an arraylist which will then be serialized, since each element within
//is its own individual object even after serailization

public class Attack implements Serializable {

    private TreeMap<Character,Weapons[]> profileMap = new TreeMap<Character,Weapons[]>(); //player team
    private TreeMap<Character,Weapons[]> enemyProfileMap = new TreeMap<Character,Weapons[]>(); //enemylist

    ArrayList<Character> characters = new ArrayList<Character>();
    ArrayList<Weapons> weapons= new ArrayList<Weapons>();
    ArrayList<Character> enemies = new ArrayList<Character>();

    Weapons weaponsArray[]= new Weapons[5]; //will serve as temprary array when putting weapons into treeset

	public Attack() {// no clue as to what imma do with this yet, but its a constructor
		
	}

	public void save() {

        try { //serializing occurs, will act as saving data, very efficient
            FileOutputStream saveState = new FileOutputStream("/tmp/savestate.ser");
            ObjectOutputStream out = new ObjectOutputStream(saveState);
            out.writeObject(profileMap);
            out.writeObject(enemyProfileMap);
            out.writeObject(weapons);
            out.close(); //closing it
            saveState.close();
            System.out.println("Serialized data is saved in /tmp/savestate.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public void load(){
        try {
            FileInputStream loadState = new FileInputStream("/tmp/savestate.ser");
            ObjectInputStream in = new ObjectInputStream(loadState);
            profileMap = (TreeMap<Character, Weapons[]>) in.readObject();
            enemyProfileMap=(TreeMap<Character, Weapons[]>) in.readObject();
            weapons=(ArrayList<Weapons>) in.readObject();
            in.close();
            loadState.close(); //closing it
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Not found");
            c.printStackTrace();
        }

    }

    public void refreshData(){

	    startingData(); //simply calls upon this in order to create a new save file

    }

    public void startingData(){

        try {
            Scanner inFile = new Scanner(new File("Character Statistics.txt")); //Scanner as input; input is in file
            while (inFile.hasNext()) {
                characters.add(new Character(inFile.nextLine()));
            }
            //inputing all the characters, which will then be put into the treeset
            inFile.close();



        }

        catch(IOException e) {
                System.out.println("Datafile not found");
        }

        try{
			Scanner inFile2 = new Scanner(new File("Weapon Statistics.txt")); //Scanner as input; input is in file
			while (inFile2.hasNext()) {
				weapons.add(new Weapons(inFile2.nextLine()));
			}
			//inputing all the characters, which will then be put into the treeset
			inFile2.close();
		}
		catch(IOException e) {
			System.out.println("Datafile not found");
		}

        try{
            Scanner inFile3 = new Scanner(new File("Enemy Statistics.txt")); //Scanner as input; input is in file
            while (inFile3.hasNext()) {
                enemies.add(new Character(inFile3.nextLine()));
            }
            //inputing all the characters, which will then be put into the treeset
            inFile3.close();
        }
        catch(IOException e) {
            System.out.println("Datafile not found");
        }

        weaponsArray[0]=weapons.get(0); //inputing character weapons into array, putting into treeset
        profileMap.put(characters.get(0),weaponsArray);
        weaponsArray[0]=weapons.get(4);
        profileMap.put(characters.get(1),weaponsArray);
        weaponsArray[0]=weapons.get(8);
        profileMap.put(characters.get(2),weaponsArray);
        weaponsArray[0]=weapons.get(12);
        profileMap.put(characters.get(3),weaponsArray);
        weaponsArray[0]=weapons.get(16);
        profileMap.put(characters.get(4),weaponsArray);

        weaponsArray[0]=weapons.get(1); //inputing enemy weapons into array, putting into treeset
        enemyProfileMap.put(enemies.get(0),weaponsArray);
        weaponsArray[0]=weapons.get(18);
        enemyProfileMap.put(enemies.get(1),weaponsArray);
        weaponsArray[0]=weapons.get(19);
        enemyProfileMap.put(enemies.get(2),weaponsArray);
        weaponsArray[0]=weapons.get(19);
        enemyProfileMap.put(enemies.get(3),weaponsArray);
        weaponsArray[0]=weapons.get(20);
        enemyProfileMap.put(enemies.get(4),weaponsArray);
        weaponsArray[0]=weapons.get(17);
        enemyProfileMap.put(enemies.get(5),weaponsArray);
        weaponsArray[0]=weapons.get(27);
        enemyProfileMap.put(enemies.get(6),weaponsArray);

        save();

        //inputing all the data into the treeset for preparing the beginning of the game

	}

	//ATTACK CALCULATIONS

    public int damage(Character cOne, Character cTwo, Weapons wOne, Weapons wTwo){ //will be the method used to calculate damage

        int attackPower=cOne.getStrength()+wOne.getMight(); //will be the attack power of the individual

        if (wOne.getName().equals("Sword")){ //altering attack power, depnding on weapon triangle
            if (wTwo.getName().equals("Lance")){
                attackPower-=1;
            }
            else if(wTwo.getName().equals("Axe")) {
                attackPower+=1;
            }
        }

        else if (wOne.getName().equals("Lance")){ //altering attack power, depnding on weapon triangle
            if (wTwo.getName().equals("Axe")){
                attackPower-=1;
            }
            else if(wTwo.getName().equals("Sword")) {
                attackPower+=1;
            }
        }

        else if (wOne.getName().equals("Axe")){ //altering attack power, depnding on weapon triangle
            if (wTwo.getName().equals("Sword")){
                attackPower-=1;
            }
            else if(wTwo.getName().equals("Lance")) {
                attackPower+=1;
            }
        }

        //checks for critical hit

        boolean critActivate=false; //will turn true if crit is activated, and will do twice the damage

        for (int i=0; i<cOne.getSkills().size();i++){
            if(cOne.getSkills().get(i).equals("Critical")){ //checks if there is the critical skill
                Random critChance= new Random();
                int critical=critChance.nextInt(8);
                if (critical==1) critActivate = true; //multiplies damage by 2, when critical occurs
            }
        }

        //will reduce , if shield skill is present and activated

        for (int i=0; i<cTwo.getSkills().size();i++){
            if(cOne.getSkills().get(i).equals("Critical")){ //checks if there is the critical skill
                Random shieldChance= new Random();
                int shield=shieldChance.nextInt(8);
                if (shield==1) attackPower -= 10; //multiplies damage by 2, when critical occurs
            }
        }

        //actual calculations of damage; attack power subtracted by defence

        if (cOne.getType().equals("")){ //attack power subtracts by magical defence
            if (critActivate==true) attackPower= (attackPower-cTwo.getMDefence())*2;
            else attackPower -=cTwo.getMDefence();
        }
        else{ //attack power subtracts by physical defence
            if (critActivate==true) attackPower= (attackPower-cTwo.getDefence())*2;
            else attackPower -= cTwo.getDefence();
        }


        //checks if damage dealt is less than 0

        if (attackPower<=0){
            return 0; //prevents characters from dealing negative damage
        }
        else return attackPower; //returns the damage done, which will be inputed into different methods

    }

    //Accuracy calculations; determines whether character will hit or not

    public boolean hitMiss(Character cOne, Character cTwo, Weapons wOne, Weapons wTwo){
	    int slowDown=cTwo.getBuild()-wTwo.getWeight();
	    if (slowDown>=0) slowDown=0;
        //determining the hit
        int hitRate= (wOne.getHit()+(cOne.getSkill()*2))-(slowDown*3);

        if (hitRate>100){
            hitRate=100; //ensures hit doesn't go over limit
        }
        if (hitRate<0){
            hitRate=-1; //ensures hit doesn't go under the limit
        }

        Random hitChance= new Random();
        int hit=hitChance.nextInt(100);
        if (hit<=hitRate){ //if it falls within range they will hit
            return true;
        }
        else return false; //if it doesn't fall within range they will not hit

    }

    public boolean weaponUsable(Character c, Weapons w){ //will determine if weapons are usable

        ArrayList<String> WeaponTypes=c.getWeaponTypes();
        ArrayList<String> WeaponRanks=c.getWeaponRanks();

	    //checking if weapon is usable by character

        int weaponIndex=-1; //index of weapon rank, no assigned index yet
        boolean weaponWield=false; //will determine if character acn use actual weapon

        for (int i=0;i<WeaponTypes.size();i++){
            if (WeaponTypes.get(i).equals(w.getType())){
                weaponIndex=i;
                weaponWield=true; //
                break;
            }
        }

        if (weaponWield==false){ //if weapon isn't usable, false is returned, method ends
            return false;
        }

        //checking if weapon rank is high enough to use, if weapon type is usable

	    int cRank=(int)WeaponRanks.get(weaponIndex).charAt(0); //turning character rank character into ascii values to be compared
	    int wRank=(int)w.getRank().charAt(0); //turning weapon rank character into ascii values to be compared

        if (wRank>=cRank){ //if ascii value of wRank is more than that of cRank, e.g. A rank person using E rank weapon
            return true;
        }
        else return false; //if weapon isn't usable, end method there

    }
    
    public Character weaponExperience(Character c,Weapons w) {//using weapons must raise experience, so improvement is there
    	String weaponType= w.getType(); //getting type first
        int placement=0;

        for (int i=0;i<c.getWeaponTypes().size();i++){
            if (c.getWeaponTypes().get(i).equals(weaponType)){
                c.weaponExpGain(i,w.getWeaponEXP());
            }
        } //changes the actual EXP, and possibly upgrades the weapon rank if needed
    	
    	return c; //will return character, so that new char values can be reassigned to real character
    }

    public TreeMap<Character,Weapons[]> gainExperience(TreeMap<Character,Weapons[]> characters, Character enemy){ //code for gaining experience
        Set<Character> players= characters.keySet(); //turns into set to be manipulated (screw iterators lol)

        int lowestLevel=100000000; //initialized variable that will be altered after going through loop

        for (Character chars: players){
            //finding player with lowest stats
            if (chars.getLevel()<lowestLevel){
                lowestLevel=chars.getLevel(); //getting the lowest level, person with lowest gets most exp
            }
        }

        //going through everything second time in order to actually level up

        for (Character chars:players){
            int expGained=100; //initializing variable, max xp one can get before levelling up
            expGained-=(chars.getLevel()-lowestLevel)*3;
            if (chars.getFallen()){//if character dies
                expGained-=30; //since they die they don't get as much experience
            }
            if (expGained<=0){
                expGained=0; //ensures it doesn't go below 0
            }
            if (chars.changeExp(expGained)){//if level up occurs
                chars=gainLevelStats(chars);
            }
        }

        TreeMap<Character,Weapons[]> tempProfileMap = new TreeMap<Character,Weapons[]>();

        for (Character chars:players){
            Weapons[] arsenal=tempProfileMap.get(chars);
            tempProfileMap.put(chars,arsenal); //puts back into temporary profileMap
        }

        return tempProfileMap;
        //converting everything back so that it changes values of actual treemap, instead of just set
    }

    public Character gainLevelStats(Character c){ //raising stats
	    Random rand = new Random();
	    int RNG= rand.nextInt();
	    c.levelGain();
	    c.upHealthMax(RNG); //adding stats accordingly, depending on RNG and such
        c.upStrength(RNG);
        c.upSkill(RNG);
        c.upSpeed(RNG);
        c.upDefence(RNG);
        c.upMDefence(RNG);
        c.upBuild(RNG);
        return c;

    }

    public TreeMap<Character, Weapons[]> reviveAll(TreeMap<Character, Weapons[]> characters){
        Set<Character> players= characters.keySet(); //turns into set to be manipulated (screw iterators lol)

        for (Character chars: players){
            //finding player with lowest stats
            chars.reviveChar();
        }

        TreeMap<Character,Weapons[]> tempProfileMap = new TreeMap<Character,Weapons[]>();

        for (Character chars:players){
            Weapons[] arsenal=tempProfileMap.get(chars);
            tempProfileMap.put(chars,arsenal); //puts back into temporary profileMap
        }

        return tempProfileMap;
    }

    public void battleSystem(Character enemyFight, Weapons[] enemyArsenal){ //since enemies differ accoreding to collision, must account for all cases
	    boolean fighting= true; //will determine when the battle ends
        boolean enemyWon=false; //will be decided whether experience is given or not

        Scanner choice = new Scanner(System.in);

        while (fighting==true){
            boolean teamDefeated=true; //will see if every character has fallen or not

            //choice to run or stay

            //PLAYER PHASE OF COMBAT

            Set<Character> players= profileMap.keySet(); //turns into set to be displayed later as print statement
            //**pre-graphics

            //checks if characters are dead
            for (Character chars:players){
                if (chars.getFallen()==false){
                    teamDefeated=false; //inplies that there is still one character left
                    break; //will end loop once everything is checked
                }
            }

            if (teamDefeated==true){//if whole team is fallen
                fighting=false; //match ends
            }

            for (Character chars: players){ //goes through each character in the table
                boolean passTurn=false; //turn isn't passed yet, that is player's choice
                Weapons usedArm=weaponsArray[0]; //will be selected, currently just given random weapon value to be initialized

                if (chars.getFallen()!=true){ //if character hasn't fallen
                    while (true) {
                        System.out.println("Choose your item");
                        System.out.println();
                        System.out.println(chars.getName());
                        System.out.println();

                        Weapons[] arms= profileMap.get(chars);

                        for (int i=0;i<arms.length;i++){ //displays all weapon options
                            System.out.print((i+1)+". ");
                            System.out.println(arms[i]);
                        }

                        System.out.println((arms.length+1)+". Pass Turn");

                        int selection=choice.nextInt();

                        if (selection==arms.length+1){ //if they pass the turn, nothing is done
                            passTurn=true; //turn will be passed
                            break; //ends player selection
                        }
                        else if(selection<1 || selection>arms.length+1){ //if selection isn't valid
                            System.out.println("Option not valid");
                            System.out.println();
                        }
                        else if (weaponUsable(chars,arms[selection-1])==false){
                            usedArm=arms[selection-1];
                            System.out.println("Item is unusable");
                            System.out.println(); //extra line for neat format, for pregraphics
                        }
                        else{ //whenever weapon is usable
                            if (chars.getUnit().equals("Healer")){
                                boolean healingPossible=false;
                                for (Character c:players){
                                    if (c.getHealth()<c.getHealthMax()){
                                        healingPossible=true; //now possible to heal
                                    }
                                }
                                if (healingPossible){
                                    System.out.println(chars.getName()+" uses "+usedArm.getName());
                                    break; //breaks loop
                                }
                                else{
                                    System.out.println("Healing is not possible!");
                                }
                            }
                            else{
                                System.out.println(chars.getName()+" selects "+arms[selection-1].getName());
                                System.out.println();
                                break; //weapon selection ends
                            }
                        }
                    }
                }

                if (passTurn!=true){
                    if (chars.getUnit().equals("Healer")){
                        while (true){
                            //selecting a character to heal
                            int counter=1;
                            boolean heal=false; //character not healed yet
                            for (Character c:players){
                                System.out.println(counter+". "+c.getName());
                            }
                            int selectHeal= choice.nextInt();
                            if (selectHeal>=1 && selectHeal<=counter){
                                counter=1;
                                for (Character c:players){
                                    if (counter==selectHeal){
                                        if (c.getHealth()<c.getHealthMax()){
                                            System.out.println(chars.getName()+" heals "+c.getName());
                                            c.changeHealth(chars.getStrength()+usedArm.getMight(),"up");
                                            weaponExperience(chars,usedArm); //adds weapon experience
                                            heal=true;
                                            break;
                                        }
                                    }
                                    else{
                                        counter+=1;
                                    }
                                }
                                if (heal==true){
                                    break; //ends the loop where the character selects a person to heal, including oneself
                                }
                            }
                            else{
                                System.out.println("Not a valid choice");
                                System.out.println();
                            }
                        }
                    }
                    else{
                        if (hitMiss(chars,enemyFight,usedArm,enemyArsenal[0])==true){ //if it hits
                            int hit=damage(chars,enemyFight,usedArm,enemyArsenal[0]);
                            System.out.println(chars.getName()+" does "+hit+" damage.");
                            enemyFight.changeHealth(hit,"down"); //deducts health from enemy
                            weaponExperience(chars,usedArm); //adds weapon experience
                        }
                        else{ //if it misses
                            System.out.println(chars.getName()+"missed! ");
                            System.out.println();
                        }
                    }
                }

                //checking to see if enemy dies

                if (enemyFight.charFallen()==true){
                    enemyWon=true; //that means enemy has died
                    break; //will break the loop so it switches to enemy phase, more loops will break then
                }

            }

            //ENEMY PHASE OF COMBAT

            if (enemyFight.charFallen()==true){
                fighting=false; //fighting loop ends, match ends
            }

            int mostDamage=-1; //initilizing variable, will be used to see which character is most vulnerable
            Character mostVulnerable= new Character("meme,0,0,0,0,0,0,0,2,E,E,Sword,Lance,1,Sure Strike,0,MemeGod,Physical,0,0,0,0,0,0,0,0,0");
            //will be used to find character that can get most damage inflicted upon them, var already initialized


            Weapons[] armsCharChosen=weaponsArray; //initializing the variable for the character weapon arsenal
            for (Character chars:players) { //checking vulnerability of character

                Weapons[] arms=profileMap.get(chars); //initializing the variable for the character weapon arsenal

                if (mostDamage<damage(enemyFight,chars,enemyArsenal[0],arms[0])){
                    mostDamage=damage(enemyFight,chars,enemyArsenal[0],arms[0]);
                    mostVulnerable=chars;
                    armsCharChosen= profileMap.get(chars);
                }

            }

            //actually inflicting the damage upon tbe player

            if (hitMiss(enemyFight,mostVulnerable,enemyArsenal[0],armsCharChosen[0])==true){
                System.out.println(enemyFight.getName()+"used "+enemyArsenal[0].getName());
                System.out.println(enemyFight.getName()+"did "+mostDamage+" damage!");

                for (Character chars:players){ //going through to find character and kill them
                    if (chars==mostVulnerable){
                        chars.changeHealth(mostDamage,"down"); //inflicts damage
                        break; //stops loop after damage is done
                    }

                }
            }

            TreeMap<Character, Weapons[]> tempProfileMap= new TreeMap<Character, Weapons[]>(); //after both turns finished, updates status of profileMap
            for (Character chars:players){
                Weapons[] arsenal=tempProfileMap.get(chars);
                tempProfileMap.put(chars,arsenal); //puts back into temporary profileMap
            }

            profileMap=tempProfileMap; //reassigns value of profile map so it updates

        }

        choice.close();

        if (enemyWon!=true){//if player wins
            profileMap=gainExperience(profileMap,enemyFight); //adds experience before game terminates
        }
        else{//if enemy wins
            profileMap=reviveAll(profileMap);
        }

    }

}

class Character{
	
	//declaring all the character stats and variables
	
	private String Name; //determines name of character
	private ArrayList<String> Skills= new ArrayList<String>(); //skills help determine special abilities
	private ArrayList<String> WeaponRanks= new ArrayList<String>(); //determines weapons usable, and weapon skills
	private ArrayList<String> WeaponTypes= new ArrayList<String>(); //determines types of weapons usable
	
	private int HPMAX; //health points
	private int STR; //strength/magic, combines with might of weapon in order to determine attack power
	private int SKL; //skill, helps determine hit rate
	private int SPD; //speed, will help determine avoidance
	private int DEF; //defense; attack power subtracted by defense to determine damage done to character
	private int MDEF; //magical defense; magic power subtracted by this to determine damage done to character
	private int BLD; //build; the bigger the build, bigger weapons can be used without speed loss
	
	private int WPNTYPES; //number that determines how many types of weapons can be used
	private int SKLNUM; //number of skills a character has
	
	private String Unit; //determines what type of unit they are
	private String Type; //determines whether they're magical or physical fighters
	
	private int HP; //will be the variable that is changed
	
	private boolean fallen= false; //will determine if a character has fallen
	
	private int LVL; //game will be a level system that will increase stats accordingly
	private int EXP; 
	
	private int HPGROWTH; //will be the growths gained based on level
	private int STRGROWTH;
	private int SKLGROWTH;
	private int SPDGROWTH;
	private int DEFGROWTH;
	private int MDEFGROWTH;
	private int BLDGROWTH;
	
	private ArrayList<Integer> weaponEXP= new ArrayList<Integer>(); //the different types of weapon experiences
	
	public Character(String characterStat) { //constructor for character class
		
		String[] data = characterStat.split(",");
		
		this.Name=data[0]; //assigning values and initializing variables
		this.HPMAX=Integer.parseInt(data[1]);
		this.STR=Integer.parseInt(data[2]);
		this.SKL=Integer.parseInt(data[3]);
		this.SPD=Integer.parseInt(data[4]);
		this.DEF=Integer.parseInt(data[5]);
		this.MDEF=Integer.parseInt(data[6]);
		this.BLD=Integer.parseInt(data[7]);
		this.WPNTYPES=Integer.parseInt(data[8]);
		
		for (int i=0;i<WPNTYPES;i++) {
			WeaponRanks.add(data[8+i+1]);//adds the ranks accordingly
		}
		
		for (int i=0;i<WPNTYPES;i++) {
			WeaponTypes.add(data[8+WPNTYPES+i+1]); //adds the types of weapons usable
		}
		
		int tempPos=9+(WPNTYPES*2)+1; //will be used to continue, so that calculations to get positions aren't messy
		
		SKLNUM=Integer.parseInt(data[tempPos]); //the number of skills a character has
		
		for (int i=0;i<SKLNUM;i++) {
			Skills.add(data[tempPos+i+1]); //adds the skills that each character has
		}
		
		this.EXP=Integer.parseInt(data[tempPos+SKLNUM+1]);
		this.Unit=data[tempPos+SKLNUM+2];
		this.Type=data[tempPos+SKLNUM+3];
		
		tempPos=tempPos+SKLNUM+3;
		
		this.LVL=Integer.parseInt(data[tempPos+1]);
		this.EXP=Integer.parseInt(data[tempPos+2]);
		this.HPGROWTH=Integer.parseInt(data[tempPos+3]);
		this.STRGROWTH=Integer.parseInt(data[tempPos+4]);
		this.SKLGROWTH=Integer.parseInt(data[tempPos+5]);
		this.SPDGROWTH=Integer.parseInt(data[tempPos+6]);
		this.DEFGROWTH=Integer.parseInt(data[tempPos+7]);
		this.MDEFGROWTH=Integer.parseInt(data[tempPos+8]);
		this.BLDGROWTH=Integer.parseInt(data[tempPos+9]);
		
		for (int i=0;i<WPNTYPES;i++) {
			weaponEXP.add(Integer.parseInt(data[tempPos+9+i+1]));
		}
		
		HP=HPMAX; //assigns the original health of a character to the max health
		
	}
	
	//METHODS TO GET ACCESS TO INFORMATION
	
	public String getName() {
		return Name;
	}
	
	public int getHealth() {
		return HP;
	}
	
	public int getHealthMax() {
		return HPMAX;
	}
	
	public int getStrength() {
		return STR;
	}
	
	public int getSkill() {
		return SKL;
	}
	
	public int getSpeed() {
		return SPD;
	}
	
	public int getDefence() {
		return DEF;
	}
	
	public int getMDefence() {
		return MDEF;
	}
	
	public int getBuild() {
		return BLD;
	}
	
	public String getUnit() {
		return Unit;
	}
	
	public String getType() {
		return Type;
	}
	
	public int getLevel() {
		return LVL;
	}
	
	public int getExp() {
		return EXP;
	}
	
	public boolean getFallen() { //will return to see if character has fallen
		return fallen;
	}

    public ArrayList<String> getSkills(){
        return Skills;
    }
    
    public ArrayList<String> getWeaponRanks(){
        return WeaponRanks;
    }
    
    public ArrayList<String> getWeaponTypes(){
        return WeaponTypes;
    }
	
	//METHODS TO ALTER INFORMATIONS
	
	public void changeHealth(int number, String gainlost) {
		if (gainlost.equals("up")) {
			HP+=number;
			if (HP>HPMAX) {
				HP=HPMAX; //character caps health without going over
			}
		}
		else if (gainlost.equals("down")) {
			HP-=number;
			if (HP<0) {
				HP=0; //character dies
			}
		}
	}
	
	public void reviveChar() { //reviving characters after battle
		if (fallen=true) {
			HP=1; //critical condition for player, must get healed
			fallen=false; //will revive of the player
		}
	}
	
	public boolean changeExp(int additionalExp) {
		EXP+=additionalExp;
		if (EXP>=100) {
			return true;
		}
		else return false;
	}
	
	public void levelGain() {
		LVL+=1;
		EXP-=100;
	}
	
	public void weaponExpGain(int placement, int expGain) {
		for (int i=0;i<weaponEXP.size();i++){
			if (i==placement) {
				if (!WeaponRanks.get(i).equals("A")) { //as long as character hasn't capped stats
					weaponEXP.set(i, weaponEXP.get(i)+expGain); //sets it as that
					if (weaponEXP.get(i)>150) {
						weaponEXP.set(i, 150); //caps weapon experience when weapon fully mastered
					}
				}
			}
		}
		
	}
	
	public void upgradeWeaponRank(int placement) { //for upgrading weapons ranks, allows more gameplay variety
		if (weaponEXP.get(placement)<30) {
			WeaponRanks.set(placement,"E");
		}
		else if (weaponEXP.get(placement)>=30 && weaponEXP.get(placement)<60) {
			WeaponRanks.set(placement,"D");
		}
		else if (weaponEXP.get(placement)>=60 && weaponEXP.get(placement)<100) {
			WeaponRanks.set(placement,"C");
		}
		else if (weaponEXP.get(placement)>=100 && weaponEXP.get(placement)<150) {
			WeaponRanks.set(placement,"B");
		}
		else if (weaponEXP.get(placement)==150) {
			WeaponRanks.set(placement,"A");
		}
	}
	
	//METHODS TO RAISE STATS DURING LEVELS
	
	public void upHealthMax(int RNG) { //will boost the health cap for a character during a level up
		if (RNG<=HPGROWTH) {
			HPMAX+=1;
		}
	}
	
	public void upStrength(int RNG) { //will boost the health cap for a character during a level up
		if (RNG<=STRGROWTH) {
			STR+=1;
		}
	}
	
	public void upSkill(int RNG) { //will boost the health cap for a character during a level up
		if (RNG<=SKLGROWTH) {
			SKL+=1;
		}
	}
	
	public void upSpeed(int RNG) { //will boost the health cap for a character during a level up
		if (RNG<=SPDGROWTH) {
			SPD+=1;
		}
	}
	
	public void upDefence(int RNG) { //will boost the health cap for a character during a level up
		if (RNG<=DEFGROWTH) {
			DEF+=1;
		}
	}
	
	public void upMDefence(int RNG) { //will boost the health cap for a character during a level up
		if (RNG<=MDEFGROWTH) {
			MDEF+=1;
		}
	}
	
	public void upBuild(int RNG) {
		if (RNG<=BLDGROWTH) {
			BLD+=1;
		}
	}
	
	//METHODS TO CHECK CERTAIN CONDITIONS
	
	public boolean charFallen() {
		if (HP<=0) {
			fallen=true; //extra safeguard, since permadeath is not a concept present in game
			return true; //no more health, therefore dead
		}
		else return false;
	}
}

class Weapons{
	
	private String Name; //determines type of weapon
	private String Type; //determines what item its made out of, e.g. silver or iron, or anima and light
	private String weaponRank; //determines rank of weapon required to wield, from E to A
	private int Value; //will determine its value, for sale and buying cost
	private int Weight; //the weight of the weapon, when subtracted from build, if a negative number comes out, it will decrease speed
	
	private int STR; //will be used to determine might of weapon
    private int HIT; //hit rate of the weapon, assists in accuracy
    
    private int expGAIN; //will determine how much weapon exp each weapon gives
	
	public Weapons(String weaponStat) { //constructor for weapon class
		
		String[] data= weaponStat.split(",");
		
		this.Name=data[0]; //assigning values and initializing variables
		this.Type=data[1];
		this.weaponRank=data[2];
		this.Value=Integer.parseInt(data[3]);
		this.Weight=Integer.parseInt(data[4]);
		this.STR=Integer.parseInt(data[5]);
		this.HIT=Integer.parseInt(data[6]);
		this.expGAIN=Integer.parseInt(data[7]);
		
	}
	
	//METHODS TO GET ACCESS TO INFORMATION
	
	public String getName() { 
		return Name;
	}
	
	public String getType() {
		return Type;
	}
	
	public String getRank() {
		return weaponRank;
	}

    public int getValue() {
        return Value;
    }

    public int getWeight() {
        return Weight;
    }

    public int getMight() {
        return STR;
    }

	public int getHit() {
		return HIT;
	}
	
	public int getWeaponEXP() {
		return expGAIN;
	}
	
	//METHODS TO ALTER INFORMATIONS
	
}

