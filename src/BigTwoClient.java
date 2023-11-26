import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
/**
 * The BigTwoClient class implements the NetworkGame interface.
 *  It is used to model a Big Two card game client that supports 4 players playing over the internet.
 * @author Wang Kesheng
 *
 */
public class BigTwoClient implements NetworkGame{
    /**
     *  A constructor for creating a Big Two client.
     */
    BigTwoClient(BigTwo game, BigTwoGUI gui){
        this.game = game;
        this.gui = gui;
        this.playerName = JOptionPane.showInputDialog("Enter your name");
        connect();
    }
    private BigTwo game; // big two game
    private BigTwoGUI gui; // GUI of this game
    private Socket sock; // a socket connection to the game server
    private ObjectOutputStream oos; // the ObjectOutputStream for sending message to the server
    private ObjectInputStream ois; // the ObjectInputStream for receiving message from the server
    private int playerID; // the index of the player
    private String playerName; // name of the player
    private String serverIP; // IP address for the game server
    private int serverPort; // TCP port for the game server
    /**
     * a method for Getting the playerID(the index of the local player).
     */
    public int getPlayerID(){
        return playerID;
    }
    /**
     * a method for Setting the playerID(the index of the local player).
     */
    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }
    /**
     * a method for Getting the player Name.
     */
    public String getPlayerName(){
        return playerName;
    }
    /**
     * a method for Setting the playerID.
     */
    public void setPlayerName(String playerName){
        game.getPlayerList().get(playerID).setName(playerName);
    }
    /**
     * A method for getting the IP address of the game server.
     */
    public String getServerIP(){
        return serverIP;
    }
    /**
     * A method for setting the IP address of the game server.
     */
    public void setServerIP(String serverIP){
        this.serverIP = serverIP;
    }
    /**
     * A method for getting the TCP port of the game server.
     */

    public int getServerPort(){
        return serverPort;
    }
    /**
     * A method for setting the TCP port of the game server.
     */

    public void setServerPort(int serverPort){
        this.serverPort = serverPort;
    }
    /**
     * A method for making connection with the game server.
     */

    public void connect(){
        serverIP = "127.0.0.1";
        serverPort = 2396;
        try{
            sock = new Socket(this.serverIP,this.serverPort);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
        Runnable rr = new ServerHandler();
        Thread tt = new Thread(rr);
        tt.start();
        sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
        sendMessage(new CardGameMessage(4, -1, null));
        gui.repaint();
    }
    /**
     * A method for parsing message from game server.
     * Be called from the thread which is responsible for receiving message from the server.
     */

    public void parseMessage(GameMessage message){
        switch (message.getType()){
            case CardGameMessage.PLAYER_LIST:
                this.playerID = message.getPlayerID();
                game.setLocalPlayerIndex(message.getPlayerID());
                for(int i = 0; i < 4;i++){
                    if(((String[])message.getData())[i] != null){
                        game.getPlayerList().get(i).setName(((String[])message.getData())[i]);
                        gui.setExist(i);
                    }
                }
                gui.repaint();
                break;
            case CardGameMessage.JOIN:
                game.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
                gui.setExist(message.getPlayerID());
                gui.repaint();
                gui.printMsg("Player " + game.getPlayerList().get(message.getPlayerID()).getName() + " has joined the game!!!\n");
                break;
            case CardGameMessage.FULL:
                playerID = -1;
                gui.printMsg("This game is full of players now. Please try again!!\n");
                gui.repaint();
                break;
            case CardGameMessage.QUIT:
                gui.printMsg("Player " + message.getPlayerID() + " " + game.getPlayerList().get(message.getPlayerID()).getName() + " has left the game.\n");
                game.getPlayerList().get(message.getPlayerID()).setName("");
                gui.setNotExist(message.getPlayerID());
                if(!game.endOfGame()){
                    gui.disable();
                    CardGameMessage msg = new CardGameMessage(CardGameMessage.READY,-1,null);
                    sendMessage(msg);
                    for (int i = 0; i < 4; i++) {
                        game.getPlayerList().get(i).removeAllCards();
                    }
                    gui.repaint();
                }
                break;
            case CardGameMessage.READY:
                gui.printMsg("Player " + message.getPlayerID() + " is ready!!\n");
                game.getHandsOnTable().clear();//
                gui.repaint();
                break;
            case CardGameMessage.START:
                game.start((Deck)message.getData());//
                gui.printMsg("Game has started!\n");
                gui.enable();
                gui.repaint();
                break;
            case CardGameMessage.MOVE:
                game.checkMove(message.getPlayerID(), (int[])message.getData());
                if (game.endOfGame()){
                    gui.repaint();
                    JOptionPane.showMessageDialog(gui.getFrame(),"Game is over");
                    sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
                    return;
                }
                if (game.getCurrentPlayerIdx() == this.playerID){
                    this.gui.enable();
                }
                else
                    gui.disable();
                gui.repaint();
                break;
            case CardGameMessage.MSG:
                gui.getChatArea().append((String)message.getData()+"\n");
                break;
            default:
                gui.printMsg("Wrong Type: " + message.getType());
                gui.repaint();
                break;
        }
    }
    /**
     * a method for sending the message to the game server.
     * This method should be called when there exists a communication with the game server or other clients.
     *
     *@param message is a variable of GameMessage.(for network)
     */
    public void sendMessage(GameMessage message){
        try{
            oos.writeObject(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * an inner class to handle the server.
     */
    class ServerHandler implements Runnable{
        @Override
        public void run() {
            CardGameMessage message = null;
            try{
                while((message = (CardGameMessage) ois.readObject()) != null){
                    parseMessage(message);
                    System.out.println("Receiving message!");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            gui.repaint();
        }
    }

}
