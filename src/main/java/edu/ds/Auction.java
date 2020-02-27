package edu.ds;

import java.io.Serializable;
import java.util.Date;

import net.tomp2p.peers.PeerAddress;

public class Auction implements Serializable{


    private static final long serialVersionUID = -3260659616951862700L;
    private String Name;
    private String Description;
    private Date EndTime;
    private double ReservePrice;
    private double LastReservePrice;
    private double Increment = 0.10;
    private PeerAddress IDBestBidder;
    private PeerAddress Owner;
    private boolean BuyItNow;
    private boolean EndBuyItNow = false;
    private double BuyItNowPrice;

    public Auction(String name, String description, Date endTime, double reservePrice, PeerAddress owner) {
        Name = name;
        Description = description;
        EndTime = endTime;
        ReservePrice = reservePrice;
        LastReservePrice = reservePrice;
        Owner = owner;
        IDBestBidder = null;
        BuyItNow = false;
    }

    public Auction(String name, String description, Date endTime, double reservePrice, PeerAddress owner, double buyItNowPrice) {
        Name = name;
        Description = description;
        EndTime = endTime;
        ReservePrice = reservePrice;
        LastReservePrice = reservePrice;
        Owner = owner;
        IDBestBidder = null;
        BuyItNow = true;
        BuyItNowPrice = buyItNowPrice;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public double getReservePrice() {
        return ReservePrice;
    }

    public PeerAddress getIDBestBidder() {
        return IDBestBidder;
    }

    public PeerAddress getOwner() {
        return Owner;
    }

    public String getDescription() {
        return Description;
    }

    public double getBuyItNowPrice() {
        return BuyItNowPrice;
    }

    public void setReservePrice(double reservePrice) {
        ReservePrice = reservePrice;
    }

    public void setIDBestBidder(PeerAddress IDBestBidder) {
        this.IDBestBidder = IDBestBidder;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public boolean isBuyItNow() {
        return BuyItNow;
    }

    public double getLastReservePrice() {
        return LastReservePrice + Increment;
    }

    public void setLastReservePrice(double lastReservePrice) {
        LastReservePrice = lastReservePrice;
    }

    public void incrementLastReservePrice(double lastReservePrice) {
        LastReservePrice += lastReservePrice;
    }

    public boolean isEndBuyItNow() {
        return EndBuyItNow;
    }

    public void setEndBuyItNow(boolean endBuyItNow) {
        EndBuyItNow = endBuyItNow;
    }

    public double getIncrement() {
        return Increment;
    }
}
