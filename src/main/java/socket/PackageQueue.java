package socket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baidu on 2017/12/11.
 */
public class PackageQueue {
    private static  List<byte[]> queue = new ArrayList<byte[]>();

    public PackageQueue(){
    }

    public void pushMsgs(byte[] array){
        synchronized(queue){
            queue.add(array);
        }
    }

    public byte[] takeMsgs() {
        synchronized (queue) {
            byte[] sd=null;
            if(queue != null){
                if(queue.size() > 0){
                    sd = queue.get(0);
                    queue.remove(0);
                }
            }
            return sd;
        }

    }

    public static List<byte[]> getQueue() {
        return queue;
    }

    public static void setQueue(List<byte[]> queue) {
        PackageQueue.queue = queue;
    }
}
