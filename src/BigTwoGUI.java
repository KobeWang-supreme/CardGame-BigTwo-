import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.text.DefaultCaret;


/**
 * This is the design of the GUI of the bigTwo game
 * It implements the CardGameUI interface
 * @author Wang Kesheng
 * @since 2021/11/24
 */
public class BigTwoGUI implements CardGameUI{
    private BigTwo game; // a card game with this GUI
    private boolean[] selected; // contains the selected cards
    private int activePlayer; // id of the active player
    private JFrame frame; //the window of the GUI
    private JPanel bigTwoPanel; // the panel shows the player's cards and cards on the table
    private JButton playButton; // a button to makeMove with the selected cards
    private JButton passButton; // a button to pass
    private JTextArea msgArea; // a text area to show the current game status
    private JTextArea chatArea; // a text area to show players' chat message
    private JTextField chatInput; // a text field for players to input and chat
    private Image[] avatars; // an array to store the images of avatars
    private Image[][] cardImages; // an array to store the images of cards
    private Image cardBack; // card's back image
    private String avatar_path = "Resource/Avatar/p"; // the path of the avatars
    private String cards_path = "Resource/Cards/"; // the path of the cards
    private char[] suit = {'d','c','h','s'}; // an array to store suit classes
    private char[] card_num = {'a','2','3','4','5','6','7','8','9','t','j','q','k'}; // an array to store the rank
    private boolean[] exist = {false,false,false,false}; // an array to store existence of them
    /**
     * This constructor takes the game as the parameter.
     * @param game
     */
    public BigTwoGUI(BigTwo game){
        //1.prepare
        this.game = game;
        selected = new boolean[13]; resetSelected();
        setActivePlayer(game.getCurrentPlayerIdx());
        //2.load all images
        avatars = new Image[4];
        for (int i =0;i < 4; i++){
            avatars[i] = new ImageIcon(avatar_path+(i+1)+".jpg").getImage();
        }
        cardBack = new ImageIcon(cards_path+"b.gif").getImage();
        cardImages = new Image[4][13];
        for (int i=0;i < 4;i++){
            for (int j=0;j < 13 ;j++){
                cardImages[i][j] = new ImageIcon(cards_path+card_num[j]+suit[i]+".gif").getImage();
            }
        }
        //3.create frame
        frame = new JFrame("BigTwo");
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //4.create menuBar and add items
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("Game");
        JMenu menu2 = new JMenu("Message");
        JMenuItem item1 = new JMenuItem("Quit");
        //add listener
        item1.addActionListener(new QuitMenuListener());
        JMenuItem item2 = new JMenuItem("Restart");
        //add listener
        item2.addActionListener(new RestartMenuItemListener());
        JMenuItem connect = new JMenuItem("Connect");
        connect.addActionListener(new ConnectMenuItemListener());
        menu1.add(item1);
        menu1.add(item2);
        menu1.add(connect);
        menuBar.add(menu1);
        menuBar.add(menu2);
        frame.setJMenuBar(menuBar);
        //5.create text area
        JPanel msg = new JPanel();
        msg.setBounds(600,0,400,640);
        msgArea = new JTextArea(100,30);
        chatArea = new JTextArea(100,30);
        msg.setLayout(new BoxLayout(msg, BoxLayout.Y_AXIS));
        chatInput = new JTextField(15);
        chatInput.addActionListener(new ActionListener() {
            /**
             * handle the action on the chatInput text field
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                String msg1 = chatInput.getText();
                if (msg1 != null){
                    //chatArea.append("Player "+(activePlayer)+":"+msg1);
                    game.getClient().sendMessage(new CardGameMessage(CardGameMessage.MSG,-1,msg1));
                    chatInput.setText("");
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                }
                chatInput.setText("");
            }
        });
        msgArea.setEditable(false);
        msgArea.setVisible(true);
        JScrollPane scrollPane1 = new JScrollPane(msgArea);
        msgArea.setLineWrap(true);
        scrollPane1.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatArea.setEditable(false);
        chatArea.setVisible(true);
        JScrollPane scrollPane2 = new JScrollPane(chatArea);
        chatArea.setLineWrap(true);
        scrollPane2.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        msgArea.setCaretPosition(msgArea.getDocument().getLength());
        DefaultCaret caret1 = (DefaultCaret)msgArea.getCaret();
        caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JPanel chatP = new JPanel();
        chatP.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel la = new JLabel("Message: ");
        chatP.add(la);
        chatP.add(chatInput);
        msg.add(scrollPane1);
        msg.add(scrollPane2);
        msg.add(chatP);
        //6.bigTwoPanel
        bigTwoPanel = new BigTwoPanel();
        bigTwoPanel.setBounds(0,0,600,600);
        JPanel btp = new JPanel();
        btp.setBounds(0,600,600,40);
        playButton = new JButton("PLAY");
        passButton = new JButton("PASS");
        //add listener
        playButton.addActionListener(new PlayButtonListener());
        passButton.addActionListener(new PassButtonListener());
        btp.setLayout(new FlowLayout());
        btp.setSize(600,40);
        btp.add(playButton);
        btp.add(passButton);
        frame.add(bigTwoPanel);
        frame.add(btp);
        frame.add(msg);
        frame.setSize(1000,700);
        frame.setVisible(true);
        this.disable();
    }

    /**
     * a method to set existence
     * @param playerID
     */
    public void setExist(int playerID){
        exist[playerID] = true;
    }

    /**
     * a method to set not existence
     * @param playerID
     */
    public void setNotExist(int playerID){
        exist[playerID] = false;
    }
    /**
     * a method used to get frame
     * @return frame
     */
    public JFrame getFrame(){
        return frame;
    }

    /**
     * a method used to get chat area
     * @return chat area
     */
    public JTextArea getChatArea(){
        return chatArea;
    }
    /**
     * An inner class
     * draw the cards of players and the hands on table.
     * with a background picture
     */
    class BigTwoPanel extends JPanel implements MouseListener{
        Image img3;

        /**
         * the constructor of this inner class
         */
        public BigTwoPanel(){
            this.addMouseListener(this);
            img3 = new ImageIcon("Resource/bk.jpeg").getImage();
        }

        /**
         * inherited from JPanel
         * It can draw the graph of the card game GUI.
         * @param g
         */
        public void paintComponent(Graphics g){
            g.drawImage(img3,0,0,this.getWidth(),this.getHeight(),this);

            //player1
            g.drawString(game.getPlayerList().get(0).getName(),10,10);
            g.drawImage(avatars[0],10,20,80,80,this);
            if (exist[0] == true) {
                if (game.getClient() != null && game.getClient().getPlayerID() == 0) {
                    for (int i = 0; i < game.getPlayerList().get(0).getNumOfCards(); i++) {
                        if (selected[i]) {
                            g.drawImage(cardImages[game.getPlayerList().get(0).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(0).getCardsInHand().getCard(i).getRank()], 150 + 20 * i, 20, 40, 60, this);
                        } else {
                            g.drawImage(cardImages[game.getPlayerList().get(0).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(0).getCardsInHand().getCard(i).getRank()], 150 + 20 * i, 40, 40, 60, this);
                        }
                    }
                } else {
                    for (int i = 0; i < game.getPlayerList().get(0).getCardsInHand().size(); i++) {
                        g.drawImage(cardBack, 150 + 20 * i, 40, 40, 60, this);
                    }
                }
            }


            g.drawLine(0,120,600,120);
            //player2
            g.drawString(game.getPlayerList().get(1).getName(),10,130);

            g.drawImage(avatars[1],10,140,80,80,this);
            if (exist[1] == true) {
                if (game.getClient() != null && game.getClient().getPlayerID() == 1) {
                    for (int i = 0; i < game.getPlayerList().get(1).getNumOfCards(); i++) {
                        if (selected[i]) {
                            g.drawImage(cardImages[game.getPlayerList().get(1).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(1).getCardsInHand().getCard(i).getRank()], 150 + 20 * i, 140, 40, 60, this);
                        } else {
                            g.drawImage(cardImages[game.getPlayerList().get(1).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(1).getCardsInHand().getCard(i).getRank()], 150 + 20 * i, 160, 40, 60, this);
                        }
                    }
                } else {
                    for (int i = 0; i < game.getPlayerList().get(1).getCardsInHand().size(); i++) {
                        g.drawImage(cardBack, 150 + 20 * i, 160, 40, 60, this);
                    }
                }
            }


            g.drawLine(0,240,600,240);
            //player3
            g.drawString(game.getPlayerList().get(2).getName(),10,250);

            g.drawImage(avatars[2],10,260,80,80,this);
            if (exist[2] == true) {
                if (game.getClient() != null && game.getClient().getPlayerID() == 2) {
                    for (int i = 0; i < game.getPlayerList().get(2).getNumOfCards(); i++) {
                        if (selected[i]) {
                            g.drawImage(cardImages[game.getPlayerList().get(2).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(2).getCardsInHand().getCard(i).getRank()], 150 + 20 * i, 260, 40, 60, this);
                        } else {
                            g.drawImage(cardImages[game.getPlayerList().get(2).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(2).getCardsInHand().getCard(i).getRank()], 150 + 20 * i, 280, 40, 60, this);
                        }
                    }
                } else {
                    for (int i = 0; i < game.getPlayerList().get(2).getCardsInHand().size(); i++) {
                        g.drawImage(cardBack, 150 + 20 * i, 280, 40, 60, this);
                    }
                }
            }


            g.drawLine(0,360,600,360);
            //player4
            g.drawString(game.getPlayerList().get(3).getName(),10,370);

            g.drawImage(avatars[3],10,380,80,80,this);
            if (exist[3] == true) {
                if (game.getClient() != null && game.getClient().getPlayerID() == 3) {
                    for (int i = 0; i < game.getPlayerList().get(3).getNumOfCards(); i++) {
                        if (selected[i]) {
                            g.drawImage(cardImages[game.getPlayerList().get(3).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(3).getCardsInHand().getCard(i).getRank()], 150 + 20 * i, 380, 40, 60, this);
                        } else {
                            g.drawImage(cardImages[game.getPlayerList().get(3).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(3).getCardsInHand().getCard(i).getRank()], 150 + 20 * i, 400, 40, 60, this);
                        }
                    }
                } else {
                    for (int i = 0; i < game.getPlayerList().get(3).getCardsInHand().size(); i++) {
                        g.drawImage(cardBack, 150 + 20 * i, 400, 40, 60, this);
                    }
                }
            }


            g.drawLine(0,480,600,480);
            //cards on table
            g.drawString("Current Hand On The Table",10,490);
            if (game.getHandsOnTable().size() != 0){
                Hand HandOnTable = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
                for(int i = 0; i < HandOnTable.size();i++){
                    g.drawImage(cardImages[HandOnTable.getCard(i).getSuit()][HandOnTable.getCard(i).getRank()],150+20*i,500,40,60,this);
                }
            }
        }

        /**
         * handle mouse release event listener
         * @param event
         */
        public void mouseReleased(MouseEvent event){
            int up_y = 40 + game.getLocalPlayerIndex()*120;
            int num_cards = game.getPlayerList().get(game.getLocalPlayerIndex()).getNumOfCards();
            int start = num_cards - 1;
            if (event.getX() <= 190 + 20*start && event.getX() >= 150 + 20*start){
                if (!selected[start] && event.getY() >= up_y && event.getY() <= up_y+60){
                    selected[start] = true;
                }
                else if (selected[start] && event.getY() >= up_y-20 && event.getY() <= up_y+40){
                    selected[start] = false;
                }
            }
            for (int i = 0; i < num_cards-1;i++){
                if (event.getX() <= 170 + 20*i && event.getX() >= 150 + 20*i){
                    if (!selected[i] && event.getY() >= up_y && event.getY() <= up_y+60){
                        selected[i] = true;
                    }
                    else if (selected[i] && event.getY() >= up_y-20 && event.getY() <= up_y+40){
                        selected[i] = false;
                    }
                }
            }
            frame.repaint();
        }
        public void mouseClicked(MouseEvent event){

        }
        @Override
        public void mouseEntered(MouseEvent arg0) {

        }
        @Override
        public void mouseExited(MouseEvent arg0) {

        }

        @Override
        public void mousePressed(MouseEvent arg0) {

        }

    }

    /**
     * a listener used to handle connect menu
     */
    class ConnectMenuItemListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getClient().getPlayerID() == -1) {
                game.getClient().connect();
            } else if (game.getClient().getPlayerID() >= 0 && game.getClient().getPlayerID() <= 3)
                printMsg("You are already connected to the server!\n");
        }
    }

