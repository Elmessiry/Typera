package system;

import java.io.Serializable;

public class ScoreObject implements Serializable , Comparable {
    Long score;

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }


    @Override
    public int compareTo(Object o) {
        long compareScore=((ScoreObject)o).getScore();
        /* For Ascending order*/
        return (int) (this.score-compareScore);
    }
}
