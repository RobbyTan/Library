/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.SUtility;

/**
 *
 * @author UPHM
 */
public class PnlLogin extends javax.swing.JPanel {

    /**
     * Creates new form PnlLogin
     */
    private Connection myConn = null;
    private PreparedStatement myStmt = null;
    private ResultSet myRs = null;

    private Student student;

    private Inject inject;

    public PnlLogin(Connection conn, Inject inject) {
        initComponents();
        this.inject = inject;
        myConn = conn;
    }

    private void verifyButtonPress() {
        if (txtPnlLogin.getText().trim().toLowerCase().equals("exit")) {
            System.exit(0);
        }
        if (txtPnlLogin.getText().length() > 11) {
            if (txtPnlLogin.getText().toLowerCase().substring(0, 11).equals("setlocation")) {
                try {
                    // Prepare statement
                    myStmt = myConn.prepareStatement("UPDATE location SET location=? WHERE id='location';");
                    myStmt.setString(1, txtPnlLogin.getText().substring(12));
                    // Execute SQL query
                    myStmt.executeUpdate();
                    System.out.println(txtPnlLogin.getText().substring(12));
                    myStmt.close();
                    txtPnlLogin.setText("");
                } catch (SQLException ex) {
                    Logger.getLogger(PnlLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }
        if (txtPnlLogin.getText().trim().toLowerCase().equals("viewlocation")) {
            try {
                // Prepare statement
                myStmt = myConn.prepareStatement("select * from location");
                // Execute SQL query
                myRs = myStmt.executeQuery();
                while (myRs.next()) {
                    SUtility.msg(this, myRs.getString("location"), "Current Location");
                }
                myRs.close();
                myStmt.close();
                txtPnlLogin.setText("");
            } catch (SQLException ex) {
                Logger.getLogger(PnlLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (txtPnlLogin.getText().trim().toLowerCase().equals("admin")) {
            txtPnlLogin.setText("");
            inject.getPnlAdminLogin().requestFocus();
            inject.getFrmMain().changeLayout(inject.getPnlAdminLogin());
        } else if (txtPnlLogin.getText().trim().isEmpty()) {
            SUtility.msg(this, "Fill in your ID");
        } else {
            boolean found;
            found = displayStudent(txtPnlLogin.getText());
            if (found) {
                txtPnlLogin.setText("");
                inject.getPnlUser().generateData(student);
                inject.getFrmMain().changeLayout(inject.getPnlUser());
            } else {
                SUtility.msg(this, "User not found");
            }
        }

    }

    private boolean displayStudent(String nim) {
        try {
            // Prepare statement
            myStmt = myConn.prepareStatement("select * from studentdata where Student_ID=?");
            myStmt.setString(1, nim);
            // Execute SQL query
            myRs = myStmt.executeQuery();
            // Process result set
            if (myRs.isBeforeFirst()) {
                while (myRs.next()) {
                    student = new Student(myRs.getString("Name"), myRs.getString("Student_ID"),
                            myRs.getString("Study_Program"), myRs.getString("Class"));
                }
                return true;
            }

            myRs.close();
            myStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Student getStudent() {
        return student;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnPnlLoginVerify = new javax.swing.JButton();
        txtPnlLogin = new org.jdesktop.xswingx.JXFormattedTextField();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        btnPnlLoginVerify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1.login.png"))); // NOI18N
        btnPnlLoginVerify.setBorder(null);
        btnPnlLoginVerify.setContentAreaFilled(false);
        btnPnlLoginVerify.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1hover.png"))); // NOI18N
        btnPnlLoginVerify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPnlLoginVerifyActionPerformed(evt);
            }
        });
        add(btnPnlLoginVerify);
        btnPnlLoginVerify.setBounds(480, 520, 630, 90);

        txtPnlLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtPnlLogin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPnlLogin.setFont(new java.awt.Font("Calibri", 0, 36)); // NOI18N
        txtPnlLogin.setOpaque(false);
        txtPnlLogin.setPrompt("Input Your ID");
        txtPnlLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPnlLoginActionPerformed(evt);
            }
        });
        add(txtPnlLogin);
        txtPnlLogin.setBounds(480, 380, 620, 70);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1600, 900);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPnlLoginVerifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPnlLoginVerifyActionPerformed
        verifyButtonPress();
    }//GEN-LAST:event_btnPnlLoginVerifyActionPerformed

    private void txtPnlLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPnlLoginActionPerformed
        verifyButtonPress();
    }//GEN-LAST:event_txtPnlLoginActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPnlLoginVerify;
    private javax.swing.JLabel jLabel1;
    private org.jdesktop.xswingx.JXFormattedTextField txtPnlLogin;
    // End of variables declaration//GEN-END:variables
}
