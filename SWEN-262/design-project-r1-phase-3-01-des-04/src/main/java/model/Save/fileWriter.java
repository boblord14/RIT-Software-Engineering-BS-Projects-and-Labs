package model.Save;

public interface fileWriter {

    public boolean saveMapData(MapProfile gameProgress);

    public boolean saveUserProfile(UserProfile userAccount);


    public UserProfile loadUserProfile(String userName);


    public MapProfile loadMapData(String mapFileName);

    public String getFileType();

    public Boolean fileExists(String fileName);
}
