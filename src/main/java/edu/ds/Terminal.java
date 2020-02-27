package edu.ds;


import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.Calendar;

public class Terminal {

    @Option(name = "-m", aliases = "--masterip", usage = "the master peer ip address", required = true)
    private static String master;

    @Option(name = "-id", aliases = "--identifierpeer", usage = "the unique identifier for this peer", required = true)
    private static int id;

    private static String name;
    private static String date;
    private static String description;
    private static double price;

    public static void main(String[] args) throws Exception {

        Terminal instance = new Terminal();
        final CmdLineParser parser = new CmdLineParser(instance);
        try {

            parser.parseArgument(args);

            TextIO textIO = TextIoFactory.getTextIO();
            TextTerminal terminal = textIO.getTextTerminal();
            AuctionMechanismImpl peer = new AuctionMechanismImpl(id, master);
            terminal.printf("\nStarting peer id: %d on master node: %s\n", id, master);
            while (true) {
                printMenu(terminal);
                int option = textIO.newIntInputReader()
                        .withMaxVal(6)
                        .withMinVal(1)
                        .read("Option");
                switch (option) {
                    case 1:
                        terminal.println("\nEnter auction name.");
                        name = textIO.newStringInputReader()
                                .read("Name: ");
                        terminal.println("\nEnter auction end date.");
                        date = textIO.newStringInputReader()
                                .read("Date (dd/MM/yyyy/HH/mm): ").trim();
                        String[] splitDate = date.split("/");
                        Calendar cal = Calendar.getInstance();
                        try {
                            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
                            cal.set(Calendar.MONTH, Integer.parseInt(splitDate[1])-1);
                            cal.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
                            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitDate[3]));
                            cal.set(Calendar.MINUTE, Integer.parseInt(splitDate[4]));
                        } catch (Exception e) {
                            terminal.println("\nData format error");
                            break;
                        }
                        terminal.println("\nEnter starting price.");
                        price = textIO.newDoubleInputReader().read("Price: ");
                        terminal.println("\nEnter description.");
                        description = textIO.newStringInputReader()
                                .withDefaultValue("no description")
                                .read("Description: ");
                        terminal.println("....");
                        if (peer.createAuction(name, cal.getTime(), price, description))
                            terminal.println("Auction has been created!!!!");
                        else
                            terminal.println("Errors have occured");
                        terminal.println("....");
                        break;
                    case 2:
                        terminal.println("\nEnter auction name.");
                        name = textIO.newStringInputReader()
                                .read("Name: ");
                        terminal.println("\nEnter auction end date.");
                        date = textIO.newStringInputReader()
                                .read("Date (dd/MM/yyyy/HH/mm): ").trim();
                        String[] splitDate2 = date.split("/");
                        Calendar cal2 = Calendar.getInstance();
                        try {
                            cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate2[0]));
                            cal2.set(Calendar.MONTH, Integer.parseInt(splitDate2[1])-1);
                            cal2.set(Calendar.YEAR, Integer.parseInt(splitDate2[2]));
                            cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitDate2[3]));
                            cal2.set(Calendar.MINUTE, Integer.parseInt(splitDate2[4]));
                        } catch (Exception e) {
                            terminal.println("\nData format error");
                            break;
                        }
                        terminal.println("\nEnter starting price.");
                        price = textIO.newDoubleInputReader().read("Price: ");
                        terminal.println("\nEnter Buy It Now price.");
                        double binprice = textIO.newDoubleInputReader().read("Buy it now price: ");
                        terminal.println("\nEnter description.");
                        description = textIO.newStringInputReader()
                                .withDefaultValue("no description")
                                .read("Description: ");
                        terminal.println("....");
                        if (peer.createAuctionWithBuyNowOption(name, cal2.getTime(), price, description, binprice))
                            terminal.println("Auction has been created!!!!");
                        else
                            terminal.println("Errors have occured");
                        terminal.println("....");
                        break;
                    case 3:
                        terminal.println("\nEnter auction name.");
                        name = textIO.newStringInputReader()
                                .read("Name: ");
                        terminal.println("....");
                        terminal.printf(peer.checkAuction(name));
                        terminal.println("....");
                        break;
                    case 4:
                        terminal.println("\nEnter auction name.");
                        name = textIO.newStringInputReader()
                                .read("Name: ");
                        terminal.println("\nEnter bid.");
                        price = textIO.newDoubleInputReader().read("Bid: ");
                        terminal.println("....");
                        terminal.println(peer.placeAbid(name, price));
                        terminal.println("....");
                        break;
                    case 5:
                        terminal.println("\nEnter auction name.");
                        name = textIO.newStringInputReader()
                                .read("Name: ");
                        terminal.println("....");
                        terminal.println(peer.buyItNow(name));
                        terminal.println("....");
                        break;
                    case 6:
                        terminal.println("\nEnter auction name.");
                        name = textIO.newStringInputReader()
                                .read("Name: ");
                        terminal.println("....");
                        if (peer.deleteAuction(name))
                            terminal.println("Auction has been deleted!!!!");
                        else
                            terminal.println("Errors have occured");
                        terminal.println("....");
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMenu(TextTerminal terminal) {
        terminal.println("1 - Create auction");
        terminal.println("2 - Create auction with Buy It Now option");
        terminal.println("3 - Check auction");
        terminal.println("4 - Place a bid");
        terminal.println("5 - Buy it now");
        terminal.println("6 - Delete auction");

    }

}
