/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseadddemo;

/**
 *
 * @author JiT
 */
public class Registration {
    private String id;
    private String courses;

    public Registration() {
    }

    public Registration(String id, String courses) {
        this.id = id;
        this.courses = courses;
    }

    public String getId() {
        return id;
    }

    public String getCourses() {
        return courses;
    }
    
    
}
