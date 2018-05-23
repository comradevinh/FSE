package application;

//Vinh Nguyen and Maggie Maun
//Our FSE which will be a role-playing game with a developed story


import javafx.application.Application; //inputting all necessary aspects
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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

    Weapons weaponsArray[]= new Weapons[5]; //will serve as temprary array when putting weapons into treeset

	public Attack() {// no clue as to what imma do with this yet, but its a constructor
		
	}

	public void save(){

        try { //serializing occurs, will act as saving data, very efficient
            FileOutputStream saveState = new FileOutputStream("/tmp/savestate.ser");
            ObjectOutputStream out = new ObjectOutputStream(saveState);
            out.writeObject(profileMap);
            out.close(); //closing it
            saveState.close();
            System.out.printf("Serialized data is saved in /tmp/savestate.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public void damage(Character one, Character two){ //will be the method used to calculate damage

    }

    public void load(){

        TreeMap<Character,Weapons[]> profileMap = null;
        try {
            FileInputStream loadState = new FileInputStream("/tmp/savestate.ser");
            ObjectInputStream in = new ObjectInputStream(loadState);
            profileMap = (TreeMap<Character, Weapons[]>) in.readObject();
            in.close();
            loadState.close(); //closing it
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Not found");
            c.printStackTrace();
            return;
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

        weaponsArray[0]=weapons.get(0);
        profileMap.put(characters.get(0),weaponsArray);
        weaponsArray[0]=weapons.get(4);
        profileMap.put(characters.get(1),weaponsArray);
        weaponsArray[0]=weapons.get(8);
        profileMap.put(characters.get(2),weaponsArray);
        weaponsArray[0]=weapons.get(12);
        profileMap.put(characters.get(3),weaponsArray);
        weaponsArray[0]=weapons.get(16);
        profileMap.put(characters.get(4),weaponsArray);

        save();

        //inputing all the data into the treeset for preparing the beginning of the game

	}

}

class Character implements Serializable{
	
	//declaring all the character stats and variables
	
	private String Name; //determines name of character
	private ArrayList<String> Skills= new ArrayList<String>(); //skills help determine special abilities
	private ArrayList<String> WeaponRanks= new ArrayList<String>(); //determines weapons usable, and weapon skills
	private ArrayList<String> WeaponTypes= new ArrayList<String>(); //determines types of weapons usable
	
	private int HPMAX; //health points
	private int STR; //strength, combines with might of weapon in order to determine attack power
	private int MAG; //magic, combines with might of magic tome in order to determine attack power
	private int SKL; //skill, helps determine hit rate
	private int SPD; //speed, will help determine avoidance
	private int DEF; //defense; attack power subtracted by defense to determine damage done to character
	private int MDEF; //magical defense; magic power subtracted by this to determine damage done to character
	private int BLD; //build; the bigger the build, bigger weapons can be used without speed loss
	
	private int WPNTYPES; //number that determines how many types of weapons can be used
	private int SKLNUM; //number of skills a character has
	
	private int EXP; //experience that will determine level ups
	
	private int HP; //will be the variable that is changed
	
	public Character(String characterStat) { //constructor for character class
		
		String[] data = characterStat.split(",");
		
		this.Name=data[0]; //assigning values and initializing variables
		this.HPMAX=Integer.parseInt(data[1]);
		this.STR=Integer.parseInt(data[2]);
		this.MAG=Integer.parseInt(data[3]);
		this.SKL=Integer.parseInt(data[4]);
		this.SPD=Integer.parseInt(data[5]);
		this.DEF=Integer.parseInt(data[6]);
		this.MDEF=Integer.parseInt(data[7]);
		this.BLD=Integer.parseInt(data[8]);
		this.WPNTYPES=Integer.parseInt(data[9]);
		
		for (int i=0;i<WPNTYPES;i++) {
			WeaponRanks.add(data[9+i+1]);//adds the ranks accordingly
		}
		
		for (int i=0;i<WPNTYPES;i++) {
			WeaponTypes.add(data[9+WPNTYPES+i+1]); //adds the types of weapons usable
		}
		
		int tempPos=9+(WPNTYPES*2)+1; //will be used to continue, so that calculations to get positions aren't messy
		
		SKLNUM=Integer.parseInt(data[tempPos]); //the number of skills a character has
		
		for (int i=0;i<SKLNUM;i++) {
			Skills.add(data[tempPos+i+1]); //adds the skills that each character has
		}
		
		this.EXP=Integer.parseInt(data[tempPos+SKLNUM+1]);
		
		HP=HPMAX; //assigns the original health of a character to the max health
		
	}
	
	//METHODS TO GET ACCESS TO INFORMATION
	
	public int getHealth() {
		return HP;
	}
	
	public int getHealthMax() {
		return HPMAX;
	}
	
	public int getStrength() {
		return STR;
	}
	
	public int getMagic() {
		return MAG;
	}
	
	public int getSkill() {
		return SKL;
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
	
	//METHODS TO ALTER INFORMATIONS
	
	public void changeHealth(int number, String gainlost) {
		if (gainlost.equals("up")) {
			HP+=number;
			if (HP>HPMAX) {
				HP=HPMAX; //character caps health without going over
			}
		}
		else {
			HP-=number;
			if (HP<0) {
				HP=0; //character dies
			}
		}
	}
	
	public void upHealthMax() { //will boost the health cap for a character during a level up
		HPMAX+=1;
	}
	
}

class Weapons implements Serializable{
	
	private String Name; //determines type of weapon
	private String Type; //determines what item its made out of, e.g. silver or iron, or anima and light
	private String weaponRank; //determines rank of weapon required to wield, from E to A
	private int Value; //will determine its value, for sale and buying cost
	private int Weight; //the weight of the weapon, when subtracted from build, if a negative number comes out, it will decrease speed
	
	private int STR; //will be used to determine might of weapon
	
	public Weapons(String weaponStat) { //constructor for weapon class
		
		String[] data= weaponStat.split(",");
		
		this.Name=data[0]; //assigning values and initializing variables
		this.Type=data[1];
		this.weaponRank=data[2];
		this.Value=Integer.parseInt(data[3]);
		this.Weight=Integer.parseInt(data[4]);
		this.STR=Integer.parseInt(data[5]);
		
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
	
	//METHODS TO ALTER INFORMATIONS
	
}

