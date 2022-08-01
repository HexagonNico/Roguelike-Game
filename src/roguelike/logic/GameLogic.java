package roguelike.logic;

import java.util.Random;

import javax.swing.Timer;

import roguelike.gui.Renderer;
import roguelike.logic.entities.EntityTile;
import roguelike.logic.entities.Monster;
import roguelike.logic.entities.Player;
import roguelike.logic.items.Item;
import roguelike.logic.level.Floor;
import roguelike.logic.level.Tile;
import roguelike.logic.level.Tower;
import roguelike.logic.text.MessageBox;
import roguelike.resources.Items;
import roguelike.resources.Textures;

public class GameLogic {

    private static Timer timer;

    private static Random randomizer;

    private static Player player;
    private static Tower tower;
    private static Floor currentFloor;
    private static Monster[] activeMonsters;
    private static MessageBox messageBox;

    private static boolean onTitleScreen;

    /**Call at beginning of main
     * Initializes objects and read files*/
    public static void startGame() {
        Textures.init();
        init();
        timer = new Timer(20, new GameLoop());
        timer.start();
    }

    private static void init() {
        randomizer = new Random();
        tower = new Tower(randomizer);
        currentFloor = tower.getFloor(0);
        player = new Player("player", 2, 6);
        activeMonsters = currentFloor.getMonsters();
        messageBox = new MessageBox();
        onTitleScreen = true;
    }

    /**Called at beginning of GameLoop#actionPerformed<br>
     * Decreases messageBoxTime
     */
    public static void genericLoop() {
        player.decreaseMotionOffset();
        for(Monster monster : activeMonsters) {
            monster.decreaseMotionOffset();
        }
    }

    /**Changes player position of given dirX and dirY
     * @param dirX - Movement on X axis
     * @param dirY - Movement on Y axis
     */
    public static void movePlayer(int dirX, int dirY) {
        onTitleScreen = false;

        if(player.isInventoryOpen())
            return;

        if(checkIfPlayerDied())
            return;

        if(detectMonsterToFight(dirX, dirY)) {
            checkIfPlayerDied();
            return;
        }

        switch (getTileInFrontOfEntity(player, dirX, dirY).getName()) {
            case "floor" -> player.setPosition(player.getPosX() + dirX, player.getPosY() + dirY, true);
            case "wall", "torch" -> messageBox.addMessage("You ran into a wall!", 20);
            case "stairs" -> {
                currentFloor = tower.getNextFloor();
                player.setPosition(currentFloor.getStartPosX(), currentFloor.getStartPosY(), false);
                activeMonsters = currentFloor.getMonsters();
                messageBox.addMessage("You went into a new floor!", 100);
                player.addFloorCleared();
            }
            case "trap" -> {
                player.damage(randomizer.nextInt(2) + 1);
                player.setPosition(player.getPosX() + dirX, player.getPosY() + dirY, true);
                currentFloor.disarmTrap(player.getPosX(), player.getPosY());
                messageBox.addMessage("You ran into a trap and you took damage!", 100);
            }
            case "locked_door" -> messageBox.addMessage("You need a key to open this door...", 100);
        }
        moveMonsters();
        checkIfPlayerDied();
    }

    /**Gets tile in current floor in front of player
     * @param dirX - +1 or -1 if player moves on x axis
     * @param dirY - +1 or -1 if player moves on y axis
     * @return the tile at pos (player.posX+dirX;player.posY+dirY)
     */
    private static Tile getTileInFrontOfEntity(EntityTile entity, int dirX, int dirY) {
        return currentFloor.getTileAt(entity.getPosX()+dirX, entity.getPosY()+dirY);
    }

    public static void openPlayerInventory() {
        if(player.getHealth() > 0)
            player.setInventoryOpen(!player.isInventoryOpen());
    }

