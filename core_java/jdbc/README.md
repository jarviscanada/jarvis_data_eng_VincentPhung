# Introduction
This Stock quote app is a Java application that allows users to perform real-time stock trading. The user can sell stocks, buy stocks, look up specific stocks,
and have the stocks they viewed saved as a quote. The application was written in Java, it utilizes libraries for JDBC for connecting to the 
postgres database. Maven was for building of the application, and dependency management. Lastly the application was dockerized and hosted within a docker container publicly available
on docker hub. 

# Implementation
The application was written in Java 11. It utilizes the Alpha Vantage API to retrieve real-time stock market data in JSON format.
This application then parses the JSON object into a Java object which is used throughout the application. The application uses libraries such as the Jackson library for handling the JSON parsing, and OKHttp for handling outgoing and incoming HTTP requests.
The Maven framework was used for building the application, managing the dependencies, and other management. For deployment the application was dockerized and is hosted on docker hub. As for the design of the application
it consists of 3 layers not including the database layer, which includes the DAO layer, service layer, and controller layer.

## ER Diagram
![ER Diagram](images/ERDiagram.png)

## Design Patterns
The Stock quote app, uses the DAO (Data Access Object), repository design pattern, and the MVC (Model-View-Controller) design patterns.

To begin the DAO design pattern is used to abstract and encapsulate the data access logic of the stock quote application. 
The DAO class contains methods that perform CRUD (Create, Read, Update, and Delete) operations which allows for the application to 
interface with the database data without direct contact. This abstraction and encapsulation, makes the code modular and improves security. 

The Repository design pattern is used to provide a centralized abstraction layer that mediates between the application's business logic and data. 
It acts as an intermediary between the DAOs and the other layers. This pattern adds another layer of abstraction, as within this layer
there are a set of methods to call upon the CRUD methods listed in the DAO layer of the application.This pattern allows the application to call upon
different DAOs from a centralized layer with ease.

Lastly, the MVC software architectural pattern which divides an application into three interconnected components to separate the internal representations
of information from the ways that information is presented to the user. The model component consists of the business logic and data manipulation. The view component
is responsible for the user interface and user interaction. The controller component handles the interactions between the model and view,
it receives user input from the view, and processes it using methods from the model component then displays the result back in the view component. Using this
pattern makes the application more structured and modular. 


# Test
The Stock quote application utilizes both unit testing and integration testing to ensure reliability and correctness. Junit 4 and Mockito are used to perform
the testing. To begin testing first off a connection is made to the database through JDBC and then creating a new object. Then the existence of that object is 
verified by asserting the findByID method to check if the correct symbol is returned. Then that object is removed from the database to ensure the database is 
still in its original state. 

Then there is unit testing done on the methods, where sample inputs are tested with the expected results of the methods. For integration testing, the tests
are written to validate the application's functionality and correctness. The integration test follows a top-down approach that goes through the entire 
business logic of the application. Starting from fetching a quote from the API, parsing that data into a java object, saving into the database, searching for that 
newly inserted data, and then deleting it. 

Logging is used throughout the application through the use of the Log4j logging framework. Which allows for the logging of events, errors, debugging information during the runtime and testing of the application
without causing the application to halt. 

