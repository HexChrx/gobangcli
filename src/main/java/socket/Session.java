package socket;

import log.Log;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import javax.lang.model.element.QualifiedNameable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;


public class Session {
    private SocketChannel socketChannel;
    private SelectionKey selectionKey;
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(10240);
    private Charset charset = Charset.forName("UTF-8");
    private CharsetDecoder charsetDecoder = charset.newDecoder();
    private CharsetEncoder charsetEncoder = charset.newEncoder();
    private long lastPant;//最后活动时间
    private final int TIME_OUT = 1000 * 60 * 5; //Session超时时间
    private String key;
    private Queue<String> sendData;
    private String receiveData = null;

    private Session session;

    public Session(SocketChannel socketChannel, Selector selector) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        this.selectionKey = socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        this.selectionKey.attach(this);
        sendData = new LinkedList<>();
        selector.wakeup();
        this.lastPant = Calendar.getInstance().getTimeInMillis();
    }

    public void distory(){
        try {
            socketChannel.close();
            selectionKey.cancel();
        } catch (IOException e) {
            Log.logE(e.getMessage());
        }

    }

    public void exec() {
        if (this.selectionKey.isReadable()){
            this.read();
        } else if (this.selectionKey.isWritable()) {
            this.send();
        }
    }

    private synchronized void read() {
        this.receiveBuffer.clear();
        try {
            socketChannel.read(this.receiveBuffer);
            receiveBuffer.flip();
            this.receiveData = charset.decode(this.receiveBuffer).toString();
            if (!this.receiveData.equals("")) {
                System.out.println("收到的数据是：" + this.receiveData);
            }
            Log.logD(receiveData);
        } catch (IOException e) {
            Log.logE(e.getMessage());
            this.selectionKey.cancel();
        }
    }

    private void send() {
        try {
            if (this.socketChannel == null) {
                return;
            }

            if (sendData != null && sendData.size() > 0) {
                String message = sendData.poll();
                socketChannel.write(charsetEncoder.encode(CharBuffer.wrap(message)));
            }
            sendData = null;
            //selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public boolean isKeekAlive() {
        return lastPant + TIME_OUT > Calendar.getInstance().getTimeInMillis();
    }

    public void setAlive() {
        lastPant = Calendar.getInstance().getTimeInMillis();
    }


    public ByteBuffer getReceiveBuffer() {
        return receiveBuffer;
    }

    public void setReceiveBuffer(ByteBuffer receiveBuffer) {
        this.receiveBuffer = receiveBuffer;
    }

    public long getLastPant() {
        return lastPant;
    }

    public void setLastPant(long lastPant) {
        this.lastPant = lastPant;
    }

    public Queue<String> getSendData() {
        return sendData;
    }

    /**
     * 添加
     * @param sendData
     */
    public boolean sendData(String sendData) {
        if (this.sendData == null) {
            this.sendData = new LinkedList<>();
        }
        return this.sendData.add(sendData);
    }

    public String getReceiveData() {
        return receiveData;
    }

    public void setReceiveData(String receiveData) {
        this.receiveData = receiveData;
    }
}
