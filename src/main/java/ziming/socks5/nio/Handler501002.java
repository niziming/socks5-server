package ziming.socks5.nio;

import ziming.socks5.aio.utils.Log;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Handler501002 implements Runnable {
    private Selector selector;
    private SocketChannel sc;
    private SelectionKey keyClient;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(100);
    static byte bb501002[] = {5,0,0,1,0,0,0,0,1,1};
    static Log log;

    public Handler501002(SelectionKey keyClient) {
        this.keyClient = keyClient;
        this.selector = keyClient.selector();
        sc = (SocketChannel) keyClient.channel();
    }

    /**
     *
     */
    @Override
    public void run() {

    }
}
