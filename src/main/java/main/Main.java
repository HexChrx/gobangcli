
package main;

import message.MessageProcs;
import message.MessageSend;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import socket.CommonSocket;

public class Main {

    public static void main(String[] argv) {
        BeanFactory beanFactory = new XmlBeanFactory(
                new ClassPathResource("ApplicationContext.xml"));
        CommonSocket commonSocket = (CommonSocket)beanFactory.getBean("commonSocket");
        commonSocket.start();

        MessageProcs messageProcs = (MessageProcs) beanFactory.getBean("messageProcs");
        messageProcs.start();

        MessageSend messageSend = (MessageSend) beanFactory.getBean("messageSend");
        messageSend.start();
    }
}
