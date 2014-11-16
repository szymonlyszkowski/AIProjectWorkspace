package ai.dicewars.common;

/**
 * Created by Krzysiek on 2014-11-11.
 */
public class AnswerEx implements Answer {

    private Boolean isEmpty;
    private int from;
    private int to;

    public AnswerEx(Boolean isEmpty, int from, int to) {
        this.isEmpty = isEmpty;
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean isEmptyMove() {
        return isEmpty;
    }

    @Override
    public int getFrom() {
        return isEmpty ? 0 : from;
    }

    @Override
    public int getTo() {
        return isEmpty ? 0 : to;
    }
}
