import edu.ds.AuctionMechanismImpl;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Date;

public class AuctionMechanismTest extends TestCase {

    private AuctionMechanismImpl peer0, peer1, peer2, peer3;

    @Test
    public void TestList() throws Exception{
        peer0 = new AuctionMechanismImpl(0,"127.0.0.1");
        peer1 = new AuctionMechanismImpl(0,"127.0.0.1");
        peer2 = new AuctionMechanismImpl(0,"127.0.0.1");
        peer3 = new AuctionMechanismImpl(0,"127.0.0.1");

        String description = "Questa è un asta blablabla";

    }
    /**
    * Creazione asta senza opzione "Compralo Subito"
     **/
    public void CreaAsta(String _auction_name, Date _end_time, double _reserved_price, String _description){

    }
    /**
     * Creazione asta con prezzo di partenza <=0
     **/
    public void CreaAstaPrezzoSbagliato(String _auction_name, Date _end_time, double _reserved_price, String _description){

    }
    /**
     * Creazione asta con opzione "Compralo Subito"
     **/
    public void CreaAstaConOpzione(String _auction_name, Date _end_time, double _reserved_price, String _description, double _buyNowPrice){

    }
    /**
     * Creazione asta con opzione "Compralo Subito" ma con prezzo compralo subito inferiore a quello di partenza asta
     **/
    public void CreaAstaConOpzionePrezzoSbagliato(String _auction_name, Date _end_time, double _reserved_price, String _description, double _buyNowPrice){

    }
    /**
     * Cancellazione asta
     **/
    public void CancellaAsta(String _auction_name){

    }
    /**
     * Cancellazione asta che non esiste
     **/
    public void CancellaAstaNonEsiste(String _auction_name){

    }
    /**
     * Cancellazione asta non del creatore
     **/
    public void CancellaAstaOwnerSbagliato(String _auction_name){

    }
    /**
     * Controlla asta
     **/

}
