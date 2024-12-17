package model;

public class PairServer {
    private long id;
    private byte c;
    private byte r;
    private byte currentPlayer;

    // Getters et Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public byte getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(byte currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String toString() {
        return "PairServer{id=" + id + ", c=" + c + ", r=" + r + ", currentPlayer=" + currentPlayer + "}";
    }
}

