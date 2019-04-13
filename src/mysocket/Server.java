package mysocket;

import controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ExchangeThread serverExchangeThread;

    public static int thePort =-1;

    public static void Init(int PORT) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket ss = new ServerSocket(PORT);
                    System.out.println("端口号" + PORT + ",服务器已启动");
                    Socket s = ss.accept();
                    serverExchangeThread = new ExchangeThread(s);
                    GameController.getTheGameController().exchangeThread =serverExchangeThread;
                    GameController.getTheGameController().connectDidSucceed();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

    }

    public static ExchangeThread getExchangeThread() {
        return serverExchangeThread;
    }
}
