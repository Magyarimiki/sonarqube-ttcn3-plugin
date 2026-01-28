# Titan SonarQube plugin

Thanks for your interest in this project.

## Project description
This plugin supports the TTCN-3 language for Sonarqube.
The plugin uses the **Titanium** static analyzer tool.

[Read more about Titan and Titanium](https://projects.eclipse.org/projects/tools.titan)

*The project is in experimental phase.*

## Quick setup

### Building Titan compiler

First, build Titan language server as a dependency and install it to the local maven repository.

```
 git clone https://gitlab.eclipse.org/eclipse/titan/titan.language-server.git
 cd titan.language-server/lsp
 mvn clean install
```

### Building and installing the plugin

 - clone the repository

```
 https://github.com/Magyarimiki/sonarqube-ttcn3-plugin.git
```

 - Compile the plugin

```
 cd sonarqube-ttcn3-plugin
 mvn clean package
```

 - copy the plugin to the Sonarqube plugin directory

```
cp target/titan-sonar-plugin-x.y.x.jar ${path_to_sonarqube}/extensions/plugins/
```

 - restart Sonarqube to load the plugin.

## Using the plugin

A quick guide to use the plugin with **sonar-scanner-cli**:

### Using the bundled compiler and analyzer
*Note: this method cannot be used for very large projects, where the compilation and analysis takes more that approx 2 minutes.*

The easiest way is to use the bundled analyzer.
Place a **sonar-project.properties** file to your project root:

```
sonar.host.url=http://your_sonarqube_host:9000
sonar.projectKey=project_key
sonar.token=your_analysis_token
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
