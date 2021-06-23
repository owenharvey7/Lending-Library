package application;

import java.io.IOException; //imports
import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;



public class LendingLibraryGUI extends Application {
    LendingLibrary LendingLibrary = new LendingLibrary(); //Creating an Object to access total numbers of items
    MediaItems Media = new MediaItems(); // creating an array of object to access MediaItems class and allowing it to hold 100 items 
    private ListView<String> library = new ListView<String>(); //Creates Listview
    ObservableList<String> libraryList = FXCollections.<String>observableArrayList(); //Creates a list to use in the Listview




    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane display = new BorderPane(); //Main display
        GridPane buttons = new GridPane(); //location to display buttons
        TextField outPut = new TextField(); //Text field to show inventory
        Insets padding = new Insets(10); //creates Insets for padding
        buttons.setPadding(padding); //padding around grid pane
        buttons.setHgap(10); //Horizontal gap
        libraryList = LendingLibrary.load(); //Loads contents from a text file back into the listview
        library.setItems(libraryList); //inputs list of data into the listview


        for (int i =0; i !=4;i++) { //Loop to create Buttons
            String[] actionButtons = {"Add","Check Out","Check In","Delete"};//String to store Button names
            Button temp = new Button(actionButtons[i]); //creates a temp button
            temp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //sets the size of buttons
            buttons.add(temp, i, 0); //add buttons to grid pane
            GridPane.setHgrow(temp, Priority.ALWAYS);
            GridPane.setVgrow(temp, Priority.ALWAYS);

            if (temp.getText().equals("Add")) { //if add button is pushed
                temp.setOnAction((e) -> {
                    try {
                        add();
                    } catch (IOException e1) {

                        e1.printStackTrace();
                    }
                });
            }
            else if (temp.getText().equals("Delete")) { //if delete button is pushed
                library.getSelectionModel().selectedItemProperty().addListener(ov -> { //gets the selected item from the listview
                temp.setOnAction((e) -> {
                        if(libraryList.isEmpty()) {

                        }
                        else {
                        try {
                            deleteLibrary();
                        } catch (IOException e1) {

                            e1.printStackTrace();
                        }
                        }

                });
                });
            }
            else if (temp.getText().equals("Check Out")){ //if check out button is pushed

                library.getSelectionModel().selectedItemProperty().addListener(ov -> { //gets the selected item from the listview
                    String name = library.getSelectionModel().getSelectedItem(); //stores selected listview data into a string

                    temp.setOnAction((e) -> { { // when checkout button is pushed

                    		//error messages and message for if item is on loan or it can still be loaned out
                                if (name.contains(" is currently on loan to ")) {
                                    errorMessage("Currently on Loan");
                                }
                                else {

                                    inputGUI("Who did you loan this to?");
                                }



                        }
                    });
                });

            }
            else if (temp.getText().equals("Check In")) {
                library.getSelectionModel().selectedItemProperty().addListener(ov -> { //gets the selected item from the listview
                    String name = library.getSelectionModel().getSelectedItem(); //stores selected listview data into a string

                    temp.setOnAction((e) -> { { // when checkout button is pushed


                                if (!name.contains(" is currently on loaned to ")) { //Catch if the item is not on loan
                                    errorMessage("Not on Loan");
                                }
                                else {
                                    try {
                                        checkIn();
                                    } catch (IOException e1) {

                                        e1.printStackTrace();
                                    }
                                }



                        }
                    });
                });



            }



    }


    outPut.setEditable(false); //no editing
    outPut.setFont(Font.font("monospace", FontWeight.BOLD, 20)); //sets fonts
    outPut.setMinHeight(300);//sets minimum height
    display.setTop(library); //sets output in display on top
    display.setCenter(buttons); //sets buttons on center 


    Scene scene = new Scene(display); //creates new scene
    primaryStage.setTitle("Lending Library"); //sets title of GUI
    primaryStage.setScene(scene); //adds scene to GUI
    primaryStage.setMinHeight(500); //Minimum height
    primaryStage.setMinWidth(450);//Minimum Width
    primaryStage.show();//Displays GUI to user
}

public static void main(String[] args) {

    launch(args);

}


private void add() throws IOException { //adding items to the library
    inputGUI("Title:"); //input method

    try {
        LendingLibrary.Save(library); //updates save of media
    } catch (IOException e1) {

        e1.printStackTrace();
    }

}

