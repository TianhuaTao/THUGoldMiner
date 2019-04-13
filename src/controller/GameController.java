package controller;

import model.*;
import mysocket.*;
import views.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.Timer;

public class GameController {

    private static GameController theGameController;
    private Timer timer;
    private Timer countDownTimer;
    public static int countDown = 60;
    int state;
    public static final int PLAYING = 1;
    public static final int PAUSE = 2;
    public static final int END = 9;
    public ExchangeThread exchangeThread;
    public Miner localMiner;
    public Miner remoteMiner;
    public boolean isServer;
    public boolean isClient;
    private Timer syncTimer;

    public static GameController getTheGameController() {
        return theGameController;
    }

    public GameController() {
        theGameController = this;
    }

    public ArrayList<Catchable> thingsToCatch = new ArrayList<Catchable>();

    public ArrayList<Catchable> getThingsToCatch() {
        return thingsToCatch;
    }

    public void keyDown() {
        System.out.println("keyDown");
        sendMsg("keyDown");
        sendMsg(Double.toString(localMiner.getHook().getTheta()));
        localMiner.shoot();
    }

    public void remoteKeyDown(double theta) {
        System.out.println("remote keyDown");
        remoteMiner.shoot(theta);
    }

    public int getState() {
        return state;
    }

    public void startGame() {
        setTimerCountDown();
        state = PLAYING;
    }

    public void restart() {
        sendMsg("restart");
        restart0();
    }

    public void restart0() {
        MainFrame.getMainFrame().setContentPane(new GamePanel());
        MainFrame.getMainFrame().validate();
        countDown = 60;
        thingsToCatch.clear();
        Location.clearHistory();
        Miner.reset();
        if (isClient) {
            localMiner = Miner.Miner2();
            remoteMiner = Miner.Miner1();
        } else if (isServer) {
            localMiner = Miner.Miner1();
            remoteMiner = Miner.Miner2();
        }
        initData();

    }

    public void initGame(boolean firstTime) {
        isServer = true;
        localMiner = Miner.Miner1();
        remoteMiner = Miner.Miner2();
        if (firstTime) {
            LaunchPanel.theLaunchPanel.isWaiting = true;
            LaunchPanel.theLaunchPanel.repaint();
        } else {
            Server.Init(Server.thePort + 1);
            this.exchangeThread = Server.getExchangeThread();
            exchangeThread.setGC(this);
        }
    }

    public void connectDidSucceed() {
        JPanel gamePanel = new GamePanel();
        MainFrame.getMainFrame().setContentPane(gamePanel);
        if (isServer)
            this.exchangeThread = Server.getExchangeThread();
        else if (isClient)
            this.exchangeThread = Client.getExchangeThread();
        MainFrame.getMainFrame().addKeyListener(new KeyController(this));
        this.initData();
        MainFrame.getMainFrame().validate();
    }

    public synchronized void syncData() {
        if (isServer) {
            sendMsg("sync");
            int i = 1;
            for (Catchable c : thingsToCatch) {
//                exchangeThread.sentObject(c);
                c.transfer();
                System.out.println("Object sent: " + i + " " + c.toString());
                i++;
            }
            exchangeThread.sendMsg("miner");
            localMiner.transfer();
            remoteMiner.transfer();
            exchangeThread.sendMsg("syncEnd");
//            exchangeThread.sentObject(thingsToCatch);
        } else if (isClient) {

        }
    }


    public void initData() {
        if (isServer) {
            for (int i = 0; i < 6; i++) {
                thingsToCatch.add(Gold.newRandomGold());
            }
            for (int i = 0; i < 3; i++) {
                thingsToCatch.add(new RandomBag());
            }
            for (int i = 0; i < 4; i++) {
                thingsToCatch.add(Stone.newRandomStone());
            }
            for (int i = 0; i < 3; i++) {
                thingsToCatch.add(new Animal());
            }

            sendMsg("init");
            int i = 1;
            for (Catchable c : thingsToCatch) {
                c.transfer();
                System.out.println("Object sent: " + i + " " + c.toString());
                i++;
            }
            exchangeThread.sendMsg("syncEnd");
            localMiner.setScore(0);
            remoteMiner.setScore(0);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        update();
//                        syncData();
                    } catch (Exception e) {

                    }
                }
            }, 0, 20);
            syncTimer = new Timer();
            syncTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    syncData();
                }
            }, 1000, 1000);
        } else if (isClient) {
            localMiner.setScore(0);
            remoteMiner.setScore(0);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        update();
                    } catch (Exception e) {

                    }
                }
            }, 0, 20);

        }
    }

    public void joinGame() {
        isClient = true;
        localMiner = Miner.Miner2();
        remoteMiner = Miner.Miner1();
    }

    void update() {
        if (state != PLAYING) return;

        Miner m1 = Miner.Miner1();
        Miner m2 = Miner.Miner2();
        m1.update();
        m2.update();

        for (Catchable c : thingsToCatch) {
            if (c instanceof Animal) {
                ((Animal) c).update();
            }
        }

        MainFrame.getMainFrame().repaint();
    }

    public void pause() {
        remotePause();
        exchangeThread.sendMsg("pause");
    }

    public void remotePause() {
        state = PAUSE;
        countDownTimer.cancel();
    }

    public void sendMsg(String msg) {
        exchangeThread.sendMsg(msg);
    }

    public void resume() {
        remoteResume();
        exchangeThread.sendMsg("resume");
    }



    public void setState(int state) {
        this.state = state;
    }

    public void remoteResume() {
        state = PLAYING;
        setTimerCountDown();
    }

    void setTimerCountDown() {
        System.out.println("setTimerCountDown() called");
        countDownTimer = new Timer();
        countDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameController.countDown--;
                if (GameController.countDown < 0) {
                    GameController.countDown = 0;
                    state = END;
                    timer.cancel();//stop update
                    checkWin();
                    this.cancel();  //stop countdown
                }
            }
        }, 1000, 1000);
    }

    void checkWin() {
        ArrayList<MinerRecord> list = null;
        if (isServer) {
            list = writeToFile(Miner.Miner1().getScore(), Miner.Miner2().getScore());
            if (list != null) {
                int i = 5;
                if (i > list.size()) i = list.size();
                sendMsg("record");
                sendMsg(Integer.toString(i));
                for (int j = 0; j < i; j++) {
                    sendMsg(list.get(j).toString());
                }
            }
        }
        MainFrame.getMainFrame().setContentPane(new GameOverPanel(list, localMiner.getScore(), remoteMiner.getScore()));
        MainFrame.getMainFrame().validate();
    }


    ArrayList<MinerRecord> writeToFile(int score1, int score2) {
        File recordFile = new File("miner_record.txt");
        try {
            ArrayList<MinerRecord> list = null;
            ObjectInputStream in = null;
            if (recordFile.exists()) {
                in = new ObjectInputStream(new FileInputStream("miner_record.txt"));
                list = (ArrayList<MinerRecord>) in.readObject();
            } else {
                list = new ArrayList<>();
            }
            if (in != null)
                in.close();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("miner_record.txt", false));
            Date now = new Date();
            MinerRecord mr1 = new MinerRecord(now, score1);
            MinerRecord mr2 = new MinerRecord(now, score2);
            list.add(mr1);
            list.add(mr2);
            list.sort(new Comparator<MinerRecord>() {
                @Override
                public int compare(MinerRecord o1, MinerRecord o2) {
                    return o1.compareTo(o2);
                }
            });
            out.writeObject(list);
            out.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}


