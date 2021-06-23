package application;

public class MediaItems { 
private String title;
private String format;
private String loanedTo;
private String dateLoaned;

public void setTitle(String name) {title = name;}
public void setFormat(String type) {format = type;}

public String getTitle() {return title;}//Gets the title
public String getFormat() {return format;}


public void setLoan(String loaned) {loanedTo = loaned;}//Keeps track of items loaned out
public String getLoan() {return loanedTo; }//Keeps track of returned items


public void setDate(String date) {dateLoaned = date;} //Keeps date an item was loaned
public String getDate() {return dateLoaned;} //Keeps date item was returned
}