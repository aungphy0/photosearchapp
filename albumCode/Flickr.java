/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albumCode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Scanner;

import java.awt.*;
import java.io.IOException;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Frame;
import java.awt.Component;
import java.lang.Object;
import javax.swing.JFrame;
import javax.swing.border.*;
import javax.swing.BoxLayout;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import com.google.gson.*;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.io.PrintWriter;
import java.io.*;
import java.io.FileNotFoundException;


public class Flickr extends JFrame implements ActionListener,MouseListener {

    JTextField searchTagField = new JTextField("");
    JTextField numResultsStr = new JTextField("10");
    JPanel onePanel;
    JScrollPane oneScrollPanel;
    JButton testButton = new JButton("Test");
    JButton loadButton = new JButton("Load");
    JButton deleteButton = new JButton("Delete");
    JButton saveButton = new JButton("Save");
    JButton exitButton = new JButton("Exit");
    JButton searchButton = new JButton("Search");

    static int frameWidth = 800;
    static int frameHeight = 600;
    //** protected datatype can access within the package class*/
    protected static String key;
    protected static ArrayList<String> list = new ArrayList<String>();
    protected static String savePhotos;
    protected static Border whiteBorder;
    protected static String delphotoUrl;

    public Flickr() {


	/** create bottom subpanel with buttons, flow layout*/
	JPanel buttonsPanel = new JPanel();
	buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
	/** add buttons to bottom subpanel */
	buttonsPanel.add(testButton);
	buttonsPanel.add(loadButton);
	buttonsPanel.add(deleteButton);
	buttonsPanel.add(saveButton);
	buttonsPanel.add(exitButton);

	/** add listener for all the buttons clicks */
	testButton.addActionListener(this);
	loadButton.addActionListener(this);
	deleteButton.addActionListener(this);
	saveButton.addActionListener(this);
	exitButton.addActionListener(this);

	/** create middle subpanel with 2 text fields and button, border layout */
	JPanel textFieldSubPanel = new JPanel(new FlowLayout());
	/** create and add label to subpanel */
	JLabel tl = new JLabel("Enter search tag:");
	textFieldSubPanel.add(tl);


	/** set width of left text field*/
	searchTagField.setColumns(23);
	/** add listener for typing in left text field*/
	searchTagField.addActionListener(this);
	/** add left text field to middle subpanel*/
	textFieldSubPanel.add(searchTagField);
	/** add search button to middle subpanel*/
	textFieldSubPanel.add(searchButton);
	/** add listener for searchButton clicks*/
	searchButton.addActionListener(this);

	/**create and add label to middle subpanel, add to middle subpanel*/
	JLabel tNum = new JLabel("max search results:");
	numResultsStr.setColumns(2);
	textFieldSubPanel.add(tNum);
	textFieldSubPanel.add(numResultsStr);

	/** create and add panel to contain bottom and middle subpanel*/
	JPanel textFieldPanel = new JPanel();
	textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));
	textFieldPanel.add(textFieldSubPanel);
	textFieldPanel.add(buttonsPanel);

	/** create top panel */
	onePanel = new JPanel();
	onePanel.setLayout(new BoxLayout(onePanel, BoxLayout.Y_AXIS));

	/** create scrollable panel for top panel */
	oneScrollPanel = new JScrollPane(onePanel,
				      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				      JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	oneScrollPanel.setPreferredSize(new Dimension(frameWidth, frameHeight-100));
	setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	/** add scrollable panel to main frame*/
	add(oneScrollPanel);
        /** create white border for the photos */
        whiteBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
	/** add panel with buttons and textfields to main frame */
	add(textFieldPanel);
    }//end constructor
    /** main method to run GUI and other methods */
    public static void main(String [] args) throws Exception {
	Flickr frame = new Flickr();
	frame.setTitle("Online Photos Album");
	frame.setSize(frameWidth, frameHeight);
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);

    }//main method

    /** method mouseClicked to implements from MouseListener abstract class */
    public void mouseClicked(MouseEvent m){}
    /** method mouseEntered to implements from MouseListener abstract class */
    public void mouseEntered(MouseEvent m){}
    /** method mouseReleased to implements from MouseListener abstract class */
    public void mouseReleased(MouseEvent m){}
    /** method mousePressed to implements from MouseListener abstract class */
    public void mousePressed(MouseEvent m){}
    /** method mouseExited to implements from MouseListener abstract class */
    public void mouseExited(MouseEvent m){}

    /** method actionPerformed implements from ActionListener abstract class */
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == searchButton) {
            try{
                System.out.println("Search " + searchTagField.getText());
                /** save search tag string to key */
                key = searchTagField.getText();
                /** method request the JSON */
                requestURL();

               } catch (Exception ex) {
                Logger.getLogger(Flickr.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	else if (e.getSource() == testButton) {
	    System.out.println("Test");
            String s = searchTagField.getText();
            Image theImg = getImageURL(s);
            /** make height=200pixels with aspect ration */
            ImageIcon img = new ImageIcon(theImg.getScaledInstance(-1, 200, Image.SCALE_SMOOTH));
            JLabel jl = new JLabel(img);
            /** make border line */
            Border white = BorderFactory.createMatteBorder(3/2, 2, 3/2, 2, Color.WHITE);
            jl.setBorder(white);
            onePanel.add(jl);
            onePanel.revalidate();
	    onePanel.repaint();
	    /** connect updated onePanel to oneScrollPanel */
	    oneScrollPanel.setViewportView(onePanel);
	}
	else if (e.getSource() == loadButton) {

            try {
                System.out.println("Load");    ///Users/aungphyo/Desktop/CSC413/GetPhotoUrl/GPCode/photoUrl.txt
                Scanner scan = new Scanner(new File("../photoUrl.txt"));
                  while(scan.hasNext()){
                      list.add(scan.next());
                  }
                     scan.close();
                for(int i=0; i<list.size(); i++){
                    savePhotos = list.get(i);
                    System.out.println(i + " " + list.get(i));
                    Image theImg = getImageURL(savePhotos);
                    /** create the imageicon object and also make the Height=200pixels ratio image */
                    ImageIcon img = new ImageIcon(theImg.getScaledInstance(-1, 300, Image.SCALE_SMOOTH));
                    JLabel jl = new JLabel(img);
                    jl.setBorder(whiteBorder);
                    onePanel.add(jl);
                    }//for loop
                onePanel.revalidate();
                onePanel.repaint();
                oneScrollPanel.setViewportView(onePanel);
              /** catch the exception if there is no such file */
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Flickr.class.getName()).log(Level.SEVERE, null, ex);
            }

	}
	else if (e.getSource() == deleteButton){
		/** to remove the delete photo from onePanel */
                onePanel.remove(list.indexOf(delphotoUrl));
                /** to remove that photo URL from the list */
                list.remove(list.indexOf(delphotoUrl));
                onePanel.revalidate();
                onePanel.repaint();
                System.out.println("Delete");

	}
	else if (e.getSource() == saveButton){
                PrintWriter pw = null;
            try {
                System.out.println("Save");
                pw = new PrintWriter("../photoUrl.txt");

                for(int i = 0; i<list.size();  i++){
                    String s = list.get(i);
                    pw.write(s + "\n");
                    System.out.println(i+" "+list.get(i));
                }//for loop
              /** catch the exception if there is no such file */
            } catch (IOException ex) {
                Logger.getLogger(Flickr.class.getName()).log(Level.SEVERE, null, ex);
            }
                pw.close();
	}
	else if (e.getSource() == exitButton){
		System.out.println("Exit");
                /** to terminate the program */
                System.exit(0);
	}

 }

       public void requestURL() throws Exception{

        String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
	/** number of results per page */
        String numPhotos = "&per_page=" + numResultsStr.getText();
        String request = api + numPhotos;
        request += "&format=json&nojsoncallback=1&extras=geo";
        request += "&api_key=" + "***Your Flicker API KEY***";

	/** check the string length at search tag field */
	if (key.length() != 0) {
	    request += "&tags="+key.replaceAll(" ", "%20");
	}

	System.out.println("Sending http GET request:");
	System.out.println(request);

	/** open http connection */
	URL obj = new URL(request);
  HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	/** send GET request */
  con.setRequestMethod("GET");

	// get response
  int responseCode = con.getResponseCode();

	System.out.println("Response Code : " + responseCode);

	      // read and construct response String
        BufferedReader in = new BufferedReader(new InputStreamReader
					       (con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

	System.out.println(response);

	Gson gson = new Gson();
	String s = response.toString();

  Response responseObject = gson.fromJson(s, Response.class);
	System.out.println("# photos = " + responseObject.photos.photo.length);
	//for loop to get all the photos
  for(int i=0; i<responseObject.photos.photo.length; i++){
    	  int farm = responseObject.photos.photo[i].farm;
    	  String server = responseObject.photos.photo[i].server;
    	  String id = responseObject.photos.photo[i].id;
    	  String secret = responseObject.photos.photo[i].secret;
    	  String photoUrl = "https://farm"+farm+".static.flickr.com/"
    	                     +server+"/"+id+"_"+secret+".jpg";
         list.add(photoUrl);
         Image theImg = getImageURL(photoUrl);
         ImageIcon img = new ImageIcon(theImg.getScaledInstance(-1, 300, Image.SCALE_SMOOTH));
         JLabel jl = new JLabel(img);
         jl.setBorder(whiteBorder);
         onePanel.add(jl);
         // annonymous class MouseListener to choose the photo to delete
         jl.addMouseListener(new MouseListener(){
              public void mouseClicked(MouseEvent e){
                    Border blueBorder = BorderFactory.createLineBorder(Color.CYAN, 2);
                    jl.setBorder(blueBorder);
                    System.out.println("select photo " +list.indexOf(photoUrl));
                    delphotoUrl = photoUrl;
              }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}

        });

    }// end for loop
        onePanel.revalidate();
        onePanel.repaint();
        oneScrollPanel.setViewportView(onePanel);

  }//end requestURL method

      Image getImageURL(String loc) {
          Image img = null;
          try {
              final URL url = new URL(loc);
  	          img = ImageIO.read(url);
          } catch (Exception e) {
              System.out.println("Error loading image...");
              return null;
          }
              return img;

      }//end getImageURL method

}//end class
