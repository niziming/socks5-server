package ziming.socks5.nio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ziming.socks5.aio.utils.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

@Component
public class NioHandler implements Runnable{
    private Selector selector;
    private SocketChannel sc;
    private SelectionKey keyClient;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    static byte bb510[] = {5,0};
    static Log log;

    public NioHandler(SelectionKey keyClient) {
        this.keyClient = keyClient;
        this.selector = keyClient.selector();
        sc = (SocketChannel) keyClient.channel();
    }
    public NioHandler (){}
    @Override
    public void run() {
        byteBuffer.clear();
        try{
            int read = sc.read(byteBuffer);
            if (read == -1) {
                keyClient.cancel();
                return;
            }
            log.log("得到的结果[%s]", Arrays.toString(byteBuffer.array()));
            sc.write(ByteBuffer.wrap(bb510));
            keyClient.attach(new Handler501002(keyClient));
        } catch (IOException e) {
            keyClient.cancel();
            selector.wakeup();
            e.printStackTrace();
        }
    }
    public void test(){
        log.log("[%s]", Arrays.toString(bb510));
    }
}