    /**Called when 'E' key is pressed<br>
     * Tests if there's a collectible around the player and take it
     */
    public static void handleInteration() {
        System.out.println("[Main][GameLogic]: Looking for an item to pickup");
        pickupItem(player.getPosX()+1, player.getPosY());
        pickupItem(player.getPosX()-1, player.getPosY());
        pickupItem(player.getPosX(), player.getPosY()+1);
        pickupItem(player.getPosX(), player.getPosY()-1);
    }

    private static void pickupItem(int itemPosX, int itemPosY) {
        switch(currentFloor.getTileAt(itemPosX, itemPosY).getName()) {
            case "red_potion" -> {
                if (player.giveItem(Items.HP_POTION)) {
                    currentFloor.removeCollectible(itemPosX, itemPosY);
                    messageBox.addMessage("You picked up a red potion!",100);
                } else {
                    messageBox.addMessage("Your inventory is full!", 50);
                }
            }
            case "gold_bag" -> {
                int g = randomizer.nextInt(5) + 3;
                player.giveGold(g);
                currentFloor.removeCollectible(itemPosX, itemPosY);
                messageBox.addMessage("You picked up a bag containing " + g + " gold!", 100);
            }
            case "key" -> {
                if (player.giveItem(Items.KEY)) {
                    currentFloor.removeCollectible(itemPosX, itemPosY);
                    messageBox.addMessage("You picked up a key!", 100);
                } else {
                    messageBox.addMessage("Your inventory is full!", 50);
                }
            }
            case "purple_potion" -> {
                if (player.giveItem(Items.STR_FRUIT)) {
                    currentFloor.removeCollectible(itemPosX, itemPosY);
                    messageBox.addMessage("You picked up a purple fruit!", 100);
                } else {
                    messageBox.addMessage("Your inventory is full!", 50);
                }
            }
            case "lime_potion" -> {
                if (player.giveItem(Items.DEF_POTION)) {
                    currentFloor.removeCollectible(itemPosX, itemPosY);
                    messageBox.addMessage("You picked up a lime potion!", 100);
                } else {
                    messageBox.addMessage("Your inventory is full!", 50);
                }
            }
            case "green_potion" -> {
                if (player.giveItem(Items.POISON)) {
                    currentFloor.removeCollectible(itemPosX, itemPosY);
                    messageBox.addMessage("You picked up a green potion!", 100);
                } else {
                    messageBox.addMessage("Your inventory is full!", 50);
                }
            }
            case "yellow_potion" -> {
                if (player.giveItem(Items.MAX_POTION)) {
                    currentFloor.removeCollectible(itemPosX, itemPosY);
                    messageBox.addMessage("You picked up a yellow potion!", 100);
                } else {
                    messageBox.addMessage("Your inventory is full!", 50);
                }
            }
            case "chest" -> {
                switch (randomizer.nextInt(12)) {
                    case 0 -> {
                        player.equipWeapon(Items.SHORT_SWORD);
                        messageBox.addMessage("You found a new weapon!", 120);
                    }
                    case 1 -> {
                        player.equipArmor(Items.LIGHT_ARMOR);
                        messageBox.addMessage("You found a new armor!", 120);
                    }
                    case 2 -> {
                        player.equipWeapon(Items.STICK);
                        messageBox.addMessage("You found a new weapon!", 120);
                    }
                    case 3 -> {
                        player.equipArmor(Items.BRONZE_ARMOR);
                        messageBox.addMessage("You found a new armor!", 120);
                    }
                    case 4 -> {
                        player.equipWeapon(Items.AXE);
                        messageBox.addMessage("You found a new weapon!", 120);
                    }
                    case 5 -> {
                        player.equipArmor(Items.MEDIEVAL_ARMOR);
                        messageBox.addMessage("You found a new armor!", 120);
                    }
                    case 6 -> {
                        player.equipWeapon(Items.GREAT_SWORD);
                        messageBox.addMessage("You found a new weapon!", 120);
                    }
                    case 7 -> {
                        player.equipArmor(Items.MISTERIOUS_ARMOR);
                        messageBox.addMessage("You found a new armor!", 120);
                    }
                    case 8, 9, 10, 11 -> {
                        int gold = randomizer.nextInt(9) + 6;
                        player.giveGold(gold);
                        messageBox.addMessage("You opened the chest and found " + gold + " gold!", 100);
                    }
                }
                currentFloor.removeCollectible(itemPosX, itemPosY); //<----- LO REMUEVE DE LA ESCENA DE TODOS MODOS INCLUSIVE SI EL INVENTARIO ESTA LLENO
            }
        }
    }

