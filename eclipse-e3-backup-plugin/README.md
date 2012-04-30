##Eclipse Backup Plugin

Backup task in package explorer. The selection gets zipped and transferred to another location. These are supported:

 * A local directory
 
 * USB
 
 * Dropbox

 * Google Drive
 
Although this is primarily an attempt to learn more about Eclipse plug-in development, I sometimes manually backup some project or classfiles on Dropbox to use on another computer. I hardly ever use the export wizard.

###Requirements

 * Eclipse 3.x (Not tested on 4.x)
 
 * Eclipse Plug-in Development Environment
 
###Installation

I have provided two options in the install directory. I recommend using the Eclipse update manager approach.

 * Within Eclipse go to the help menu and choose Install new software. Afterwards you give in file:\[path\to\local\install\p2updatesite] and you'll be able to select the plugin and install it.
 
 * An alternative approach is by putting the provided jar into the "dropins" directory within your Eclipse installation folder. After restarting Eclipse it should also work.
 
###Credits
 
Some icons created by Mazenl77.