/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author UPHM
 */
public class Student {
    private String name;
    private String nim;
    private String studiProgram;
    private String kelas;

    public Student(String name, String nim, String studiProgram, String kelas) {
        this.name = name;
        this.nim = nim;
        this.studiProgram = studiProgram;
        this.kelas = kelas;
    }

    public String getName() {
        return name;
    }

    public String getNim() {
        return nim;
    }

    public String getStudiProgram() {
        return studiProgram;
    }

    public String getKelas() {
        return kelas;
    }
    
    
}
