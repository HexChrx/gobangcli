package message;

import log.Log;
import model.MessageModel;
import model.UserModel;
import util.Json;

import java.util.*;

public class MessageProcs extends Thread{

    public final int LOGIN = 1;
    public final int SENDMSG = 2;
    public final int CREATEROOM = 3;

    private final Queue<MessageModel> messageList = new LinkedList<>();

    public boolean add(String json) {
        if (json == null || json.equals("")) {
            return false;
        }
        MessageModel message = (MessageModel) Json.decode(new MessageModel(), json);
        synchronized (this) {
            this.messageList.add(message);
        }
        return true;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            MessageModel message;
            synchronized (this) {
                message = this.messageList.poll();
            }
            if (message != null) {
                Log.logD(message.getContent());
                switch (message.getType()) {
                    case LOGIN:
                        if (message.getErrno() == 3000) {
                            UserModel userModel = UserModel.getInstance();
                            userModel.setUid(message.get("uid"));
                        }
                        break;
                }
            }
        }
    }
}
