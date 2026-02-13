## Testing the plugin
This folder contains some very simple TTCN3 code to test the plugin.

*Please note that this code is completely meaningless and is only used for testing purposes.*

### Setting up the test

 - install the plugin and start **Sonarqube**. See the readme file in the project root for install instructions
 - create a project and make sure TTCN3 is added as a language in the *quality profile*
 - if you don't have an analysis or user token, create one
 - create a **sonar-project.properties** file by copying the sample and set the config values
 - run **sonar-scanner**

After executing the analysis, you should see a couple of issues for your project on the Sonarqube UI.