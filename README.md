# Titan SonarQube plugin
Thanks for your interest in this project.

## Project description
This plugin supports the TTCN-3 language for Sonarqube.
The plugin uses the **Titan** compiler and its bundled **Titanium** static analyzer tool.

[Read more about Titan and Titanium](https://projects.eclipse.org/projects/tools.titan)


## Building and installing the plugin

 - clone the repository

```
 https://github.com/Magyarimiki/sonarqube-ttcn3-plugin.git
```

 - Build and install Titan compiler

Make sure to clone the repository into the root directory of the plugin source.

```
 cd sonarqube-ttcn3-plugin
 git clone https://gitlab.eclipse.org/eclipse/titan/titan.language-server.git
 cd titan.language-server/lsp
 mvn clean install
```

 - Compile the plugin

```
 cd ../..
 mvn clean package
```

 - Copy the plugin to the Sonarqube plugin directory

```
cp target/titan-sonar-plugin-x.y.x.jar ${path_to_sonarqube}/extensions/plugins/
```

 - restart Sonarqube to load the plugin.

## Testing the plugin

To test the plugin functionality, see the *readme* file under the **test/** directory.

## Using the plugin
*Note*: for very large projects you may need to adjust the java heap and stack size. Set the environment variable **SONAR_SCANNER_OPTS**:

```
-Xmx4G -Xss8m
```

This setting is needed for the **titan** compiler and static analyzer, not for the plugin itself, so you should use it for both of the options below.

A quick guide to use the plugin with **sonar-scanner-cli**:

### Using the bundled compiler and analyzer
*Note: this method should not be used for very large projects, where the compilation and analysis takes more that approx 2 minutes.*

The easiest way is to use the bundled analyzer.
Place a **sonar-project.properties** file to your project root:

```
sonar.host.url=http://your_sonarqube_host:9000
sonar.projectKey=project_key
sonar.token=your_analysis_token
```

The following optional settings can be used:

```
# enable/disable OOP extension
sonar.ttcn3.enableOOP=true

# enable/disable the Realtime extension
sonar.ttcn3.enableRealtime=true
```

Run **sonar-scanner** for the analysis.

### Using the analyzer externally
If your project is very large, the complier and analyzer must be executed separately:
Locate **org.eclipse.titan.lsp.jar** in the *target/* subdirectory of the Titan language server source folder. Compile and analyze your project:

```
java -jar /path/to/org.eclipse.titan.lsp.jar --cmd --root /your/project/dir --saFormat "%f:::%l:::%m:::%k" --outfile /your/project/dir/.titan_compile
```
Place a **sonar-project.properties** file to your project root:

```
sonar.host.url=http://your_sonarqube_host:9000
sonar.projectKey=project_key
sonar.token=your_analysis_token
```

Run **sonar-scanner**.
