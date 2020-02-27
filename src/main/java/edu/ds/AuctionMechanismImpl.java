package edu.ds;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuctionMechanismImpl implements AuctionMechanism {

    final private Peer peer;
    final private PeerDHT dht;
    private PeerAddress ID;
    final private int DEFAULT_MASTER_PORT = 4000;


    public AuctionMechanismImpl(int _ID, String _masterPeer) throws Exception {
        peer = new PeerBuilder(Number160.createHash(_ID)).ports(DEFAULT_MASTER_PORT + _ID).start();
        dht = new PeerBuilderDHT(peer).start();
        FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(_masterPeer)).ports(DEFAULT_MASTER_PORT).start();
        fb.awaitUninterruptibly();
        if (fb.isSuccess()) {
            peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
            ID = dht.peer().peerAddress();
        } else {
            throw new Exception("BOOTSTRAP ERROR!!!");
        }
    }

    public boolean createAuction(String _auction_name, Date _end_time, double _reserved_price, String _description) {
        try {
            if (_reserved_price <= 0 || _end_time.before(new Date())) return false;
            FutureGet futureGet = dht.get(Number160.createHash(_auction_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty()) {
                dht.put(Number160.createHash(_auction_name)).data(new Data(new Auction(_auction_name, _description, _end_time, _reserved_price, ID))).start().awaitUninterruptibly();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createAuctionWithBuyNowOption(String _auction_name, Date _end_time, double _reserved_price, String _description, double _buyNowPrice) {
        try {
            if (_reserved_price <= 0 || _end_time.before(new Date()) || _buyNowPrice <= _reserved_price) return false;
            FutureGet futureGet = dht.get(Number160.createHash(_auction_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty()) {
                dht.put(Number160.createHash(_auction_name)).data(new Data(new Auction(_auction_name, _description, _end_time, _reserved_price, ID, _buyNowPrice))).start().awaitUninterruptibly();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String checkAuction(String _auction_name) {
        try {
            FutureGet futureGet = dht.get(Number160.createHash(_auction_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty()) return "This auction does not exist.";
                String status = "";
                Auction auction = (Auction) futureGet.dataMap().values().iterator().next().object();
                if (auction.getEndTime().before(new Date())) {
                    //status += "This auction ended. Winning price: " + auction.getReservePrice() + ".";
                    if (!auction.isEndBuyItNow())
                        status += "This auction ended. Winning price: " + auction.getLastReservePrice() + ".";
                    else
                        status += "This auction ended. Winning price: " + auction.getReservePrice() + ".";
                    if (auction.getIDBestBidder().equals(ID))
                        status += "You are the winner.";
                } else {
                    if(auction.getIDBestBidder()==null)
                        status += "Starting bid: " + auction.getReservePrice() + ".";
                    else{
                        if(auction.getIDBestBidder().equals(ID)){
                            status += "You are the best binder, reserve price: " + auction.getReservePrice()+". ";

                        }
                        status += "Current bid: " + auction.getLastReservePrice();
                    }
                    if (auction.isBuyItNow())
                        status += "\nAuction has But It Now option. Price: " + auction.getBuyItNowPrice();
                    status += "\nAuction end: " + auction.getEndTime() + ".";
                    status += "\nAuction description: " + auction.getDescription() + ".";

                }
                return status;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String placeAbid(String _auction_name, double _bid_amount) {
        try {
            FutureGet futureGet = dht.get(Number160.createHash(_auction_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty()) return "This auction does not exist";
                Auction auction = (Auction) futureGet.dataMap().values().iterator().next().object();
                if (auction.getEndTime().before(new Date())) {
                    return "This auction ended. Check its status.";
                } else {
                    if (auction.getOwner().equals(ID))
                        return "You cannot place bids at your own auction.";
                    else if (auction.getReservePrice() >= _bid_amount) {
                        if (auction.getLastReservePrice() < _bid_amount) {
                            auction.incrementLastReservePrice(_bid_amount - auction.getLastReservePrice() + auction.getIncrement());
                            dht.put(Number160.createHash(_auction_name)).data(new Data(auction)).start().awaitUninterruptibly();
                            return "You didn't exceed the reserve price.";
                        }
                        return "Your bid must be higher";
                    } else {
                        auction.setReservePrice(_bid_amount);
                        auction.setLastReservePrice(auction.getLastReservePrice());
                        auction.setIDBestBidder(ID);
                        dht.put(Number160.createHash(_auction_name)).data(new Data(auction)).start().awaitUninterruptibly();
                        return "BID PLACED: " + _bid_amount;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String buyItNow(String _auction_name) {
        try {
            FutureGet futureGet = dht.get(Number160.createHash(_auction_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty()) return "This auction does not exist";
                Auction auction = (Auction) futureGet.dataMap().values().iterator().next().object();
                if (auction.getEndTime().before(new Date()))
                    return "This auction ended. Check its status.";
                if (auction.isBuyItNow()) {
                    if (auction.getOwner().equals(ID))
                        return "You cannot buy your own stuff.";
                    auction.setReservePrice(auction.getBuyItNowPrice());
                    auction.setEndBuyItNow(true);
                    auction.setIDBestBidder(ID);
                    auction.setEndTime(new Date());
                    dht.put(Number160.createHash(_auction_name)).data(new Data(auction)).start().awaitUninterruptibly();
                    return "Congratulation, you bought it!";
                } else {
                    return "This auction has no Buy It Now option";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteAuction(String _auction_name) {
        try {
            FutureGet futureGet = dht.get(Number160.createHash(_auction_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty()) return false;
                Auction auction = (Auction) futureGet.dataMap().values().iterator().next().object();
                if (!auction.getOwner().equals(ID)) return false;
                if (auction.getEndTime().before(new Date()))
                    if (auction.getIDBestBidder() != null) return false;
                return dht.remove(Number160.createHash(_auction_name)).start().awaitUninterruptibly().isSuccess();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
