/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import datechooser.beans.DateChooserCombo;
import inject.Inject;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import util.SUtility;

/**
 *
 * @author UPHM
 */
public class PnlAdmin extends javax.swing.JPanel {

    private Connection myConn = null;
    private PreparedStatement myStmt = null;
    private ResultSet myRs = null;

    double tFee = 0;

    private Inject inject;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public PnlAdmin(Connection conn, Inject inject) {
        initComponents();
        myConn = conn;
        this.inject = inject;
    }

    public void generateTotal() {
        tFee = 0;
        for (int row = 0; row < tblFulldata.getRowCount(); row++) {
            tFee += Double.valueOf(tblFulldata.getValueAt(row, 7).toString());
        }
        txtTotalFee.setText(String.valueOf(tFee));
    }

    private void backToLogin() {
        inject.getFrmMain().changeLayout(inject.getPnlLogin());
    }

    private String getDate(DateChooserCombo dtc) {
        Date date = dtc.getSelectedDate().getTime();
        return format.format(date.getTime());
    }

    private void generateData() {
        DefaultTableModel tableModel = (DefaultTableModel) tblFulldata.getModel();

        for (int i = tblFulldata.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        try {
            // Prepare statement
            myStmt = myConn.prepareStatement("select * from fulldata where date(date) between ? and ? order by date");
            myStmt.setString(1, getDate(dtcAdminFrom));
            myStmt.setString(2, getDate(dtcAdminTo));
            // Execute SQL query
            myRs = myStmt.executeQuery();
            // Process result set
            if (myRs.isBeforeFirst()) {
                while (myRs.next()) {
                    Object[] data = {myRs.getString("date"), myRs.getString("name"),
                        myRs.getString("study_program"), myRs.getString("class"), Double.valueOf(myRs.getString("photocopy")),
                        Double.valueOf(myRs.getString("scanning")), Double.valueOf(myRs.getString("printing")), Double.valueOf(myRs.getString("fee"))};
                    tableModel.addRow(data);
                }
            }

            myRs.close();
            myStmt.close();
            generateTotal();
        } catch (SQLException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void toPDF() {
        try {
            generateData();
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            InputStream path = this.getClass().getResourceAsStream("report2.jrxml");
            jasperReport = JasperCompileManager.compileReport(path);
            Map parameters = new HashMap();
            parameters.put("datemin", getDate(dtcAdminFrom));
            parameters.put("datemax", getDate(dtcAdminTo));
            parameters.put("totalFee", tFee);
            parameters.put("location","Laporan Keuangan Library Services "+getLocationFromDatabase() + " Campus");
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, myConn);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getLocationFromDatabase() {
        try {
            // Prepare statement
            myStmt = myConn.prepareStatement("select * from location");
            // Execute SQL query
            myRs = myStmt.executeQuery();
            while (myRs.next()) {
                return myRs.getString("location");
            }
            myRs.close();
            myStmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(PnlLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "null";

    }

    public void deleteFromDatabase() {
        if (tblFulldata.getSelectedRow() > -1) {
            try {
                int x = SUtility.msq(this, "Are you sure?");
                if (x == 0) {
                    myStmt = myConn.prepareStatement("delete from transaction where date=?");
                    myStmt.setString(1, tblFulldata.getValueAt(tblFulldata.getSelectedRow(), 0).toString());
                    // Execute SQL query
                    myStmt.executeUpdate();
                    generateData();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PnlAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        dtcAdminFrom = new datechooser.beans.DateChooserCombo();
        dtcAdminTo = new datechooser.beans.DateChooserCombo();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFulldata = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        btnGenerate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtTotalFee = new javax.swing.JTextField();

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        dtcAdminFrom.setCalendarPreferredSize(new java.awt.Dimension(400, 400));

        dtcAdminTo.setCalendarPreferredSize(new java.awt.Dimension(400, 400));

        jLabel1.setText("From");

        jLabel2.setText("To");

        tblFulldata.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Name", "Study Program", "Class", "Photocopy", "Scanning", "Printing", "Fee"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblFulldata);
        if (tblFulldata.getColumnModel().getColumnCount() > 0) {
            tblFulldata.getColumnModel().getColumn(0).setResizable(false);
            tblFulldata.getColumnModel().getColumn(1).setResizable(false);
            tblFulldata.getColumnModel().getColumn(2).setResizable(false);
            tblFulldata.getColumnModel().getColumn(3).setResizable(false);
            tblFulldata.getColumnModel().getColumn(4).setResizable(false);
            tblFulldata.getColumnModel().getColumn(5).setResizable(false);
            tblFulldata.getColumnModel().getColumn(6).setResizable(false);
            tblFulldata.getColumnModel().getColumn(7).setResizable(false);
        }

        jButton2.setText("Generate Report");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

        btnUpdate.setText("Delete");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jLabel3.setText("Total Fee :");

        txtTotalFee.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1443, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotalFee, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1)
                        .addGap(194, 194, 194)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dtcAdminFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1)
                            .addComponent(btnGenerate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(148, 148, 148)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(dtcAdminTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(508, 508, 508)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtcAdminTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(7, 7, 7)))
                        .addComponent(dtcAdminFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerate)
                    .addComponent(btnUpdate))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jLabel3)
                    .addComponent(txtTotalFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        backToLogin();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        generateData();
    }//GEN-LAST:event_btnGenerateActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        toPDF();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        deleteFromDatabase();
    }//GEN-LAST:event_btnUpdateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerate;
    private javax.swing.JButton btnUpdate;
    private datechooser.beans.DateChooserCombo dtcAdminFrom;
    private datechooser.beans.DateChooserCombo dtcAdminTo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFulldata;
    private javax.swing.JTextField txtTotalFee;
    // End of variables declaration//GEN-END:variables
}
