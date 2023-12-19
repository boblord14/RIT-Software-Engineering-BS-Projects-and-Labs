package model.Drops;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.Stat;
import model.Items.Armor;
import model.Items.BagItem;
import model.Items.BaseItem;
import model.Items.Food;
import model.Items.ItemAttribute;
import model.Items.Rarity;
import model.Items.RarityModifier;
import model.Items.Weapon;
import model.Items.Buffer;

public class DropHelper {

    final static String dropsFile = "src\\main\\java\\model\\Drops\\Drops.csv";
    final static Probability dropsP = new Probability(dropsFile);

    final static String armorsFile = "src\\main\\java\\model\\Drops\\Armors.csv";
    final static Probability armorsP = new Probability(armorsFile);

    final static String weaponsFile = "src\\main\\java\\model\\Drops\\Weapons.csv";
    final static Probability weaponsP = new Probability(weaponsFile);

    final static String foodFile = "src\\main\\java\\model\\Drops\\Food.csv";
    final static Probability foodP = new Probability(foodFile);

    final static String buffersFile = "src\\main\\java\\model\\Drops\\Buffers.csv";
    final static Probability buffersP = new Probability(buffersFile);

    final static String rarityFile = "src\\main\\java\\model\\Drops\\Rarities.csv";
    final static Probability rarityP = new Probability(rarityFile);




    public static ItemAttribute getItem(int scalingFactor){
        ItemAttribute retItem = null;
        String itemType = dropsP.getRandomEvent();
        retItem = makeItem(itemType, scalingFactor);
        String rareStr = rarityP.getRandomEvent(scalingFactor);
        if (!rareStr.equalsIgnoreCase("common")){
            RarityModifier temp = new RarityModifier(Rarity.ParseString(rareStr));
            temp.setNext(retItem);
            retItem = temp;
        }
        return retItem;
    }

    private static BaseItem makeItem(String itemType, int scalingFactor){
        Map<String, String> props;
        BaseItem item = null;
        if (itemType.equalsIgnoreCase("Armor")){
            props = getItemProperties(armorsFile, armorsP.getRandomEvent(scalingFactor));
            return new Armor(Integer.parseInt(props.get("value")), props.get("name"), props.get("description"));
        } else if (itemType.equalsIgnoreCase("Weapon")){
            props = getItemProperties(weaponsFile, weaponsP.getRandomEvent(scalingFactor));
            return new Weapon(Integer.parseInt(props.get("value")), props.get("name"), props.get("description"));
        } else if (itemType.equalsIgnoreCase("Buffer")){
            props = getItemProperties(buffersFile, buffersP.getRandomEvent(scalingFactor));
            return new Buffer(Integer.parseInt(props.get("value")), props.get("name"), props.get("description"), Stat.ParseString(props.get("stat")));
        } else if (itemType.equalsIgnoreCase("Food")){
            props = getItemProperties(foodFile, foodP.getRandomEvent(scalingFactor));
            return new Food(Integer.parseInt(props.get("value")), props.get("name"), props.get("description"));
        } else if (itemType.equalsIgnoreCase("Bag")){
            return new BagItem(2);
        }
        return item;
    }

    private static Map<String, String>getItemProperties(String csvFilePath, String itemName){
        Map<String, String> props = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String[] propNames = reader.readLine().split(",");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equalsIgnoreCase(itemName)) {
                    for (int i = 0; i < values.length; i++) {
                        props.put(propNames[i], values[i]);
                    }
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return props;
    }

}
