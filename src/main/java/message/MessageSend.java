package message;

import log.Log;
import model.MessageModel;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import socket.CommonSocket;
import socket.Session;
import socket.SessionCtrl;
import util.Json;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MessageSend extends Thread{

    private Session session;

    public boolean send(String message) {
        session.sendData(message);
        return true;
    }

    public MessageSend () {

    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            System.out.println("请输入消息类型,1:lognin,2:senddata");
            Scanner scan = new Scanner(System.in);
            String type = scan.nextLine();
            MessageModel messageModel = null;
            switch (Integer.parseInt(type)) {
                case 1:
                    messageModel = this.getLogninInfo(scan);
                    break;
                case 2:
                    messageModel = this.getSendDataInfo(scan);
                    break;
            }
            if (messageModel != null) {
                Session session = SessionCtrl.getInstence().getCommonSession();
                session.sendData(Json.encode(messageModel));
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取登录信息
     * @param scanner Scanner
     * @return MessageModel
     */
    private MessageModel getLogninInfo(Scanner scanner) {
        System.out.println("请输入用户名：");
        String name = scanner.nextLine();
        while(name.equals("")) {
            System.out.println("用户名不能为空，请输入用户名：");
            name = scanner.nextLine();
        }
        System.out.println("请输入密码：");
        String password = scanner.nextLine();
        while(password.equals("")) {
            System.out.println("密码不能为空，请输入密码：");
            password = scanner.nextLine();
        }
        MessageModel messageModel = new MessageModel();
        messageModel.setType(1);
        messageModel.getContent().put("uid", name);
        messageModel.getContent().put("password", password);
        return messageModel;
    }

    public MessageModel getSendDataInfo(Scanner scanner) {
        System.out.println("请输入收件人id：");
        int uid = Integer.parseInt(scanner.nextLine());
        while(uid == 0) {
            System.out.println("收件人id不能为空，请输入收件人id：");
            uid = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("请输入消息内容：");
        String message = scanner.nextLine();
        while(message.equals("")) {
            System.out.println("消息内容不能为空，请输入消息内容：");
            message = scanner.nextLine();
        }
        MessageModel messageModel = new MessageModel();
        messageModel.setType(2);
        messageModel.getContent().put("to", String.valueOf(uid));
        messageModel.getContent().put("message", message);
        return messageModel;
    }
}
