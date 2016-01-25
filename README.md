# osgi-ninja-core
OSGI Dependency Tools

This library contains features to simplify the management of OSGI dependencies. 
Using XML configuration files it will download dependencies, wrap them, and 
install them into a project folder for use in deployment.

## Example

Java Code:
```
  OsgiUserBundleRespository repository = new OsgiUserBundleRespository("bundles");
  URL srcUrl = getClass().getClassLoader().getResource("osgideps.xml");
  repository.updateRepository(srcUrl, false);
```

Dependency File: osgideps.xml
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<deps xmlns="urn:com:vnetpublishing:osgi:dependencies">

    <!-- Load Group of Dependencies -->
    <link href="more_dependencies.xml"/>
    
    <!-- Load Bundle from maven -->
    <mavenBundle>
        <groupId>org.apache.servicemix.bundles</groupId>
        <artifactId>org.apache.servicemix.bundles.spring-core</artifactId>
        <version>4.2.4.RELEASE_1</version>
    </mavenBundle>
    
    <!-- Wrap a library from maven as a bundle -->
    <mavenBundle>
      <groupId>com.vnetpublishing.java</groupId>
      <artifactId>super-user-application</artifactId>
      <version>0.0.5</version>
      <wrap>
          <instr>
              <name>Bundle-SymbolicName</name>
              <value>super-user-application</value>
          </instr>
          <instr>
              <name>Import-Package</name>
              <valueList>
              	<value>java.util.logging</value>
              	<value>*;resolution:=optional</value>
              </valueList>
          </instr>
      </wrap>
    </mavenBundle>
    
    <!-- Load a group of dependencies from a maven library -->
    <mavenLibrary>
        <groupId>com.vnetpublishing.java</groupId>
        <artifactId>osgi-ninja-library-example</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <resources>/com/vnetpublishing/java/osgi/tools/example/example.xml</resources>
    </mavenLibrary>
    
    <!-- Wrap a local jar file as a bundle -->
    <resourceBundle>
        <artifactId>helloworld</artifactId>
        <version>1.0</version>
        <resource>file:src/test/ext/helloworld.jar</resource>
        <wrap>
            <instr>
                <name>Bundle-SymbolicName</name>
                <value>helloworld</value>
            </instr>
            <instr>
                <name>Import-Package</name>
                <valueList>
                	<value>java.util.logging</value>
                	<value>*;resolution:=optional</value>
                </valueList>
            </instr>
        </wrap>
    </resourceBundle>
</deps>
```
