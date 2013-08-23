/*
 * This class encapsulates various static methods useful throughout any java
 * class.
 */
package org.emmanet.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author mrelac
 */
public class Utils {

    /**
     * Given a property name <code>property</code>, this method searches the
     * default properties file for the property name and, if found, returns the
     * property value. If the property is <em>not</em> found, null is returned.
     * @param property the name of the desired property
     * @return the property value, if found; null otherwise
     */
    public static String getEmmaProperty(String property) {
        return getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, property);
    }
    /**
     * Given a property name <code>property</code>, this method searches the
     * default properties file for the property name and, if found, returns the
     * property values as an array of <code>String</code>. If the property is
     * <em>not</em> found, null is returned. If the property contains a single
     * value (i.e. the property value does not contain the delimiter), that value
     * is returned in an <code>Array&lt;String&gt;</code> of exactly 1 element.
     * @param property the name of the desired property
     * @return the property value, if found; null otherwise
     */
    public static String[] getEmmaProperty(String property, String delimiter) {
        return getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, property, delimiter);
    }
    
    /**
     * Given a property filename and a property name, this method returns the
     * desired property value from <code>filename</code> as a <code>String</code>.
     * <code>propertiesFile</code> is relative to the classloader's working
     * directory and typically is not an absolute path (e.g. no leading slash).
     * 
     * @param propertiesFile the name of the properties file
     * @param propertyName name of desired property value
     * @return the property value, if found; null otherwise
     */
    public static String getProperty(String propertiesFile, String property) {
        String retVal = null;
        FileInputStream stream = null;
        Properties properties = new Properties();
        
        try {
            stream = new FileInputStream(propertiesFile);
            properties.load(stream);
            retVal = properties.getProperty(property);
        }
        catch (IOException e) {
            return retVal;
        }
        finally {
            if (stream != null) {
                try {
                    stream.close();
                }
                catch (IOException ioe) { }
            }
        }
        
        return retVal;
    }
    
    /**
     * Given a property filename, a property name, and a delimiter, this method
     * returns the desired property values as an array of <code>String</code>.
     * The delimiter separates multiple values for the same property on the same
     * line. If the delimiter is null or is an empty string, then this function
     * returns the rvalue in its entirety, in an array of exactly one element.
     * @param propertiesFile the name of the properties file
     * @param propertyName name of desired property value
     * @return the property value, if found; null otherwise
     * @param delimiter The delimiter used to separate input property values
     * @return An array of <code>String</code> containing the property values
     */
    public static String[] getProperty(String propertiesFile, String property, String delimiter) {
        String[] retVal = null;
        
        if ((delimiter == null) || (delimiter.trim().length() == 0)) {
            retVal = new String[1];
            retVal[0] = getProperty(propertiesFile, property);
            return retVal;
        }
        FileInputStream stream = null;
        Properties properties = new Properties();
        
        try {
            stream = new FileInputStream(propertiesFile);
            properties.load(stream);
            String rvalue = properties.getProperty(property);
            if (rvalue == null)
                return null;
            
            if (rvalue.contains(delimiter)) {
                retVal = rvalue.split(delimiter);
                for (int i = 0; i < retVal.length; i++)
                    retVal[i] = retVal[i].trim();
            } else {
                retVal = new String[1];
                retVal[0] = rvalue.trim();
            }
        }
        catch (IOException e) {
            return retVal;
        }
        finally {
            if (stream != null) {
                try {
                    stream.close();
                }
                catch (IOException ioe) {
                System.out.println(ioe.getLocalizedMessage());
                }
            }
        }
        
        return retVal;
    }
    
    /**
     * Given an array of <code>String</code>, this method returns an equivalent
     * array of <code>Address</code>.
     * @param values
     * @return 
     */
    public static Address[] toAddress(String[] values) {
        if ((values == null) || (values.length == 0))
            return null;
        Address[] addresses = new Address[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                addresses[i] = new InternetAddress(values[i]);
            }
            catch (AddressException ae) {
                throw new RuntimeException("AddressException: " + ae.getLocalizedMessage());
            }
        }
        
        return addresses;
    }
    
    /**
     * Given two non-primitive types, either of which may be null or may contain
     * no elements, this method returns a single, new array of the contents of
     * array1 followed by the contents of array2. Empty arrays are treated as
     * null. If either array is null or empty (and the other is not), an array
     * of the other non-null, non-empty array values is returned.
     * @param <T> The array type
     * @param array1 the first array
     * @param array2 the second array
     * @return a single, new array of the contents of array1 followed by the
     *         contents of array2
     */
    public static <T> T[] join(T[] array1, T[] array2) {
        if ((array1 == null) || (array1.length == 0))
            array1 = null;
        if ((array2 == null) || (array2.length == 0))
            array2 = null;
        if (array1 == null)
            return array2;
        if (array2 == null)
            return array1;
        
        Class c = array1[0].getClass();
        
        int length = array1.length + array2.length;
        T[] retVal = (T[])Array.newInstance(c, length);
        int curIndex = 0;
        for (int i = 0; i < array1.length; i++) {
            retVal[curIndex] = array1[i];
            curIndex++;
        }
        for (int i = 0; i < array2.length; i++) {
            retVal[curIndex] = array2[i];
            curIndex++;
        }

        return retVal;
    }

    /**
     * Given the emma properties file that contains the following properties:
     * <ul><li><code>DBVENDOR</code></li>
     * <li><code>DBHOST</code></li>
     * <li><code>DBPORT</code></li>
     * <li><code>DBNAME</code></li>
     * <li><code>username</code></li>
     * <li><code>password</code></li></ul>
     * this method uses those properties to attempt to connect to the database.
     * 
     * @param dbPropertiesFilename the properties file containing the db connection
     * properties
     * @return a <code>Connection</code> object, if successful; null otherwise
     */
    public static Connection connectDB() {
        return connectDB(Constants.DEFAULT_EMMA_PROPERTIES_FILE);
    }
    
    /**
     * Given a properties file that contains the following properties:
     * <ul><li>DBDRIVER</li>
     * <li><code>DBVENDOR</code></li>
     * <li><code>DBHOST</code></li>
     * <li><code>DBPORT</code></li>
     * <li><code>DBNAME</code></li>
     * <li><code>username</code></li>
     * <li><code>password</code></li></ul>
     * this method uses those properties to attempt to connect to the database.
     * 
     * @param dbPropertiesFilename the properties file containing the db connection
     * properties
     * @return a <code>Connection</code> object, if successful; null otherwise
     */
    public static Connection connectDB(String dbPropertiesFilename) {
        Connection connection = null;
        
        try {
            String url = buildEmmaUrl();
            String dbdriver = getProperty(dbPropertiesFilename, Constants.DBDRIVER);
            String dbusername = getProperty(dbPropertiesFilename, Constants.DBUSERNAME);
            String dbpassword = getProperty(dbPropertiesFilename, Constants.DBPASSWORD);
            
            Class.forName(dbdriver);
            connection = DriverManager.getConnection(url, dbusername, dbpassword);
        }
        catch (SQLException se) {
            System.out.println("SQLException: " + se.getLocalizedMessage());
            throw new RuntimeException("SQLException occurred. Reason: " + se.getLocalizedMessage());
        }
        catch (Exception e) {
            String errMsg = "Exception of type " + e.getClass().getCanonicalName() + ": " + e.getLocalizedMessage();
            System.out.println(errMsg);
            throw new RuntimeException(errMsg);
        }
        
        return connection;
    }
    
    /**
     * Given the emma properties file that contains the following properties:
     * <ul><li><code>DBVENDOR</code></li>
     * <li><code>DBHOST</code></li>
     * <li><code>DBPORT</code></li>
     * <li><code>DBNAME</code></li></ul>
     * this method returns a db connection url string standardized for EBI (i.e.
     * unicode, collation, character set specification).
     * 
     * @return a db connection url string standardized for EBI (i.e.
     *         unicode, collation, character set specification)
     */
    public static String buildEmmaUrl() {
        return buildEmmaUrl(Constants.DEFAULT_EMMA_PROPERTIES_FILE);
    }
    
    /**
     * Given a properties file that contains the following properties:
     * <ul><li><code>DBVENDOR</code></li>
     * <li><code>DBHOST</code></li>
     * <li><code>DBPORT</code></li>
     * <li><code>DBNAME</code></li></ul>
     * this method returns a db connection url string standardized for EBI (i.e.
     * unicode, collation, character set specification).
     * 
     * @param dbPropertiesFilename the properties file containing the db connection
     * properties
     * @return a db connection url string standardized for EBI (i.e.
     *         unicode, collation, character set specification)
     */
    public static String buildEmmaUrl(String dbPropertiesFilename) {
        String dbVendor = getProperty(dbPropertiesFilename, Constants.DBVENDOR);
        String dbHost = getProperty(dbPropertiesFilename, Constants.DBHOST);
        String dbPort = getProperty(dbPropertiesFilename, Constants.DBPORT);
        String dbName = getProperty(dbPropertiesFilename, Constants.DBNAME);
        
        return "jdbc:" + dbVendor + "://" + dbHost + ":" + dbPort + "/" + dbName
             + "?autoReconnect=true&amp;useUnicode=true&amp;connectionCollation=utf8_general_ci&amp;characterEncoding=utf8&amp;characterSetResults=utf8";
    }

    /**
     * Given a <code>ResultSet</code> and a column name, this method attempts to
     * fetch the value from the result set. If successful, the value is returned;
     * otherwise, an empty <code>String</code> is returned. This method guarantees
     * a non-null return value regardless of <em>columnName</em> to be suitable
     * for client display.
     * @param rs the result set to search
     * @param columnName the desired column name
     * @return the value, if successful; an empty <code>String</code> otherwise
     */
    public static String getDbValue(ResultSet rs, String columnName) {
        String retVal = "";
        
        try {
            Object o = rs.getObject(columnName);
            if (o != null)
                retVal = o.toString();
        }
        catch (SQLException e) {
            throw new RuntimeException("Unable to get result set value for column '" + columnName + "'");
        }
        
        return retVal;
    }
    
    /**
     * Given an <code>Object</code> that may be null or may be an Integer, this
     * method attempts to convert the value to an <code>Integer</code>. If successful,
     * the <code>Integer</code> value is returned; otherwise, <code>null</code> is returned.
     * @param o the object to try to convert
     * @return the converted value, if <em>o</em> is an <code>Integer</code>; null otherwise
     */
    public static Integer tryParseInt(Object o) {
        if (o == null)
            return null;
        
        Integer retVal = null;
        try {
            retVal = Integer.parseInt(o.toString());
        }
        catch (NumberFormatException nfe) {
            System.out.println("nfe: " + nfe.getLocalizedMessage());
        }
        catch (Exception e) {
            System.out.println("e: " + e.getLocalizedMessage());
        }
        
        return retVal;
    }
}
