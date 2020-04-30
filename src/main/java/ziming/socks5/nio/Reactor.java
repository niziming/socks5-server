package ziming.socks5.nio;

import ziming.socks5.exception.ServiceException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {
    private Selector selector;
    private ServerSocketChannel ssc;

    public Reactor(int port) {
        try {
            // 初始化服务器和容器
            this.selector = Selector.open();
            this.ssc = ServerSocketChannel.open();
            // 绑定事件 端口 设置异步
            ssc.bind(new InetSocketAddress(port));
            ssc.configureBlocking(false);
            SelectionKey register = ssc.register(selector, SelectionKey.OP_ACCEPT);
            register.attach(new AcceptAcceptor(selector, ssc));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("容器初始失败" + this.getClass());
        }
    }

    public Reactor() {

    }

    @Override
    public void run() {
        while (true) {
            try {
                int size = selector.select();
                if (size == 0) continue;
            } catch (IOException e){
                e.printStackTrace();
                throw new ServiceException("Reactor.run()");
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                consume(next);
                iterator.remove();
            }
        }
    }

    private void consume(SelectionKey key) {
        Runnable attachment = (Runnable) key.attachment();
        attachment.run();
    }
}
