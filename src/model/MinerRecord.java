package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MinerRecord implements Comparable<MinerRecord>, Serializable {
    Date date;
    int score;

    public MinerRecord(Date d, int score) {
        date = d;
        this.score = score;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy.MM.dd 'at' hh:mm");
        String dateString = sdf.format(date);
        String line1 = "Score: " + Integer.toString(score) + " Time: " + dateString;
        return line1;
    }


    @Override
    public int compareTo(MinerRecord o) {
        return o.score - score;
    }
}
