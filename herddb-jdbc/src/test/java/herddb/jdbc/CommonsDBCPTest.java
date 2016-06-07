/*
 Licensed to Diennea S.r.l. under one
 or more contributor license agreements. See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership. Diennea S.r.l. licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.

 */
package herddb.jdbc;

import org.junit.Test;

import herddb.server.Server;
import herddb.server.ServerConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author enrico.olivelli
 */
public class CommonsDBCPTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void test() throws Exception {
        try (Server server = new Server(new ServerConfiguration(folder.newFolder().toPath()))) {
            server.start();
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl("jdbc:herddb:server:localhost:7000?");
            dataSource.setDriverClassName(Driver.class.getName());
            try (Connection connection = dataSource.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT * FROM SYSTABLES")) {
                int count = 0;
                while (rs.next()) {
                    System.out.println("table: " + rs.getString(1));
                    count++;
                }
                assertTrue(count > 0);
            }
            dataSource.close();

        }
    }

    @After    
    public void destroy() throws Exception {
        for (Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers(); drivers.hasMoreElements();) {
            DriverManager.deregisterDriver(drivers.nextElement());
        }
    }
}
