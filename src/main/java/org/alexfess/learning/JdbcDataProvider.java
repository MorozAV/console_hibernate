package org.alexfess.learning;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class JdbcDataProvider {
    private static Logger logger = Logger.getLogger(JdbcDataProvider.class.getName());
    private static Connection cn;

    public static Connection getConnection() {
        try {
            if (cn == null || cn.isClosed()) {
                createConnection();
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return cn;
    }

    private static void createConnection() throws SQLException {
        Locale.setDefault(Locale.ENGLISH);
        OracleDataSource ods = new OracleDataSource();
        String url = "jdbc:oracle:thin:@//oracle-main.tmb.nf/hope.intra.net";
        ods.setURL(url);
        ods.setUser("TEST_HIBERNATE");
        ods.setPassword("test_hibernate");
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("oracle.jdbc.ReadTimeout", "" + 45000);
        ods.setConnectionProperties(connectionProperties);
        cn = ods.getConnection();
        cn.setAutoCommit(false);
    }

    public static String execute(String query) {
        String rez = null;
        try (Connection conn = getConnection();
             Statement st =conn.createStatement()){
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                rez = rs.getString(1);
            }
            conn.commit();
        } catch(SQLException ex){
            logger.log(Level.SEVERE, null, ex);
        }
        return rez;
    }
}
