package com.auction.containers;

public class PickOrderData {

    private int roundNo;
    private int pickNo;
    private int teamId;

    public PickOrderData(int roundNo, int pickNo, int teamId) {
        this.roundNo = roundNo;
        this.pickNo = pickNo;
        this.teamId = teamId;
    }

    public int getRoundNo() {
        return roundNo;
    }

    public int getPickNo() {
        return pickNo;
    }

    public int getTeamId() {
        return teamId;
    }
}
