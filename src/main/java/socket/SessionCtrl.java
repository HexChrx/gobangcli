package socket;

import java.util.HashMap;
import java.util.Map;


public class SessionCtrl {
    private Map<String, Session> sessionMap = new HashMap<>();
    private static SessionCtrl sessionCtrl;

    private SessionCtrl() {}

    public static SessionCtrl getInstence() {
        if (sessionCtrl == null) {
            sessionCtrl = new SessionCtrl();
        }
        return sessionCtrl;
    }

    public void sessionAdd(String key, Session session) {
        this.sessionMap.put(key, session);
    }

    public Session sessionRemove(String key) {
        return this.sessionMap.remove(key);
    }

    public Session getCommonSession() {
        return sessionMap.get("common");
    }
}
