# Jboss-MSSql-Connetor / JBoss Fuse server.

Sample Project :
https://examples.javacodegeeks.com/core-java/sql/java-jdbc-mssql-connection-example/

Driver download link :
https://docs.microsoft.com/en-us/sql/connect/jdbc/connecting-to-sql-server-with-the-jdbc-driver?view=sql-server-2017


Include deoendancy into pom :

<dependency>
        <groupId>com.microsoft.sqlserver</groupId>
        <artifactId>mssql-jdbc</artifactId>
        <version>7.0.0.jre8</version>
</dependency>


Install driver into jboss fuse server :

install mvn:com.microsoft.sqlserver/mssql-jdbc/7.0.0.jre8
