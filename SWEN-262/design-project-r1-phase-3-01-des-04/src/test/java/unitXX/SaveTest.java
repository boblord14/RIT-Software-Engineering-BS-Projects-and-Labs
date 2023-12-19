package unitXX;

import model.PlayerCharacter;
import model.Save.UserProfile;
import model.map.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaveTest {
    private ProfileManager profileManager;
    private PlayerCharacter playerCharacter;
    private Map gameMap;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        playerCharacter = new PlayerCharacter("TestPlayer", "Test description");
        gameMap = new Map(playerCharacter, false);
        userProfile = new UserProfile(playerCharacter, gameMap);
        profileManager = new ProfileManager();
    }

    @Test
    void testSaveGameData() {
        Boolean testBool = profileManager.saveGameData(userProfile);
        assertEquals(testBool, true);
    }

    /*
     * Saves game data, and then loads the saved data
     * Tests if the loaded data matches the data that was saved
     */
    @Test
    void testLoadGameDataCharacter() {

        profileManager.saveGameData(userProfile);
        UserProfile loadedGameData = profileManager.loadGameData();
        assertEquals(playerCharacter.getName(), loadedGameData.getPlayerCharacter().getName());
        assertEquals(playerCharacter.getHealth(), loadedGameData.getPlayerCharacter().getHealth());
        assertEquals(playerCharacter.getArmor(), loadedGameData.getPlayerCharacter().getArmor());
        assertEquals(playerCharacter.getAttack(), loadedGameData.getPlayerCharacter().getAttack());
        assertEquals(playerCharacter.getWeapon(), loadedGameData.getPlayerCharacter().getWeapon());
    }

    @Test
    void testLoadGameDataMap() {

        profileManager.saveGameData(userProfile);
        UserProfile loadedGameData = profileManager.loadGameData();
        assertEquals(gameMap.getCurrentPlayerRoom().getWidth(), loadedGameData.getMap().getCurrentPlayerRoom().getWidth());
        assertEquals(gameMap.getCurrentPlayerRoom().getHeight(), loadedGameData.getMap().getCurrentPlayerRoom().getHeight());
        assertEquals(gameMap.getCurrentPlayerRoom().getDescription(), loadedGameData.getMap().getCurrentPlayerRoom().getDescription());
    }
}