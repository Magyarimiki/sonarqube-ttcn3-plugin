# Titan SonarQube plugin

Thanks for your interest in this project.

## Project description
This plugin helps to present the Titanium TTCN-3 static analyser tool results on SonarQube server.
The project is in experimental phase.

[More about Titan and Titanium](https://projects.eclipse.org/projects/tools.titan)

## Quick setup
 - clone the repository

```

 https://gitlab.eclipse.org/eclipse/titan/titan-sonarqube-plugin.git
 

```

 - fetch and build Titan language server in the project root

```
 
 cd titan-sonarqube-plugin
 git clone https://gitlab.eclipse.org/eclipse/titan/titan.language-server.git
 cd titan.language-server/lsp
 mvn clean compile
 
```

 - Compile the plugin

```

 cd ../..
 mvn clean package


```

 - copy the plugin to the Sonarqube plugin directory
 
```

cp target/titan-sonar-plugin-x.y.x.jar ${path_to_sonarqube}/extensions/plugins/


```

 - restart Sonarqube to load the plugin.


 
