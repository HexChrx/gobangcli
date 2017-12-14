package socket;

import log.Log;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class CommonSocket extends Thread{
    private SocketChannel socketChannel;
    private boolean stop = false;
    private int port = 8741;
    private String ip = "127.0.0.1";
    private Selector selector = null;
    private InetSocketAddress socketAddress = null;
    private Logger logger = Logger.getLogger(CommonSocket.class);

    private final int TIME_OUT = 1000;

    public CommonSocket() {
        socketConnet();
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                if (selector.select(TIME_OUT) > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    SelectionKey key;
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        Session session = (Session) key.attachment();
                        if (session != null) {
                            session.exec();
                        } else {
                            Session ses = new Session(socketChannel, selector);
                            key.attach(ses);
                            SessionCtrl.getInstence().sessionAdd("common", ses);
                            ses.exec();
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                Log.logE(e.getMessage());
            }
        }
    }

    private void socketBuilder() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Open to selector failed: IOException");
        }
    }

    private void openSocketChannel() {
        try {
            socketAddress = new InetSocketAddress(ip, port);
            socketChannel = SocketChannel.open();
            socketChannel.socket().setReuseAddress(true);
            socketChannel.connect(socketAddress);
        } catch (ClosedChannelException e) {
            logger.warn("Channel is closed: ClosedChannelException");
        } catch (IOException e) {
            logger
                    .warn("Connet is failed or time out,the system will automatically re-connected : IOException");
        }
    }

    /**
     * do ClientBuilder if socket conncte success
     */
    private void socketConnet() {
        try {
            openSocketChannel();
            if (socketChannel.isOpen()) {
                this.stop = true;
                socketBuilder();
                socketChannel.configureBlocking(false);
                SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ
                        | SelectionKey.OP_WRITE);
                Session commonSession = new Session(socketChannel, selector);
                key.attach(commonSession);
                SessionCtrl.getInstence().sessionAdd("common", commonSession);
                logger.info("Has been successfully connected to " + ip
                        + "and port:    " + port);
            } else {
                socketChannel.close();
            }
        } catch (ClosedChannelException e) {
            logger.warn("Channel is closed: ClosedChannelException");
        } catch (IOException e) {
            logger.warn("Connet is failed or time out,the system will automatically re-connected : IOException");
        }

    }
}


