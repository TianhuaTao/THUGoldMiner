package resource;


import javax.sound.sampled.*;
import java.io.*;

public class GMSound {
    public static final int DIG = 1;
    public static final int GET = 2;
    public static final int CATCH = 3;
    public static final int PULL = 4;


    public static void playSound(int sound) {

        Thread soundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String soundName = "";
                switch (sound) {
                    case DIG:
                        soundName = "/res/sound/dig.wav";
                        break;
                    case GET:
                        soundName = "/res/sound/high-value.wav";
                        break;
                    case CATCH:
                        soundName = "/res/sound/normal-value.wav";
                        break;
                    case PULL:
                        soundName = "/res/sound/pull.wav";
                        break;
                }
                try {
                    InputStream inputStream = getClass().getResourceAsStream(soundName);
                    InputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    AudioInputStream in = AudioSystem.getAudioInputStream(bufferedInputStream);

                    boolean canplay = true;
                    AudioFormat outFormat = new AudioFormat(
                            AudioFormat.Encoding.PCM_SIGNED, in.getFormat().getSampleRate(),
                            16, in.getFormat().getChannels(), in.getFormat().getChannels() * 2,
                            in.getFormat().getSampleRate(), false);

                    DataLine.Info info =
                            new DataLine.Info(SourceDataLine.class, outFormat);
                    SourceDataLine line =
                            (SourceDataLine) AudioSystem.getLine(info);

                    if (line != null) {
                        line.open(outFormat);
                        line.start();
                        canplay = true;
                        final byte[] buffer = new byte[65536];
                        for (int n = 0; n != -1 && canplay;
                             n = AudioSystem
                                     .getAudioInputStream(outFormat, in)
                                     .read(buffer, 0, buffer.length)) {
                            line.write(buffer, 0, n);
                        }
                        line.drain();
                        line.stop();
                    }
                    line.close();
                    in.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        soundThread.start();

    }
}
