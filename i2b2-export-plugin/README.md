# I2b2 Export Plugin
[Atlanta Clinical and Translational Science Institute (ACTSI)](http://www.actsi.org), [Emory University](http://www.emory.edu), Atlanta, GA

## What does it do?
It is an i2b2 web client plugin for exporting patient data
from i2b2 to CSV files. Users can configure the format of the CSV file using the
plugin's interface, as well as save, load, and delete configurations. More
information on doing so is available in the plugin's Help tab. The plugin
communicates with i2b2's cells via the i2b2 Export Service, a RESTful
web service that routes requests from the plugin to the appropriate i2b2 cell
and provides authentication.

## Version 1.1 development series
* Update dependencies
* Port to https://github.com/eurekaclinical/i2b2-client
* Address some modifier bugs

## Version history
### Version 1.0
Supports specifying rows and columns, saving and loading output templates, and exporting data.

## Requirements
* [i2b2](http://www.i2b2.org) version 1.7
  
## Installation
These instructions assume your i2b2 installation is rooted at /var/www/html/webclient.
This location will be referred to as $I2B2_ROOT.

1) Locate your i2b2 installation's plugins directory: `$I2B2_ROOT/js-i2b2/cells/plugins`.

2) Create a new directory inside the plugins directory. This directory defines
a new plugin category and will be referred to as `$CUSTOM_PLUGIN_DIR`. The 
plugin will show up under a category with the name of this directory in the 
i2b2 plugin list.

3) In `cell_config_data.js`, find the 'config' map and the 'category' key. Change
the last value of the 'category' array to be `$CUSTOM_PLUGIN_DIR`.

4) Inside `$CUSTOM_PLUGIN_DIR`, create a new directory, `PatientDataExport`.

5) Copy the entire directory and its contents into the `PatientDataExport` directory.

6) In `PatientDataExport_ctrlr.js`, change the value of `i2b2.PatientDataExort.SERVICE_URL`
variable to the location of the i2b2 Export Service's REST methods that the
plugin needs to call.

7) Modify `$I2B2_ROOT/js-i2b2/i2b2_loader.js` to add the plugin to the list of
plugins. Add the following JavaScript map to the `i2b2.hive.tempCellsList` array:
```
 { code: "PatientDataExport",
    forceLoading: true,
    forceConfigMsg: { params: [] },
    forceDir: "cells/plugins/$CUSTOM_PLUGIN_DIR"
 }
```
where `$CUSTOM_PLUGIN_DIR` is the name of the directory you created in step 2
(where the plugin was copied to). Where you place this block determines the
order in which the plugin is displayed in the i2b2 plugin list.

For more information about i2b2 plugins, see:
https://community.i2b2.org/wiki/display/webclient/Web+Client+Plug-in+Developers+Guide.

## Getting help
Feel free to contact us at help@eurekaclinical.org.

