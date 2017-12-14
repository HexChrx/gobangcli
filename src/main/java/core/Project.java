package core;


import java.util.HashMap;

public class Project extends Base{
    private HashMap<String, Object> projectIni = new HashMap<>();

    private static Project instence;

    private Project(){
        this.projectIni = this.config("project");
    }

    public static Project getInstence(){
        if (instence == null) {
            instence = new Project();
        }
        return instence;
    }

    public Object get(String key) {
        if (this.projectIni == null) {
            return null;
        }
        return this.projectIni.get(key);
    }
}
