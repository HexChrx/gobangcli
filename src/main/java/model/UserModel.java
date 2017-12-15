package model;

/**
 * Created by baidu on 2017/12/15.
 */
public class UserModel {

    private static UserModel instance;

    private UserModel() {}

    public static UserModel getInstance() {
        if (UserModel.instance == null) {
            UserModel.instance = new UserModel();
        }
        return UserModel.instance;
    }

    private String uid;
    private String name;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