private void inputGUI(String input) { //input method to put information into the list

    Stage secondaryStage = new Stage();
    BorderPane border = new BorderPane();
    VBox titlePane = new VBox(8);
    HBox buttonLayout = new HBox(8);
    Label lblTitle = new Label(input);
    Button save = new Button("Save");
    Button close = new Button("Close");
    Insets padding = new Insets(10);
    TextField txt = new TextField("");
    close.setOnAction((e) -> secondaryStage.close());; //close button closes secondary stage



    save.setOnAction(new EventHandler<ActionEvent>() { //Save button to save information
        @Override 
        public void handle(ActionEvent e) {

            if (txt.getText().trim().isEmpty()) { //if text field is empty do nothing
                errorMessage("Please Enter Data into the text area"); //tells user to enter data in text field
            }
            else {

                if (input.equals("Title:")) { //gets the title of media
                    Media.setTitle(txt.getText()); //sets title of media
                    secondaryStage.close(); 
                    inputGUI("Format:"); //calls inputGUI to get format of media
                }
                else if (input.equals("Format:")) { //gets the format of media
                    Media.setFormat(txt.getText()); //sets format of media
                    secondaryStage.close();
                    addToLibrary(); //adds media to library
                }
                else if (input.equals("Who did you loan this to?")) { //gets information on who borrowed the media
                    Media.setLoan(txt.getText()); //sets loaned out
                    secondaryStage.close();
                    inputGUI("When did you loan it(date)?"); //gets date when media was borrowed
                }
                else if (input.equals("When did you loan it(date)?")) { //gets date when media was borrowed
                    Media.setDate(txt.getText());//sets date when media was borrowed
                    secondaryStage.close();
                    checkOut(); //checks out media from library

                }
            }
            try {
                LendingLibrary.Save(library); //updates save of media
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        }

    });



    buttonLayout.getChildren().addAll(close,save);
    titlePane.setPadding(padding);
    titlePane.getChildren().addAll(lblTitle,txt,buttonLayout);
    border.setCenter(titlePane);
    BorderPane.setAlignment(titlePane, Pos.CENTER);

    Scene scene = new Scene(border); //creates new scene
    secondaryStage.setTitle("Input"); //sets title of GUI
    secondaryStage.setScene(scene); //adds scene to GUI
    secondaryStage.setMinHeight(200); //Minimum height
    secondaryStage.setMinWidth(350);//Minimum Width
    secondaryStage.setMaxHeight(200); //Minimum height
    secondaryStage.setMaxWidth(350);//Minimum Width

    secondaryStage.show();//Displays GUI to user


}


private void addToLibrary() { //adding media to library
    String total; //string to hold name and format of media

    total = Media.getTitle();
    total = total + " ("+ Media.getFormat() +")";
    libraryList.add(total); //adds media to list
    library.setItems(libraryList); //adds list to library



}

private void deleteLibrary() throws IOException { //deletes media from library

        int selectedItem = library.getSelectionModel().getSelectedIndex(); //gets selected item in library list
        libraryList.remove(selectedItem); //removes media from list
        LendingLibrary.Save(library); //updates save of media

        }






private void checkOut(){ //checkout method for media
    String name = library.getSelectionModel().getSelectedItem(); //grabs name of media selected
    int selectedItem = library.getSelectionModel().getSelectedIndex(); //grabs location of media in list
    libraryList.remove(selectedItem); //removes selected media
    libraryList.add(name + " | is currently on loaned to " + Media.getLoan() + " on " + Media.getDate()); //add media back to list 

}

private void checkIn() throws IOException { //check in method for media

    String temp = library.getSelectionModel().getSelectedItem().toString(); //grabs name of media selected
    String data = temp.substring(temp.indexOf("|") -1); //splits data up
    temp = temp.replace(data, ""); //removes checked out

    int selectedItem = library.getSelectionModel().getSelectedIndex(); //grabs location of media
    libraryList.remove(selectedItem); //removes media
    libraryList.add(temp); //adds data back in
    try {
        LendingLibrary.Save(library); //updates save of media
    } catch (IOException e1) {

        e1.printStackTrace();
    }


}
private void errorMessage(String message) { //displays error messages
    Stage errorStage = new Stage();
    BorderPane border = new BorderPane();
    VBox titlePane = new VBox(8); //frame for title
    HBox buttonLayout = new HBox(8); //frame for button
    Label lblTitle = new Label(message); //displays reason of error
    Button close = new Button("Close"); // adds close button
    Insets padding = new Insets(10);
    close.setOnAction((e) -> errorStage.close());;
    buttonLayout.getChildren().addAll(close);
    buttonLayout.setAlignment(Pos.CENTER);//sets alignment for the button
    titlePane.setPadding(padding);
    titlePane.getChildren().addAll(lblTitle,buttonLayout);
    titlePane.setAlignment(Pos.CENTER);
    border.setCenter(titlePane);
    BorderPane.setAlignment(titlePane, Pos.CENTER);
    Scene scene = new Scene(border); //creates new gui for an error
    errorStage.setTitle("Error"); //sets title of GUI
    errorStage.setScene(scene); //adds scene to GUI
    errorStage.setMinHeight(200); //Minimum height
    errorStage.setMinWidth(350);//Minimum Width
    errorStage.setMaxHeight(200); //Minimum height
    errorStage.setMaxWidth(350);//Minimum Width
    errorStage.show();//Displays GUI to user

}
}