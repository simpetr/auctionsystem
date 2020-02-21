import edu.ds.AuctionMechanismImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class AuctionMechanismTest {

    private AuctionMechanismImpl peer0, peer1, peer2, peer3;

    @Test
    public void List() throws Exception {
        peer0 = new AuctionMechanismImpl(0, "127.0.0.1");
        peer1 = new AuctionMechanismImpl(1, "127.0.0.1");
        peer2 = new AuctionMechanismImpl(2, "127.0.0.1");
        peer3 = new AuctionMechanismImpl(3, "127.0.0.1");

        String description = "Test description name";
        Date date = getDate(3, 3, 2020, 11, 30);

        CreateAuction("Test Auction name", date, 5, description);
        CreateAuction("Test Auction name No Option", date, 10, description);
        CreateAuctionWrongPrice("Test Auction 2 name", date, 0, description);
        CreateAuctionWithOption("Test Auction 3 name", date, 20, description, 200);
        CreateAuctionWithOptionWrongPrice("Test Auction 4 name", date, 150, description, 50);

        AuctionBuyItNow("Test Auction 3 name");
        BuyAuctionWithNoOption("Test Auction name No Option");
        CheckAuctionNoBids("Test Auction name No Option");
        CheckAuctionWon("Test Auction 3 name");
        PlaceBid("Test Auction name No Option");

        DeleteAuction("Test Auction name");
        DeleteInexistentAuction("Test Auction 2 name");
        DeleteAuctionNotOwned("Test Auction 3 name");
    }

    /**
     * Creazione asta senza opzione "Compralo Subito"
     **/
    public void CreateAuction(String _auction_name, Date _end_time, double _reserved_price, String _description) {
        Assert.assertTrue(peer0.createAuction(_auction_name, _end_time, _reserved_price, _description)); //true
        Assert.assertFalse(peer0.createAuction(_auction_name, _end_time, _reserved_price, _description)); //already exists = false
    }

    /**
     * Creazione asta con prezzo di partenza <=0
     **/
    public void CreateAuctionWrongPrice(String _auction_name, Date _end_time, double _reserved_price, String _description) {
        Assert.assertFalse(peer1.createAuction(_auction_name, _end_time, _reserved_price, _description)); //wrong price = false
    }

    /**
     * Creazione asta con opzione "Compralo Subito"
     **/
    public void CreateAuctionWithOption(String _auction_name, Date _end_time, double _reserved_price, String _description, double _buyNowPrice) {
        Assert.assertTrue(peer2.createAuctionWithBuyNowOption(_auction_name, _end_time, _reserved_price, _description, _buyNowPrice)); //true
    }

    /**
     * Creazione asta con opzione "Compralo Subito" ma con prezzo compralo subito inferiore a quello di partenza asta
     **/
    public void CreateAuctionWithOptionWrongPrice(String _auction_name, Date _end_time, double _reserved_price, String _description, double _buyNowPrice) {
        Assert.assertFalse(peer3.createAuctionWithBuyNowOption(_auction_name, _end_time, _reserved_price, _description, _buyNowPrice)); //true
    }

    /**
     * Un peer prova a comprare la propria asta con "Comapralo Subito"
     * Un peer compra un asta con l'opzione "Compralo Subito"
     * Un peer prova a comprare un asta con l'opzione "Comprarlo Subito" già scaduta
     **/
    public void AuctionBuyItNow(String _auction_name) {
        assertThat(peer2.buyItNow(_auction_name), containsString("You cannot buy your own stuff."));
        assertThat(peer0.buyItNow(_auction_name), containsString("Congratulation, you bought it!"));
        assertThat(peer1.buyItNow(_auction_name), containsString("This auction ended. Check its status."));
    }

    /**
     * Un peer prova a comprare un asta senza opzione "Compralo Subito"
     **/
    public void BuyAuctionWithNoOption(String _auction_name) {
        assertThat(peer2.buyItNow(_auction_name), containsString("This auction has no Buy It Now option"));
    }

    /**
     * Un peer controlla un asta.
     **/
    public void CheckAuctionNoBids(String _auction_name) {
        assertThat(peer1.checkAuction(_auction_name), containsString("Starting bid: 10."));
    }

    /**
     * Un peer controlla un asta terminata.
     **/
    public void CheckAuctionWon(String _auction_name) {
        assertThat(peer0.checkAuction(_auction_name), containsString("This auction ended. Winning price: 200.0."));
    }

    /**
     * Un peer prova ad offrire alla propria asta
     * Un peer offre 45
     * Un peer prova ad offrire una cifra minore del necessario
     **/
    public void PlaceBid(String _auction_name) {
        assertThat(peer0.placeAbid(_auction_name, 30), containsString("You cannot place bids at your own auction."));
        assertThat(peer2.placeAbid(_auction_name, 45), containsString("BID PLACED: 45"));
        assertThat(peer3.placeAbid(_auction_name, 25), containsString("Your bid must be higher than 45"));
    }


    /**
     * Cancellazione asta
     **/
    public void DeleteAuction(String _auction_name) {
        Assert.assertTrue(peer0.deleteAuction(_auction_name)); //true
    }

    /**
     * Cancellazione asta che non esiste
     **/
    public void DeleteInexistentAuction(String _auction_name) {
        Assert.assertFalse(peer3.deleteAuction(_auction_name)); //false the auction doesn't exist
    }

    /**
     * Cancellazione asta non del creatore
     **/
    public void DeleteAuctionNotOwned(String _auction_name) {
        Assert.assertFalse(peer1.deleteAuction(_auction_name)); //false the owner is peer 2 not peer 1

    }


    private Date getDate(int day, int month, int year, int hours, int minutes) {
        // creating a Calendar object
        Calendar c1 = Calendar.getInstance();
        // set Month
        // MONTH starts with 0 i.e. ( 0 - Jan)
        c1.set(Calendar.MONTH, day);
        // set Date
        c1.set(Calendar.DATE, month);
        // set Year
        c1.set(Calendar.YEAR, year);
        // set hours
        c1.set(Calendar.HOUR_OF_DAY, hours);
        // set minutes
        c1.set(Calendar.MINUTE, minutes);
        // creating a date object with specified time.
        return c1.getTime();
    }
}
