package ziming.socks5.aio.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ziming.socks5.aio.utils.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class AioServer {
    @Autowired
    private Log log;

    private ExecutorService service;
    // 通道 aio channel
    private AsynchronousServerSocketChannel serverSocketChannel;

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }
    public ExecutorService getService() {
        return service;
    }

    public AioServer(int port) {
        init(port);
    }

    public AioServer() {}

    public void init(int port) {
        log.log("开启port[%s]", port);
        // 定义线程池
        service = Executors.newFixedThreadPool(4);
        try {
            // 初始化 Async
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            // 监听端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.accept(this, new AioHandler());
            try {
                // 阻塞程序防止为GC回收
                TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AioServer(8000);
    }
}