    /**Called in Mouse#mouseReleased<br>
     * Checks if inventory is open and uses an item
     * @param mouseX - Mouse position x
     * @param mouseY - Mouse position y
     */
    public static void handleLeftClick(int mouseX, int mouseY) {
        System.out.println("[Main][GameLogic]: Handling mouse click");
        if(player.isInventoryOpen()) {
            if(Renderer.inventorySlot1.contains(mouseX, mouseY)) {
                usePlayerItem(0);
            }else if(Renderer.inventorySlot2.contains(mouseX, mouseY)) {
                usePlayerItem(1);
            }else if(Renderer.inventorySlot3.contains(mouseX, mouseY)) {
                usePlayerItem(2);
            }
        }

        if(player.getHealth() <= 0) {
            init();
        }
    }

    private static void usePlayerItem(int index) {
        Item item = player.getInventoryItem(index);

        if(item == null) return;

        if(item == Items.HP_POTION) {
            player.heal(10);
            messageBox.addMessage("You drank a red potion and you recovered health!", 150);
        }
        else if(item == Items.KEY) {
            if(currentFloor.getTileAt(player.getPosX()+1, player.getPosY()).getName() .equals("locked_door"))
                currentFloor.openDoor(player.getPosX()+1, player.getPosY());
            else if(currentFloor.getTileAt(player.getPosX()-1, player.getPosY()).getName().equals("locked_door"))
                currentFloor.openDoor(player.getPosX()-1, player.getPosY());
            else if(currentFloor.getTileAt(player.getPosX(), player.getPosY()+1).getName().equals("locked_door"))
                currentFloor.openDoor(player.getPosX(), player.getPosY()+1);
            else if(currentFloor.getTileAt(player.getPosX(), player.getPosY()-1).getName().equals("locked_door"))
                currentFloor.openDoor(player.getPosX(), player.getPosY()-1);
            else {
                messageBox.addMessage("You can't use this item this way...", 100);
                return;
            }
            messageBox.addMessage("You used the key to open the door!", 150);
        }
        else if(item == Items.STR_FRUIT) {
            player.addStrengthBuff();
            messageBox.addMessage("You ate a strength fruit! Strength temporary increased!", 150);
        }
        else if(item == Items.POISON) {
            player.damage(5);
            messageBox.addMessage("You drank a bottle of poison and it wasn't a good idea!", 150);
        }
        else if(item == Items.DEF_POTION) {
            player.addDefenceBuff();
            messageBox.addMessage("You drank a defence potion! Defence temporary increased!", 150);
        }
        else if(item == Items.MAX_POTION) {
            player.increaseHealth(5);
            messageBox.addMessage("Your max HP was increased of 5 units!", 150);
        }
        player.removeItem(index);
    }

    private static void tookDamage(Monster monster){
        messageBox.addMessage("A monster attacked you!", 80);
        player.damage(monster.getStrength() - player.getDefence()/3);
        if(player.damageArmor())
            messageBox.addMessage("Your armor broke!", 50);
    }

