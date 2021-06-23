package application;

import java.io.*;//Imports
import java.util.*;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;


public class LendingLibrary{


    public void Save(ListView<String> library) throws IOException { //saves the library as text file

        String joined = library.getItems().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        File file = new File("LendingLibrary.txt");//Creates the text file Calc
        file.createNewFile(); //if file doesn't exist it will create it (if it does nothing will happen)

        if(file.exists()) { //if the file exists

            PrintWriter output = new PrintWriter(file);
            output.println(joined);
            if(!file.exists()) { //if file doesn't exist
                System.out.println("Error creating file");

            }
            output.close();
        }



    }




    @SuppressWarnings("resource")
    public ObservableList<String> load() throws FileNotFoundException { //loads information into program


        ObservableList<String> libraryList = FXCollections.<String>observableArrayList(); //creates a list
        File file = new File ("LendingLibrary.txt"); //access the file

        if(file.exists()) { //if file does exist 
        Scanner input = new Scanner(file);

        input.useDelimiter(","); //Separates data by a comma 

        while (input.hasNext()) { //while the list has data

            String temp = input.next(); //stores data into a string
            temp = temp.trim(); //trims spaces from beginning of data
            if (temp.equals("")) {

            }
            else {
            libraryList.addAll(temp); //stores data into the list 
            }

        }


        }
        else if(!file.exists()) { //if does not file exist

        }
        return libraryList; //returns list to use in the list view

    }


    }