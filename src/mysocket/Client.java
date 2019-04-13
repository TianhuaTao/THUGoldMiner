package mysocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static ExchangeThread clientExchangeThread;

    public static InetAddress inetAddress;
    public static int thePort = -1;

    public static void Init(InetAddress add, int PORT) {
        try {
            Socket socket = new Socket(add, PORT);
            System.out.println("客户端IP:" + socket.getLocalAddress() + "端口" + socket.getPort());
            // 启动交流线程
            clientExchangeThread = new ExchangeThread(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ExchangeThread getExchangeThread() {
        return clientExchangeThread;
    }
}
