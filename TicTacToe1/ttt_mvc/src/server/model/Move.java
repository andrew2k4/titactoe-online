package server.model;

public class Move {
    private byte c;

    private byte r;

    private byte turnPlayer;

    public byte getC() {
        return c;
    }

    public void setC(byte c) {
        this.c = c;
    }

    public byte getR() {
        return r;
    }

    public void setR(byte r) {
        this.r = r;
    }

    public byte getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(byte turnPlayer) {
        this.turnPlayer = turnPlayer;
    }
}
