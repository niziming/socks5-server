package ziming.socks5.aio.server;

import org.springframework.beans.factory.annotation.Autowired;
import ziming.socks5.aio.utils.Log;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * clint连接后的回调
 */
public class AioHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {
    @Autowired
    private Log log;
    @Override
    public void completed(AsynchronousSocketChannel result, AioServer attachment) {
        // 处理下一次client连接
        attachment.getServerSocketChannel().accept(attachment, this);

        // 执行业务逻辑
        doRead(result);
    }

    @Override
    public void failed(Throwable exc, AioServer attachment) {
        exc.printStackTrace();
    }

    /**
     * 读取client发送的信息到控制台
     * AIO中OS已经完成了readIO操作,所以只需要拿去结果
     * 服务端用于客户端通信的 channel
     * @param clientChannel
     */
    private void doRead(AsynchronousSocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        clientChannel.read(
                buffer, // 数据中转
                buffer, // 存储client发送的数据
                new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        log.log("收到客户端的数据长度为[%s]byte", attachment.capacity());
                        attachment.flip(); // 移动limit位置
                        // 读client发的数据
                        log.log("来自client数据:[%s]", new String(attachment.array(), StandardCharsets.UTF_8));
                        doWrite(clientChannel);
                    }
                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {

                    }
                }
        );
    }

    private void doWrite(AsynchronousSocketChannel clientChannel) {
        // 向client发数据 异步会立即返回
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        buffer.put(s.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        clientChannel.write(buffer);
        // clientChannel.write(buffer).get(); // 阻塞
    }
}
