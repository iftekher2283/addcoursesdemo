/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseadddemo;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 *
 * @author Atoms
 */
public class CourseAddDemoUIController implements Initializable {
    @FXML
    private Text messageText;
    @FXML
    private TextField studentNameField;
    @FXML
    private TextField studentBatchField;
    @FXML
    private TextField studentCGPAField;
    @FXML
    private ComboBox<String> studentIdBox;
    @FXML
    private ListView<Courses> allCoursesList;
    @FXML
    private ListView<String> selectedCoursesList;
    
    private ObservableList<Courses> allCourses;
    private ObservableList<String> selectedCourses;
    private ArrayList<String> stdIds;
    
    private String DB_URL = "jdbc:mysql://127.0.0.1/addcoursesdb";
    private String DB_USER = "root";
    private String DB_PASS = "";
    
    private Courses addCourse = new Courses("", "");
    private String removeCourse = "";
    private String message = "";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        messageText.setText("");
        allCourses = FXCollections.observableArrayList();
        selectedCourses = FXCollections.observableArrayList();
        stdIds = new ArrayList();
        
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Statement statement = connection.createStatement();
            
            String query = "select id from tbl_students";
            ResultSet ids = statement.executeQuery(query);
            
            while(ids.next()){
                String id = ids.getString("id");
                stdIds.add(id);
            }
            
            String query2 = "select * from tbl_all_courses";
            ResultSet courses = statement.executeQuery(query2);
            
            while(courses.next()){
                String courseCode = courses.getString("course_code");
                String courseName = courses.getString("course_name");
                
                Courses course = new Courses(courseCode, courseName);
                allCourses.add(course);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseAddDemoUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        studentIdBox.getItems().addAll(stdIds);
        allCoursesList.setItems(allCourses);
    }    

    @FXML
    private void handleAddCourseAction(ActionEvent event) {
        messageText.setText("");
        if(addCourse.getCourseCode().equals("")){
            message = "Select a course first";
            messageText.setText(message);
        }
        else{
            selectedCourses.add(addCourse.toString());
            selectedCoursesList.setItems(selectedCourses);
            addCourse = new Courses("", "");
        }
    }

    @FXML
    private void handleRemoveCourseAction(ActionEvent event) {
        messageText.setText("");
        if(!removeCourse.equals("")){
            selectedCourses.remove(removeCourse);
            removeCourse = "";
        }
        else{
            message = "Select a course first";
            messageText.setText(message);
        }
    }

    @FXML
    private void handleRegisterCoursesAction(ActionEvent event) {
        messageText.setText("");
        String id = studentIdBox.getSelectionModel().getSelectedItem();
        String name = studentNameField.getText();
        if(name.equals("")){
            message = "Please Select an ID first";
            messageText.setText(message);
        }
        else{
            int size = selectedCourses.size();
            if (size == 0){
                message = "You must add at least one course for registration";
                messageText.setText(message);
            }
            else{
                String courses = "";
                for(int i = 0; i < size; i++){
                    String tokens[] = selectedCourses.get(i).split(" ");
                    if(i == 0){
                        courses = courses + tokens[0];
                    }
                    else{
                        courses = courses + ", " + tokens[0];
                    }
                }

                Registration registration = new Registration(id, courses);

                try {
                    Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                    Statement statement = connection.createStatement();

                    String query = "INSERT INTO `tbl_registered` (`sl`, `id`, `courses`) VALUES (NULL, '" + registration.getId() + "', '" + registration.getCourses() + "');";
                    statement.executeUpdate(query);
                    message = "Registration Successful";
                    messageText.setText(message);
                } catch (SQLException ex) {
                    message = "Database couldn't be connected";
                    messageText.setText(message);
                }
            }
        }
    }

    @FXML
    private void handleIdAction(ActionEvent event) {
        messageText.setText("");
        selectedCourses.remove(0, selectedCourses.size());
        String id = studentIdBox.getSelectionModel().getSelectedItem();
        String name = null;
        int batch = 0;
        double cgpa = 0;
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Statement statement = connection.createStatement();
            
            String query = "select * from tbl_students;"; 
            ResultSet student = statement.executeQuery(query);
            
            while(student.next()){
                if(student.getString("id").equals(id)){
                    name = student.getString("name");
                    batch = student.getInt("batch");
                    cgpa = student.getDouble("cgpa");
                    break;
                }
            }
            Student selStudent = new Student(id, name, batch, cgpa);
            
            studentNameField.setText(selStudent.getName());
            studentBatchField.setText(batch + "");
            studentCGPAField.setText(cgpa + "");
        } catch (SQLException ex) {
            message = "Database couldn't be connected";
            messageText.setText(message);
        }
        
    }

    @FXML
    private void handleSelectAddCourseAction(MouseEvent event) {
        messageText.setText("");
        
        addCourse = allCoursesList.getSelectionModel().getSelectedItem();
        removeCourse = "";
    }

    @FXML
    private void handleSelectRemoveCourseAction(MouseEvent event) {
        messageText.setText("");
        removeCourse = selectedCoursesList.getSelectionModel().getSelectedItem();
        addCourse = new Courses("", "");
    }

    @FXML
    private void handleNewAction(ActionEvent event) {
        messageText.setText("");
        studentIdBox.getSelectionModel().select("");
        studentNameField.setText("");
        studentBatchField.setText("");
        studentCGPAField.setText("");
        selectedCourses.remove(0, selectedCourses.size());
    }
    
}
