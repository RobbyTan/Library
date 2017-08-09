/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.FrmMain;
import main.PnlAdmin;
import main.PnlAdminLogin;
import main.PnlLogin;
import main.PnlUser;
import util.DbConn;

/**
 *
 * @author UPHM
 */
public class Inject {

    private Connection myConn = null;
    private PnlLogin pnlLogin;
    private FrmMain frmMain;
    private PnlAdmin pnlAdmin;
    private PnlUser pnlUser;
    private PnlAdminLogin pnlAdminLogin;

    public Inject(FrmMain main) {
        connectToDatabase();
        frmMain = main;
        pnlLogin = new PnlLogin(myConn, this);
        pnlAdmin = new PnlAdmin(myConn, this);
        pnlUser = new PnlUser(myConn, this);
        pnlAdminLogin = new PnlAdminLogin(this);

    }

    private void connectToDatabase() {
        try {
            Class.forName(DbConn.JDBC_CLASS);
            myConn = DriverManager.getConnection(DbConn.JDBC_URL,
                    DbConn.JDBC_USERNAME,
                    DbConn.JDBC_PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PnlLogin getPnlLogin() {
        return pnlLogin;
    }

    public FrmMain getFrmMain() {
        return frmMain;
    }

    public PnlAdmin getPnlAdmin() {
        return pnlAdmin;
    }

    public PnlUser getPnlUser() {
        return pnlUser;
    }

    public PnlAdminLogin getPnlAdminLogin() {
        return pnlAdminLogin;
    }

}
