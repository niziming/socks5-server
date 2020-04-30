package ziming.socks5.aio.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ziming.socks5.aio.utils.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Component
public class AioClient {
    @Autowired
    private static Log log;

    private AsynchronousSocketChannel clientChannel;

    public AioClient() {}
    public AioClient(String host, int port) {
        init(host, port);
    }
    private void init(String host, int port){
        try {
            clientChannel = AsynchronousSocketChannel.open();
            clientChannel.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void doWrite(String line){
        // 向server发数据 异步会立即返回
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(line.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        clientChannel.write(buffer);
    }

    private void doRead() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try{
            clientChannel.read(buffer).get();
            buffer.flip();
            log.log("来自server[%s]", new String(buffer.array(), StandardCharsets.UTF_8));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void doDestory(){
        if (null != clientChannel) {
            try {
                clientChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        AioClient client = new AioClient("localhost", 8000);
        // try {
            log.log("输入信息发送到服务端");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            client.doWrite(s);
            client.doRead();
        // } finally {
        //     client.doDestory();
        // }
    }
}
