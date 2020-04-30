package ziming.socks5.nio;

public class Main {
    public static void main(String[] args) {
        Reactor reactor = new Reactor(5000);
        reactor.run();
    }

}