    private static void moveMonsters() {
        for(Monster monster : activeMonsters) {
            if(monster.getHealth() <= 0)
                continue;

            if(!monster.shouldChasePlayer()) {
                int dx = 0;
                int dy = 0;
                switch (randomizer.nextInt(4)) {
                    case 0 -> {
                        dx = 1;
                        dy = 0;
                    }
                    case 1 -> {
                        dx = -1;
                        dy = 0;
                    }
                    case 2 -> {
                        dx = 0;
                        dy = 1;
                    }
                    case 3 -> {
                        dx = 0;
                        dy = -1;
                    }
                }

                if (currentFloor.thereIsMonsterHere(monster.getPosX() + dx, monster.getPosY() + dy))
                    return;
                else if (monster.getPosX() + dx == player.getPosX() && monster.getPosY() + dy == player.getPosY()) {
                    tookDamage(monster);
                    break;
                }
                if (getTileInFrontOfEntity(monster, dx, dy).getName().equals("floor")) {
                    monster.setPosition(monster.getPosX() + dx, monster.getPosY() + dy, true);
                    break;
                }

            } else {
                float angCoeff = -((float)player.getPosY()-(float)monster.getPosY())/((float)player.getPosX()-(float)monster.getPosX());

                int dx = 0;
                int dy = 0;

                if(angCoeff>-1 && angCoeff<1 && player.getPosX()>monster.getPosX()) {
                    dx = 1;
                    dy = 0;
                }
                else if(angCoeff>-1 && angCoeff<1 && player.getPosX()<monster.getPosX()) {
                    dx = -1;
                    dy = 0;
                }
                else if((angCoeff>1 || angCoeff<-1) && player.getPosY()>monster.getPosY()) {
                    dx = 0;
                    dy = 1;
                }
                else if((angCoeff>1 || angCoeff<-1) && player.getPosY()<monster.getPosY()) {
                    dx = 0;
                    dy = -1;
                }

                if(monster.getPosX() + dx == player.getPosX() && monster.getPosY() + dy == player.getPosY())
                    tookDamage(monster);
                else if(getTileInFrontOfEntity(monster, dx, dy).getName().equals("floor")){
                    monster.setPosition(monster.getPosX(), monster.getPosY() + dy, true);
                }
            }
        }
    }

    /**Checks if there is a monster around the player, then starts the fight
     * @param dirX - Direction the player wants to move on the X axis
     * @param dirY - Direction the player wants to move on the X axis
     * @return True if the fight happened, false if there was no monster nearby
     */
    private static boolean detectMonsterToFight(int dirX, int dirY) {
        Monster fight = currentFloor.getMonsterAt(player.getPosX()+dirX, player.getPosY()+dirY);
        if (fight == null)
            return false;

        fight.damage(player.getStrength() - fight.getDefence()/3);

        if(player.damageWeapon())
            messageBox.addMessage("Your weapon broke!", 50);

        if(fight.getHealth() <= 0) { //Monster is dead
            currentFloor.killMonster(fight.getPosX(), fight.getPosY());
            int g = randomizer.nextInt(12)+8;
            player.giveGold(g);
            messageBox.addMessage("You killed a monster and it dropped "+g+" gold!", 80);
        } else { //Monster is still alive after attack
            messageBox.addMessage("You attacked the monster and he attacked you back!", 80);
            player.damage(fight.getStrength() - player.getDefence()/3);
            if(player.damageArmor())
                messageBox.addMessage("Your armor broke!", 50);
        }

        if(dirX > 0) //Has attacked a monster on its left
            player.setMotionOffset(-16, 0);
        else if(dirX < 0) //Has attacked a monster on its right
            player.setMotionOffset(16, 0);
        else if(dirY > 0) //Has attacked a monster above
            player.setMotionOffset(0, -16);
        else if(dirY < 0) //Has attacked a monster below
            player.setMotionOffset(0, 16);
        return true;
    }

    private static boolean checkIfPlayerDied() {
        if (player.getHealth() > 0)
            return false;
        messageBox.addMessage("You perished in the dungeon!", 600);
        return true;
    }

    public static Player getPlayer() {
        return player;
    }

    public static Floor getCurrentFloor() {
        return currentFloor;
    }

    public static Monster[] getMonsters() {
        return activeMonsters;
    }

    public static MessageBox getMessageBox() {
        return messageBox;
    }

    public static boolean isOnTitleScreen() {
        return onTitleScreen;
    }
}
