/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseadddemo;

/**
 *
 * @author JIT
 */
public class Student {
    private String id;
    private String name;
    private int batch;
    private double cgpa;

    public Student() {
    }

    public Student(String id, String name, int batch, double cgpa) {
        this.id = id;
        this.name = name;
        this.batch = batch;
        this.cgpa = cgpa;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBatch() {
        return batch;
    }

    public double getCgpa() {
        return cgpa;
    }
    
    
}