    /**
     * handle the "play" button action - listener
     */
    class PlayButtonListener implements ActionListener{
        /**
         * handle the "play" button action
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(getSelected().length != 0)/*{
                game.makeMove(activePlayer,null);
            }
            else*/{
                game.makeMove(activePlayer,getSelected());
            }
            resetSelected();
            repaint();
        }
    }
    /**
     * handle the "pass" button action - listener
     */
    class PassButtonListener implements ActionListener{
        /**
         * handle the "pass" button action
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeMove(activePlayer,null);
            resetSelected();
            repaint();
        }
    }
    /**
     * handle the "Quit" menuItem action  - listener
     */
    class QuitMenuListener implements ActionListener{
        /**
         * handle the "Quit" menuItem action
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
     * handle the "Restart" menuItem action - listener
     */
    class RestartMenuItemListener implements ActionListener{
        /**
         * handle the "Restart" menuItem action
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            reset();

        }
    }

    /**
     * Sets the index of the active player
     *
     * @param activePlayer
     * int value representing the index of the active player
     */
    public void setActivePlayer(int activePlayer){
        this.activePlayer = activePlayer;
    }

    /**
     * repaint GUI
     */
    public void repaint(){
        frame.repaint();
    }

    /**
     * print the messages contains the game's status
     * @param msg the string to be printed to the message area of the card game user
     */
    public void printMsg(String msg){
        msgArea.append(msg);
    }

    /**
     * clear the MsgArea
     */
    public void clearMsgArea(){
        msgArea.setText("");
    }

    /**
     * clear the ChatArea
     */
    public void clearChatArea(){
        chatArea.setText("");
    }

    /**
     * resets the GUI
     */
    public void reset(){
        frame.setVisible(false);
        game = new BigTwo();
        clearChatArea();
        clearMsgArea();
        BigTwoDeck deck = new BigTwoDeck();
        deck.shuffle();
        game.start(deck);
        printMsg("The game has been restarted");
    }

    /**
     * enable user to interact
     */
    public void enable(){
        playButton.setEnabled(true);
        passButton.setEnabled(true);
        bigTwoPanel.setEnabled(true);
    }

    /**
     * disable user to interact
     */
    public void disable(){
        playButton.setEnabled(false);
        passButton.setEnabled(false);
        bigTwoPanel.setEnabled(false);
    }

    /**
     * prompt the Active player
     */
    public void promptActivePlayer() {
        resetSelected();
        printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn: ");
        frame.repaint();
        //int[] cardIdx = getSelected();
        //game.makeMove(activePlayer, cardIdx);
    }

    /**
     * get the cards selected
     * @return
     */
    public int[] getSelected(){
        int num = 0;
        for (int i = 0; i < 13; i++){
            if(selected[i]) num += 1;
        }
        int [] CardsID = new int[num];
        if (num != 0){
            CardsID = new int[num];
            num = 0;
            for(int i =0;i < 13;i++){
                if(selected[i]){
                    CardsID[num] = i;
                    num++;
                }
            }
        }
        return CardsID;
    }

    /**
     * reset the "selected" array
     */
    public void resetSelected(){
        for (int j = 0; j < selected.length; j++) {
            selected[j] = false;
        }
    }


}