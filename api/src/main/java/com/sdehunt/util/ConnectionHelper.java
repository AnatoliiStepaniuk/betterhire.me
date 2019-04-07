package com.sdehunt.util;

import com.sdehunt.service.params.HardCachedParameterService;
import com.sdehunt.service.params.ParameterService;
import com.sdehunt.service.params.SsmParameterService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionHelper {

    private static final ParameterService params = new HardCachedParameterService(new SsmParameterService());

    //Configuration parameters for the generation of the IAM Database Authentication token
    private static final String RDS_HOST = "RDS_HOST";
    private static final String RDS_PORT = "RDS_PORT";
    private static final String RDS_USER = "RDS_USER";
    private static final String RDS_PASSWORD ="RDS_PASSWORD";
    private static final String JDBC_URL = "jdbc:mysql://" + params.get(RDS_HOST) + ":" + params.get(RDS_PORT);

    private static final String SSL_CERTIFICATE = "certs/rds-ca-2015-root.pem";

    private static final String KEY_STORE_TYPE = "JKS";
    private static final String KEY_STORE_PROVIDER = "SUN";
    private static final String KEY_STORE_FILE_PREFIX = "sys-connect-via-ssl-test-cacerts";
    private static final String KEY_STORE_FILE_SUFFIX = ".jks";
    private static final String DEFAULT_KEY_STORE_PASSWORD = "a2134iuhr86w^%"; // TODO what happens here? do I update key store files everytime?

    /**
     * This method returns a connection to the db instance authenticated using IAM Database Authentication
     *
     * @return
     * @throws Exception
     */
    public static Connection getDBConnection() {
        try {
            setSslProperties();
            Connection connection = DriverManager.getConnection(JDBC_URL, setMySqlConnectionProperties());
            return connection;
        } catch (Exception e) {
            System.out.println(e.getCause());
            for(StackTraceElement traceElement : e.getStackTrace()){
                System.out.println(traceElement);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sets the mysql connection properties which includes the IAM Database Authentication token
     * as the password. It also specifies that SSL verification is required.
     *
     * @return
     */
    private static Properties setMySqlConnectionProperties() {
        Properties mysqlConnectionProperties = new Properties();
        mysqlConnectionProperties.setProperty("verifyServerCertificate", "true");
        mysqlConnectionProperties.setProperty("useSSL", "true");
        mysqlConnectionProperties.setProperty("user", params.get(RDS_USER));
        mysqlConnectionProperties.setProperty("password", params.get(RDS_PASSWORD));
        return mysqlConnectionProperties;
    }

    /**
     * This method sets the SSL properties which specify the key store file, its type and password:
     *
     * @throws Exception
     */
    private static void setSslProperties() throws Exception {
        System.setProperty("javax.net.ssl.trustStore", createKeyStoreFile());
        System.setProperty("javax.net.ssl.trustStoreType", KEY_STORE_TYPE);
        System.setProperty("javax.net.ssl.trustStorePassword", DEFAULT_KEY_STORE_PASSWORD);
    }

    /**
     * This method returns the path of the Key Store File needed for the SSL verification during the IAM Database Authentication to
     * the db instance.
     *
     * @return
     * @throws Exception
     */
    private static String createKeyStoreFile() throws Exception {
        return createKeyStoreFile(createCertificate()).getPath();
    }

    /**
     * This method generates the SSL certificate
     *
     * @return
     * @throws Exception
     */
    private static X509Certificate createCertificate() throws Exception {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        URL url = new File(SSL_CERTIFICATE).toURI().toURL();
        try (InputStream certInputStream = url.openStream()) {
            X509Certificate res = (X509Certificate) certFactory.generateCertificate(certInputStream);
            return res;
        }
    }

    /**
     * This method creates the Key Store File
     *
     * @param rootX509Certificate - the SSL certificate to be stored in the KeyStore
     * @return
     * @throws Exception
     */
    private static File createKeyStoreFile(X509Certificate rootX509Certificate) throws Exception {
        File keyStoreFile = File.createTempFile(KEY_STORE_FILE_PREFIX, KEY_STORE_FILE_SUFFIX);
        try (FileOutputStream fos = new FileOutputStream(keyStoreFile.getPath())) {
            KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE, KEY_STORE_PROVIDER);
            ks.load(null);
            ks.setCertificateEntry("rootCaCertificate", rootX509Certificate);
            ks.store(fos, DEFAULT_KEY_STORE_PASSWORD.toCharArray());
        }
        return keyStoreFile;
    }

}

