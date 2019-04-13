package mysocket;

import controller.GameController;
import model.*;
import views.GameOverPanel;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ExchangeThread implements Runnable {

    private Socket socket;
    GameController gc;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public void setGC(GameController gc) {
        this.gc = gc;
    }

    public ExchangeThread(Socket socket) {
        this.socket = socket;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("A new thread started!");
        new Thread(this).start();
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        gc = GameController.getTheGameController();
        try {
            while (true) {
                String line = bufferedReader.readLine();
                System.out.println("message received: " + line);
                if (line.equals("pause")) {
                    gc.remotePause();
                } else if (line.equals("sync")) {
                    sync();
                } else if (line.equals("resume")) {
                    gc.remoteResume();
                } else if (line.equals("keyDown")) {
                    double t = Double.parseDouble(bufferedReader.readLine());
                    gc.remoteKeyDown(t);
                } else if (line.equals("catch")) {
//                    Catchable c = (Catchable) ois.readObject();
//                    if (gc.getThingsToCatch().contains(c)) {
//                        gc.getThingsToCatch().remove(c);
//                        Miner.Miner2().getHook().didCatch(c);
//                    }
                } else if (line.equals("restart")) {
                    gc.restart0();
                } else if (line.equals("END")) {
//                    gc.setState(GameController.END);
//                    gc.restart0();
                } else if (line.equals("array")) {
//                    int length = Integer.parseInt(bufferedReader.readLine());
                } else if (line.equals("init")) {
//                    for (int i = 0; i < 6 + 3 + 4 + 3; i++) {
//                        try {
//                            Object oo = ois.readObject();
//                            System.out.println("Object received: " + (i + 1) + " " + oo.toString());
//                            gc.getThingsToCatch().add((Catchable) oo);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                    sync();
                    sendMsg("good");
                    gc.startGame();
                } else if (line.equals("good")) {
                    gc.startGame();
                } else if (line.equals("record")) {
                    int i = Integer.parseInt(bufferedReader.readLine());
                    ArrayList<String> recordList = new ArrayList<>();
                    for (int j = 0; j < i; j++) {
                        recordList.add(bufferedReader.readLine());
                    }
                    GameOverPanel.theGameOverPanel.setRecordStringArray(recordList);
                    GameOverPanel.theGameOverPanel.repaint();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    socket = null;
                    e.printStackTrace();
                }
            }
        }

    }

    public synchronized void sync() {
        gc = GameController.getTheGameController();
        ArrayList<Catchable> newList = new ArrayList<>();
        try {
            while (true) {
                String line = bufferedReader.readLine();
                if (line.equals("syncEnd")) {
                    break;
                } else if (line.equals("gold")) {
                    syncGold(newList);
                } else if (line.equals("stone")) {
                    syncStone(newList);
                } else if (line.equals("animal")) {
                    syncAnimal(newList);
                } else if (line.equals("bag")) {
                    syncBag(newList);
                } else if (line.equals("miner")) {
                    syncMiner();
                }
            }
        } catch (Exception e) {

        }
        gc.thingsToCatch = newList;

    }

    synchronized void syncMiner() {
        try {
            int remoteScore = Integer.parseInt(bufferedReader.readLine());
            boolean on1 = Boolean.getBoolean(bufferedReader.readLine());
            int localScore = Integer.parseInt(bufferedReader.readLine());
            boolean on2 = Boolean.getBoolean(bufferedReader.readLine());
            gc.localMiner.setScore(localScore);
            gc.localMiner.getHook().setOnHook(on1);
            gc.remoteMiner.setScore(remoteScore);
            gc.remoteMiner.getHook().setOnHook(on2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void syncGold(ArrayList<Catchable> l) {
        try {
            double x = Double.parseDouble(bufferedReader.readLine());
            double y = Double.parseDouble(bufferedReader.readLine());
            int size = Integer.parseInt(bufferedReader.readLine());
            Gold g = new Gold(x, y, size);
            l.add(g);
            System.out.println("Gold received.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void syncStone(ArrayList<Catchable> l) {
        try {
            double x = Double.parseDouble(bufferedReader.readLine());
            double y = Double.parseDouble(bufferedReader.readLine());
            Stone s = new Stone(x, y);
            l.add(s);
            System.out.println("Stone received.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void syncAnimal(ArrayList<Catchable> l) {
        try {
            double x = Double.parseDouble(bufferedReader.readLine());
            double y = Double.parseDouble(bufferedReader.readLine());
            int direc = Integer.parseInt(bufferedReader.readLine());
            Animal ani = new Animal(x, y, direc);
            l.add(ani);
            System.out.println("Animal received.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void syncBag(ArrayList<Catchable> l) {
        try {
            double x = Double.parseDouble(bufferedReader.readLine());
            double y = Double.parseDouble(bufferedReader.readLine());
            int value = Integer.parseInt(bufferedReader.readLine());
            double speed = Double.parseDouble(bufferedReader.readLine());
            RandomBag bag = new RandomBag(x, y, value, speed);
            l.add(bag);
            System.out.println("Bag received.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveArr() {
//        try {
//            Catchable arr[] = ois.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public synchronized void sentObject(Object o) {
        try {
            oos.writeObject(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendArr(Catchable[] arr) {
//        sendMsg("array");
//        sendMsg(Integer.toString(arr.length));
//        try {
//            for (int i = 0; i < arr.length; i++) {
//                oos.writeObject(arr[i]);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void sendMsg(String msg) {
        try {
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
