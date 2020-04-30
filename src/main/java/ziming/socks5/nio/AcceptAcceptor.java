package ziming.socks5.nio;

import ziming.socks5.exception.ServiceException;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

// 负责接收连接的类, 接收完立即初测, 读操作触发 Handler551
public class AcceptAcceptor implements Runnable{
    private Selector selector;
    private ServerSocketChannel ssc;

    public AcceptAcceptor(Selector selector, ServerSocketChannel ssc) {
        this.selector = selector;
        this.ssc = ssc;
    }

    @Override
    public void run() {
        try {
            SocketChannel socket = ssc.accept();
            socket.configureBlocking(false);
            SelectionKey keyClient = socket.register(selector, SelectionKey.OP_ACCEPT);
            keyClient.attach(new NioHandler(keyClient));

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("AcceptAcceptor");
        }
    }
}
