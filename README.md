# Delete Tomcat Files Plugin

Delete temporary Tomcat files plugin for `NetBeans IDE 8.2+`.

## Feature

This plugin tries to clear all Tomcat temp files.

## Install

Go to `Tools > Plugins > Downloaded Intalled > Add Plugins...` and add the `deletetomcatfilesplugin.nbm` file.

##Usage

Go to `Tools > Clear Tomcat` and click.

It will try to perform the following tasks:
 - Delete all but ROOT.xml or manager.xml at: 
  <user-dir>/AppData/Roaming/NetBeans/X.X.X/apache-tomcat-X.X.X.X_base/conf/Catalina/localhost
 - Delete all at <user-dir>/AppData/Roaming/NetBeans/X.X.X/apache-tomcat-X.X.X.X_base/logs
 - Delete all at <user-dir>/AppData/Roaming/NetBeans/X.X.X/apache-tomcat-X.X.X.X_base/temp
 - Delete all at <user-dir>/AppData/Roaming/NetBeans/X.X.X/apache-tomcat-X.X.X.X_base/webapps
 - Delete all but /ROOT or /manager at: 
   <user-dir>/AppData/Roaming/NetBeans/X.X.X/apache-tomcat-X.X.X.X_base/work/Catalina/localhost


## Credits
[Mauricio Soares da Silva](mailto:maumss.git@gmail.com).

## License

[GNU General Public License (GPL) v3](http://www.gnu.org/licenses/)

Copyright &copy; 2015 Mauricio Soares da Silva

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

