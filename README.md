# Auction system

A simple auction system implemented on a P2P network. Basic APIs are described in [Auction Mechanism APIs](https://github.com/spagnuolocarmine/distributedsystems/blob/master/challenges/AuctionMechanism.java).

### Basic operations

- Create an auction with a certain end date and starting price.
- Check the status of an auction.
- Place a bid on an auction that has not ended yet

### Extra operations

- Create an auction with a certain end date, starting price and a "Buy it Now" option.
- Buy an auction through the "Buy it now" option.
- Delete an ongoing auction with no bids.

Note: The "check" now gives new information due to the "Buy it Now" option.

## Project Structure

The package ```src/main/java/edu/ds``` provides four Java classes: 
- Auction,  a class with all the information related to an auction.
- AuctionMechanism, an interface that defines the auction mechanism.
- AuctionMechanismImpl, an implementation of the AuctionMechanism interface.	
- Terminal, a terminal that allow to interact with the auction system.

The package ```src/tes/java``` provides one JUnit class:

- AuctionMechanismTest, a Junit class with different test cases.

### Build app in a Docker container

Build your docker container:  
```docker build --no-cache -t auction_system```

#### Start the master peer

Start the master peer, in interactive mode (-i) and with two (-e) environment variables:  
```docker run -i --name MASTER-PEER -e MASTERIP="127.0.0.1" -e ID=0 auction_system```

,the MASTERIP envirnoment variable is the master peer ip address and the ID environment variable is the unique id of your peer. Rember you have to run the master peer using the ID=0.

#### Start a generic peer

When master is started check the ip address of your container:

- Check the docker <container ID>: ```docker ps```
- Check the IP address: ```docker inspect <container ID>```

Now you can start your peers varying the unique peer id:  
```docker run -i --name PEER-1 -e MASTERIP="172.17.0.2" -e ID=1 auction_system```

### Technologies

The project has been implemented with:

- Java 8
- Apache Maven
- Eclipse IDE
- Tom p2p
- JUnit4
- Docker

