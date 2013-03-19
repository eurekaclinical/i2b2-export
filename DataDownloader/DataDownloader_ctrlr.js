/**
 * @projectDescription	Example using the Patient Data Object (PDO).
 * @inherits	i2b2
 * @namespace	i2b2.DataDownloader
 * @author	Nick Benik, Griffin Weber MD PhD
 * @version 	1.3
 * ----------------------------------------------------------------------------------------
 * updated 11-06-08: 	Initial Launch [Nick Benik] 
 */

i2b2.DataDownloader.proxyUrl = '/HelloI2b2Rest/rest/hello';
i2b2.DataDownloader.initialReq = 150;

i2b2.DataDownloader.Init = function(loadedDiv) {
	// register DIV as valid DragDrop target for Patient Record Sets (PRS) objects
	var op_trgt = {dropTarget:true};
	i2b2.sdx.Master.AttachType("DataDownloader-CONCPTDROP-1", "CONCPT", op_trgt);
	i2b2.sdx.Master.AttachType("DataDownloader-PRSDROP", "PRS", op_trgt);
	// drop event handlers used by this plugin
	i2b2.sdx.Master.setHandlerCustom("DataDownloader-CONCPTDROP-1", "CONCPT", "DropHandler", 
		function(sdxData) {
                        sdxData = sdxData[0]; // only interested in first concept
                        i2b2.DataDownloader.model.configuration.columnConfigs['1'].conceptRecord = sdxData;
                        // let the user know that the drop was successful by displaying the name of the concept
                        $("DataDownloader-CONCPTDROP-1").innerHTML = i2b2.h.Escape(sdxData.sdxInfo.sdxDisplayName);
                        // temporarly change background color to give GUI feedback of a successful drop occuring
                        $("DataDownloader-CONCPTDROP-1").style.background = "#CFB";
                        setTimeout("$('DataDownloader-CONCPTDROP-1').style.background='#DEEBEF'", 250);
			$("DataDownloader-CONCPTDROP-1").className = "droptrgt SDX-CONCPT";		
	
			// populate the column name field with the name of the concept
			$("DataDownloader-columnName-1").value = i2b2.h.Escape(sdxData.sdxInfo.sdxDisplayName);

			// populate the display format dropdown based on the type of concept dropped	
			var dispFmtSel = $("DataDownloader-dispfmt-select-1");
			while (dispFmtSel.hasChildNodes()) {
				dispFmtSel.removeChild(dispFmtSel.lastChild);
			}

			var selectOpt = document.createElement("option");
			selectOpt.disabled = true;
			selectOpt.selected = true;
			selectOpt.text = "Select one:";
			dispFmtSel.appendChild(selectOpt);

			var existOpt = document.createElement("option");
			existOpt.value = "existence";
			existOpt.text = "Existence";
			
			var valOpt = document.createElement("option");
			valOpt.value = "value";
			valOpt.text = "Value";
			
			dispFmtSel.appendChild(existOpt);
			dispFmtSel.appendChild(valOpt);
			
			var lvMetaDatas = i2b2.h.XPath(sdxData.origData.xmlOrig, 'metadataxml/ValueMetadata');
			if (lvMetaDatas.length > 0) {
				var aggOpt = document.createElement("option");
				aggOpt.value = "aggregation";
				aggOpt.text = "Aggregation";

				dispFmtSel.appendChild(aggOpt);
			}

			// clear out the display format specific options
		        $("DataDownloader-timerange-container-1").style.display = "none";
			$("DataDownloader-units-container-1").style.display = "none";
        	        $("DataDownloader-aggregation-container-1").style.display = "none";
	                $("DataDownloader-howmany-container-1").style.display = "none";

                        // optimization to prevent requerying the hive for new results if the input dataset has not changed
                        i2b2.DataDownloader.model.dirtyResultsData = true;
		}
	);
	i2b2.sdx.Master.setHandlerCustom("DataDownloader-PRSDROP", "PRS", "DropHandler", i2b2.DataDownloader.prsDropped);
	// initialize configuration object
	i2b2.DataDownloader.model.configuration = {};
	i2b2.DataDownloader.model.configuration.columnConfigs = [];

	// initialize the first configuration
	i2b2.DataDownloader.initColumnConfig('1');

	$("DataDownloader-loadConfig").addEventListener("change", i2b2.DataDownloader.setSaveConfigName);
	$("DataDownloader-upBtn-1").addEventListener("click", i2b2.DataDownloader.moveUp);
	$("DataDownloader-dnBtn-1").addEventListener("click", i2b2.DataDownloader.moveDown);
	$("DataDownloader-dispfmt-select-1").addEventListener("change", i2b2.DataDownloader.onDispFmtChange);
	$("DataDownloader-addColumnBtn").addEventListener("click", i2b2.DataDownloader.addColumnConfig);
	$("DataDownloader-previewBtn").addEventListener("click", i2b2.DataDownloader.generatePreview);
	$("DataDownloader-saveonly").addEventListener("click", i2b2.DataDownloader.getResults);

	
	// manage YUI tabs
	this.yuiTabs = new YAHOO.widget.TabView("DataDownloader-TABS", {activeIndex:0});
	this.yuiTabs.on('activeTabChange', function(ev) { 
		//Tabs have changed 
		if (ev.newValue.get('id')=="DataDownloader-TAB1") {
			// user switched to Results tab
			if (i2b2.DataDownloader.model.concepts && i2b2.DataDownloader.model.concepts.length > 0 && i2b2.DataDownloader.model.prsRecord) {
				// contact PDO only if we have data
				if (i2b2.DataDownloader.model.dirtyResultsData) {
					// recalculate the results only if the input data has changed
					i2b2.DataDownloader.getResults();
				}
			}
		}
	});

	YAHOO.util.Event.addListener(window, 'resize', i2b2.DataDownloader.resizeDataTable);
};

i2b2.DataDownloader.setSaveConfigName = function(evt) {
	$("DataDownloader-saveas").value = evt.target.value;
};

i2b2.DataDownloader.initColumnConfig = function(index) {
	i2b2.DataDownloader.model.configuration.columnConfigs[index] = {};
        i2b2.DataDownloader.model.configuration.columnConfigs[index].conceptRecord = null;
        i2b2.DataDownloader.model.configuration.columnConfigs[index].columnName = '';
        i2b2.DataDownloader.model.configuration.columnConfigs[index].displayFormat = '';
        i2b2.DataDownloader.model.configuration.columnConfigs[index].howMany = 0;
	i2b2.DataDownloader.model.configuration.columnConfigs[index].includeTimeRange = false;
	i2b2.DataDownloader.model.configuration.columnConfigs[index].includeUnits = false;
	i2b2.DataDownloader.model.configuration.columnConfigs[index].aggregation = '';
};

i2b2.DataDownloader.onDispFmtChange = function(evt) {
	var value = evt.target.value;
	var index = evt.target.id.split('-')[3];
	if (value === "existence") {
		$("DataDownloader-timerange-container-" + index).style.display = "none";
		$("DataDownloader-howmany-container-" + index).style.display = "none";
		$("DataDownloader-units-container-" + index).style.display = "none";
		$("DataDownloader-aggregation-container-" + index).style.display = "none";
	} else if (value === "value") {
		$("DataDownloader-timerange-container-" + index).style.display = "block";
		$("DataDownloader-timerange-container-" + index).style.marginBottom = "5px";
		$("DataDownloader-howmany-container-" + index).style.display = "block";
		var lvMetaDatas = i2b2.h.XPath(i2b2.DataDownloader.model.configuration.columnConfigs[index].conceptRecord.origData.xmlOrig, 'metadataxml/ValueMetadata');
		if (lvMetaDatas.length > 0) {
			$("DataDownloader-units-container-" + index).style.display = "block";
		} else {
			$("DataDownloader-units-container-" + index).style.display = "none";
		}
		$("DataDownloader-aggregation-container-" + index).style.display = "none";
	} else if (value === "aggregation") {
		$("DataDownloader-timerange-container-" + index).style.display = "none";
		$("DataDownloader-howmany-container-" + index).style.display = "none";
		$("DataDownloader-units-container-" + index).style.display = "block";
		$("DataDownloader-aggregation-container-" + index).style.display = "block";
	}
};

i2b2.DataDownloader.updateColumnConfigFirstRow = function() {
	var table = $("DataDownloader-configTable");
	var delCell = table.rows[1].cells[0];
	while (delCell.hasChildNodes()) {
		delCell.removeChild(delCell.lastChild);
	}
	var img = document.createElement('img');
	if (table.rows.length === 2) {
		img.src = "http://placehold.it/35/dbe8ff";
		delCell.appendChild(img);
	} else {
	        img.src = "http://placehold.it/35/dbe8ff/ff0000&text=x";
        	//img.style.border = "1px solid red";
	        img.addEventListener("click", i2b2.DataDownloader.deleteBtnClickListener);
        	var anchor = document.createElement('a');
	        anchor.href = "#";
        	anchor.appendChild(img);
	        delCell.className = "deleteBtn";
	        delCell.appendChild(anchor);
	}	
};

i2b2.DataDownloader.addColumnConfig = function() {
	var table = $("DataDownloader-configTable");
	i2b2.DataDownloader.addColumnConfigRow(table);
	i2b2.DataDownloader.updateColumnConfigFirstRow();
};

i2b2.DataDownloader.deleteBtnClickListener = function(evt) {
	var node = evt.target;
	while (node.nodeName !== 'TR') {
		node = node.parentNode;
	}
	var tr = node;
	tr.parentNode.removeChild(tr);
	var index = tr.id.split('-')[2];
	delete i2b2.DataDownloader.model.configuration.columnConfigs[index];
	i2b2.DataDownloader.updateColumnConfigFirstRow();
};

i2b2.DataDownloader.moveDown = function(evt) {
	var element = evt.target.ancestors()[2];
	var next = element.next();
	if (next) {
		next.remove();
		element.insert({before:next});
	}
};

i2b2.DataDownloader.moveUp = function(evt) {
	var element = evt.target.ancestors()[2];
	var prev = element.previous();
	if (prev && prev.id !== "DataDownloader-headerRow") {
		prev.remove();
		element.insert({after:prev});
	}
};

i2b2.DataDownloader.addColumnConfigRow = function(table) {
	var index = 1 + parseInt(table.rows[table.rows.length - 1].id.split('-')[2]);
	i2b2.DataDownloader.initColumnConfig(index);
	var tr = table.insertRow(-1);
	tr.id = "DataDownloader-columnConfig-" + index;

	var delCell = document.createElement('td');
	var img = document.createElement('img');
	img.src = "http://placehold.it/35/dbe8ff/ff0000&text=x";
	//img.style.border = "1px solid red";
	img.addEventListener("click", i2b2.DataDownloader.deleteBtnClickListener);
	var anchor = document.createElement('a');
	anchor.href = "#";
	anchor.appendChild(img);
	delCell.className = "deleteBtn";
	delCell.appendChild(anchor);
	tr.appendChild(delCell);

	var reorderCell = document.createElement('td');
	var upImg = document.createElement('img');
	upImg.src = "http://placehold.it/20/dbe8ff/00ff00&text=UP";
	upImg.addEventListener("click", i2b2.DataDownloader.moveUp);
	var upAnchor = document.createElement('a');
	upAnchor.href = '#';
	upAnchor.appendChild(upImg);
	var dnImg = document.createElement('img');
	dnImg.src = "http://placehold.it/20/dbe8ff/00ff00&text=DN";
	dnImg.addEventListener("click", i2b2.DataDownloader.moveDown);
	var dnAnchor = document.createElement('a');
	dnAnchor.href = '#';
	dnAnchor.appendChild(dnImg);
	reorderCell.appendChild(upAnchor);
	reorderCell.appendChild(document.createElement('br'));
	reorderCell.appendChild(dnAnchor);
	tr.appendChild(reorderCell);

	var concptCell = document.createElement('td');
	var concptDiv = document.createElement('div');
	var concptSpan = document.createElement('span');
	concptSpan.className = "droptrgtInit";
	concptSpan.textContent = "Drop a concept here";
	concptDiv.className = "droptrgtInit SDX-CONCPT";
	concptDiv.id = "DataDownloader-CONCPTDROP-" + index;
	concptDiv.appendChild(concptSpan);
	concptCell.className = "droptrgtCell";
	concptCell.appendChild(concptDiv);
	tr.appendChild(concptCell);

	i2b2.sdx.Master.AttachType("DataDownloader-CONCPTDROP-" + index, "CONCPT", {dropTarget:true});
	i2b2.sdx.Master.setHandlerCustom("DataDownloader-CONCPTDROP-" + index, "CONCPT", "DropHandler", 
		function(sdxData) {
			sdxData = sdxData[0]; // only interested in first concept
			i2b2.DataDownloader.model.configuration.columnConfigs[index.toString()].conceptRecord = sdxData;
			// let the user know that the drop was successful by displaying the name of the concept
			$("DataDownloader-CONCPTDROP-" + index).innerHTML = i2b2.h.Escape(sdxData.sdxInfo.sdxDisplayName);
			// temporarly change background color to give GUI feedback of a successful drop occuring
			$("DataDownloader-CONCPTDROP-" + index).style.background = "#CFB";
			setTimeout("$('DataDownloader-CONCPTDROP-" + index + "').style.background='#DEEBEF'", 250);	
			$("DataDownloader-CONCPTDROP-" + index).className = "droptrgt SDX-CONCPT";
			
			// populate the column name field with the name of the concept
			$("DataDownloader-columnName-" + index).value = i2b2.h.Escape(sdxData.sdxInfo.sdxDisplayName);

			// populate the display format dropdown based on the type of concept dropped	
			var dispFmtSel = $("DataDownloader-dispfmt-select-" + index);
			while (dispFmtSel.hasChildNodes()) {
				dispFmtSel.removeChild(dispFmtSel.lastChild);
			}

			var selectOpt = document.createElement("option");
			selectOpt.disabled = true;
			selectOpt.selected = true;
			selectOpt.text = "Select one:";
			dispFmtSel.appendChild(selectOpt);

			var existOpt = document.createElement("option");
			existOpt.value = "existence";
			existOpt.text = "Existence";
			
			var valOpt = document.createElement("option");
			valOpt.value = "value";
			valOpt.text = "Value";

			dispFmtSel.appendChild(existOpt);
			dispFmtSel.appendChild(valOpt);
			
			var lvMetaDatas = i2b2.h.XPath(sdxData.origData.xmlOrig, 'metadataxml/ValueMetadata');
			if (lvMetaDatas.length > 0) {
				var aggOpt = document.createElement("option");
				aggOpt.value = "aggregation";
				aggOpt.text = "Aggregation";

				dispFmtSel.appendChild(aggOpt);
			}

			// clear out the display format specific options
		        $("DataDownloader-timerange-container-" + index).style.display = "none";
			$("DataDownloader-units-container-" + index).style.display = "none";
        	        $("DataDownloader-aggregation-container-" + index).style.display = "none";
	                $("DataDownloader-howmany-container-" + index).style.display = "none";
			// optimization to prevent requerying the hive for new results if the input dataset has not changed
			i2b2.DataDownloader.model.dirtyResultsData = true;		
		}
	);

	var colNameCell = document.createElement('td');
	colNameCell.className = 'columnNameCell';
	var colNameText = document.createElement('input');
	colNameText.type = 'text';
	colNameText.id = 'DataDownloader-columnName-' + index;
	colNameText.name = 'columnName-' + index;
	colNameText.placeholder = 'Column name';
	colNameText.size = '16';
	colNameText.maxLength = '32';
	colNameCell.appendChild(colNameText);
	tr.appendChild(colNameCell);

	var dispfmtCell = document.createElement('td');
	dispfmtCell.className = 'dispfmt';
	var dispfmtSelect = document.createElement('select');
	dispfmtSelect.id = 'DataDownloader-dispfmt-select-' + index;
	dispfmtSelect.name = 'dispfmt-' + index;
	dispfmtSelect.addEventListener("change", i2b2.DataDownloader.onDispFmtChange);
	var dispfmtOpt = document.createElement('option');
	dispfmtOpt.disabled = true;
	dispfmtOpt.selected = true;
	dispfmtOpt.text = 'Select a concept first';
	dispfmtSelect.appendChild(dispfmtOpt);
	dispfmtCell.appendChild(dispfmtSelect);
	tr.appendChild(dispfmtCell);

	var optionsCell = document.createElement('td');
	
	var showTimeDiv = document.createElement('div');
	showTimeDiv.className = 'options';
	showTimeDiv.id = 'DataDownloader-timerange-container-' + index;
	showTimeDiv.style.display = 'none';
	var showTimeLbl = document.createElement('label');
	showTimeLbl.htmlFor = 'DataDownloader-timerange-' + index;
	showTimeLbl.textContent = 'Include time range';
	var showTimeCheck = document.createElement('input');
	showTimeCheck.type = 'checkbox';
	showTimeCheck.name = 'timerange-' + index;
	showTimeCheck.id = 'DataDownloader-timerange-' + index;
	showTimeDiv.appendChild(showTimeCheck);
	showTimeDiv.appendChild(showTimeLbl);

	var unitsDiv = document.createElement('div');
	unitsDiv.className = 'options';
	unitsDiv.id = 'DataDownloader-units-container-' + index;
	unitsDiv.style.display = 'none';
	var unitsLbl = document.createElement('label');
	unitsLbl.htmlFor = 'DataDownloader-units-' + index;
	unitsLbl.textContent = 'Include units';
	var unitsCheck = document.createElement('input');
	unitsCheck.type = 'checkbox';
	unitsCheck.name = 'units-' + index;
	unitsCheck.id = 'DataDownloader-units-' + index;
	unitsDiv.appendChild(unitsCheck);
	unitsDiv.appendChild(unitsLbl);

	var howManyDiv = document.createElement('div');
	howManyDiv.className = 'options';
	howManyDiv.id = 'DataDownloader-howmany-container-' + index;
	howManyDiv.style.display = 'none';

	var howManyLbl = document.createElement('label');
	howManyLbl.style.paddingLeft = '5px';
	howManyLbl.htmlFor = 'DataDownloader-howmany-' + index;
	howManyLbl.textContent = 'How many? ';

	var howManyText = document.createElement('input');
	howManyText.id = 'DataDownloader-howmany-' + index;
	howManyText.type = 'text';
	howManyText.name = 'howmany-' + index;
	howManyText.value = '1';
	howManyText.size = '4';

	howManyDiv.appendChild(howManyLbl);
	howManyDiv.appendChild(howManyText);
	
	var aggDiv = document.createElement('div');
	aggDiv.className = 'options';
	aggDiv.id = 'DataDownloader-aggregation-container-' + index;
	aggDiv.style.display = 'none';
	var minLbl = document.createElement('label');
	minLbl.htmlFor = 'DataDownloader-min-' + index;
	minLbl.className = 'aggMargin';
	minLbl.textContent = 'Min';
	var minRadio = document.createElement('input');
	minRadio.type = 'radio';
	minRadio.name = 'aggregation-' + index;
	minRadio.id = 'DataDownloader-min-' + index;
	minRadio.value = 'min';

	var maxLbl = document.createElement('label');
	maxLbl.htmlFor = 'DataDownloader-max-' + index;
	maxLbl.className = 'aggMargin';
	maxLbl.textContent = 'Max';
	var maxRadio = document.createElement('input');
	maxRadio.type = 'radio';
	maxRadio.name = 'aggregation-' + index;
	maxRadio.id = 'DataDownloader-max-' + index;
	maxRadio.value = 'max';

	var avgLbl = document.createElement('label');
	avgLbl.htmlFor = 'DataDownloader-avg-' + index;
	avgLbl.className = 'aggMargin';
	avgLbl.textContent = 'Avg';
	var avgRadio = document.createElement('input');
	avgRadio.type = 'radio';
	avgRadio.name = 'aggregation-' + index;
	avgRadio.id = 'DataDownloader-avg-' + index;
	avgRadio.value = 'avg';

	aggDiv.appendChild(minRadio);
	aggDiv.appendChild(minLbl);
	aggDiv.appendChild(maxRadio);
	aggDiv.appendChild(maxLbl);
	aggDiv.appendChild(avgRadio);
	aggDiv.appendChild(avgLbl);

	optionsCell.appendChild(howManyDiv);
	optionsCell.appendChild(aggDiv);
	optionsCell.appendChild(unitsDiv);
	optionsCell.appendChild(showTimeDiv);

	tr.appendChild(optionsCell);
	
};

i2b2.DataDownloader.assembleColumnConfig = function(index) {
	// the concept object is populated at drop-time, so we just focus on the other fields here
	var config = i2b2.DataDownloader.model.configuration.columnConfigs[index];
	config.columnName = $("DataDownloader-columnName-" + index).value;
	config.displayFormat = $("DataDownloader-dispfmt-select-" + index).value;
	switch (config.displayFormat) {
		case "value":
			config.howMany = parseInt($("DataDownloader-howmany-" + index).value);
			config.includeTimeRange = $("DataDownloader-timerange-" + index).value;
			config.includeUnits = $("DataDownloader-units-" + index).value;
			break;
		case "aggregation":
			var aggs = document.getElementsByName("aggregation-" + index);
			for (var i = 0; i < aggs.length; i++) {
				if (aggs[i].checked) {
					config.aggregation = aggs[i].value;
				}
			}
			config.includeUnits = $("DataDownloader-units-" + index).value;
			break;
		default:
			break;
	}
};

i2b2.DataDownloader.assembleConfig = function() {
	var table = $("DataDownloader-configTable");
	for (var i = 1; i < table.rows.length; i++) {
		var index = table.rows[i].id.split('-')[2];
		if (i2b2.DataDownloader.model.configuration.columnConfigs[index].conceptRecord) {
			i2b2.DataDownloader.assembleColumnConfig(index);
		}
	}
	i2b2.DataDownloader.model.configuration.rowDimension = $("DataDownloader-rowdim").value;
	i2b2.DataDownloader.model.configuration.whitespace = $("DataDownloader-whitespace").value;
	i2b2.DataDownloader.model.configuration.delimiter = $("DataDownloader-delimiter").value;
	i2b2.DataDownloader.model.configuration.missing = $("DataDownloader-missing").value;
};

i2b2.DataDownloader.generatePreview = function() {
	i2b2.DataDownloader.assembleConfig();
	var previewStr = '';
	switch (i2b2.DataDownloader.model.configuration.rowDimension) {
		case "patient":
			previewStr += 'PatientID';
			break;
		case "visit":
			previewStr += 'VisitID';
			break;
		case "provider":
			previewStr += 'ProviderID';
			break;
	}
	i2b2.DataDownloader.model.configuration.columnConfigs.forEach(function(config) {
		if (config.conceptRecord) {
			var colName = config.columnName;
			if (i2b2.DataDownloader.model.configuration.whitespace !== '') {
				colName = colName.replace(' ', i2b2.DataDownloader.model.configuration.whitespace, 'g');
			}
			if (config.displayFormat === 'value') {
				var howMany = config.howMany;
				if (!howMany || howMany < 1) {
					howMany = 1;
				}
				for (var i = 0; i < howMany; i++) {
					previewStr += i2b2.DataDownloader.model.configuration.delimiter + colName + '_value';
			
					if (config.includeUnits) {
						previewStr += i2b2.DataDownloader.model.configuration.delimiter + colName + '_unit';
					}
					if (config.includeTimeRange) {
						previewStr += i2b2.DataDownloader.model.configuration.delimiter + colName + '_start' + i2b2.DataDownloader.model.configuration.delimiter + colName + '_end';
					}	
				}
			} else if (config.displayFormat === 'aggregation') {
				colName += '_' + config.aggregation;
				previewStr += i2b2.DataDownloader.model.configuration.delimiter + colName;
				if (config.includeUnits) {
					previewStr += i2b2.DataDownloader.model.configuration.delimiter + colName + '_unit';
				}
			} else {
				previewStr += i2b2.DataDownloader.model.configuration.delimiter + colName;
			} 
		}	
	});	
	$("DataDownloader-previewText").textContent = previewStr;
};

i2b2.DataDownloader.Unload = function() {
	// purge old data
	i2b2.DataDownloader.model.configuration = false;
	i2b2.DataDownloader.model.prsRecord = false;
	i2b2.DataDownloader.model.dirtyResultsData = true;
	return true;
};

i2b2.DataDownloader.prsDropped = function(sdxData) {
	sdxData = sdxData[0];	// only interested in first record
	// save the info to our local data model
	i2b2.DataDownloader.model.prsRecord = sdxData;
	// let the user know that the drop was successful by displaying the name of the patient set
	$("DataDownloader-PRSDROP").innerHTML = i2b2.h.Escape(sdxData.sdxInfo.sdxDisplayName);
	// temporarly change background color to give GUI feedback of a successful drop occuring
	$("DataDownloader-PRSDROP").style.background = "#CFB";
	setTimeout("$('DataDownloader-PRSDROP').style.background='#DEEBEF'", 250);	
	// optimization to prevent requerying the hive for new results if the input dataset has not changed
	i2b2.DataDownloader.model.dirtyResultsData = true;		
};

i2b2.DataDownloader.buildMsg = function(params, noescparams){
	var tag_values = {};
	var self = i2b2.DataDownloader.model;
	var sUrl;
	var tmpl;
	var message;
	var proxyUrl;
	var tags = [];
	var syntax = /(^|.|\r|\n)(\{{{\s*(\w+)\s*}}})/; //matches symbols like '{{{ field }}}'
	var commObj = eval("(i2b2.CRC.ajax)");
	var commFunc = "getPDO_fromInputList";
	var commMsg = commObj._commData[commFunc];

	tmpl = new Template(commMsg.url, syntax);
	sUrl = i2b2.h.Escape(i2b2[commObj.ParentCell].cfg.cellURL,syntax);
	sUrl = tmpl.evaluate({URL: sUrl});

	params.funcURL = sUrl;
	params.proxy_info = '';
	proxyUrl = i2b2.h.getProxy();

	if (proxyUrl) {
		params.proxy_info = '<proxy>\n            <redirect_url>' + sUrl + '</redirect_url>\n        </proxy>\n';
	} else {
		proxyUrl = sUrl;
	}
	params.proxyURL = proxyUrl;

	noescparams = noescparams.concat(commMsg.dont_escape_params);

	// apply message values to message template
	i2b2.h.EscapeTemplateVars(params, noescparams);
	tmpl = new Template(commMsg.msg, syntax);
	message = tmpl.evaluate(params);
	return message;
};


i2b2.DataDownloader.getResults = function() {
	
	//if (i2b2.DataDownloader.model.dirtyResultsData) {

		patientLimit = i2b2.DataDownloader.model.prsRecord.origData.size;
		var filterList = '';
		// translate the concept XML for injection as PDO item XML
		/*for (var i1=0; i1<i2b2.DataDownloader.model.concepts.length; i1++) {
			var t = i2b2.DataDownloader.model.concepts[i1].origData.xmlOrig;
			var cdata = {};
			cdata.level = i2b2.h.getXNodeVal(t, "level");
			cdata.key = i2b2.h.getXNodeVal(t, "key");
			cdata.tablename = i2b2.h.getXNodeVal(t, "tablename");
			cdata.dimcode = i2b2.h.getXNodeVal(t, "dimcode");
			cdata.synonym = i2b2.h.getXNodeVal(t, "synonym_cd");
		
			filterList += 
			'	<panel name="'+cdata.key+'">\n'+
			'		<panel_number>0</panel_number>\n'+
			'		<panel_accuracy_scale>0</panel_accuracy_scale>\n'+
			'		<invert>0</invert>\n'+
			'		<item>\n'+
			'			<hlevel>'+cdata.level+'</hlevel>\n'+
			'			<item_key>'+cdata.key+'</item_key>\n'+
			'			<dim_tablename>'+cdata.tablename+'</dim_tablename>\n'+
			'			<dim_dimcode>'+cdata.dimcode+'</dim_dimcode>\n'+
			'			<item_is_synonym>'+cdata.synonym+'</item_is_synonym>\n'+
			'		</item>\n'+
			'	</panel>\n';			
		}*/
		var msg_filter = '<input_list>\n' +
			'	<patient_list max="' + patientLimit + '" min="1">\n'+   // <--- only the first $patientLimit records
			'		<patient_set_coll_id>'+i2b2.DataDownloader.model.prsRecord.sdxInfo.sdxKeyValue+'</patient_set_coll_id>\n'+
			'	</patient_list>\n'+
			'</input_list>\n'+
			'<filter_list>\n'+
				filterList+
			'</filter_list>\n'+
			'<output_option>\n'+
			'	<patient_set select="using_input_list" onlykeys="false"/>\n'+
			'	<event_set select="using_input_list" onlykeys="false"/>\n'+
			'	<observation_set blob="true" onlykeys="false"/>\n'+
			'</output_option>\n';

		
		// AJAX CALL USING THE EXISTING CRC CELL COMMUNICATOR
		//i2b2.CRC.ajax.getPDO_fromInputList("Plugin:DataDownloader", {patient_limit:5, PDO_Request: msg_filter}, scopedCallback);

		var params = {
			sec_user: i2b2.h.getUser(),
			sec_domain: i2b2.h.getDomain(),
			sec_pass_node: i2b2.h.getPass(),
			sec_project: i2b2.h.getProject(),
			header_msg_id: i2b2.h.GenerateAlphaNumId(20),
			msg_datetime: i2b2.h.GenerateISO8601DateTime(),
			result_wait_time: 120,
			patient_limit: patientLimit,
			PDO_Request: msg_filter
		};
		var xml = i2b2.DataDownloader.buildMsg(params, []);
		var cbFunc = function (transport) {
			console.log('status: ' + transport.status);
			if (transport.status == 200) {
				// optimization - only requery when the input data is changed
				i2b2.DataDownloader.model.dirtyResultsData = false;
			} else if (transport.status == 204 || (Prototype.Browser.IE && transport.status == 1223)) {
				// a bug in IE causes it to report 204 as 1223
			} else {
				alert("An error occurred while processing your request. If this continues to happen, please report it to the AIW team.");
				//alert(transport.status);
			}

		};

		var commOpts = {
			contentType: 'application/x-www-form-urlencoded',
			method: 'post',
			asynchronous: true,
			evalJS: false,
			evalJSON: false,
			parameters: { "requestXml": xml, "initialReq": i2b2.DataDownloader.initialReq },
			//postBody: xml,
			onSuccess: cbFunc,
			onFailure: cbFunc
			//onFailure: function (transport) { alert("Call Failed!"); }
		};

		//var req = new Ajax.Request("http://aiwdev02.eushc.org/i2b2/index.php", commOpts);
		//if (format) {
			var url = i2b2.DataDownloader.proxyUrl;// + "?format=" + format + "&initialReq=" + i2b2.DataDownloader.initialReq;
			var form = $$("FORM#DataDownloader-Form")[0];
			form.action = url;
			form.method = "POST";
			form.elements["requestXml"].value = xml;
			alert("submitting the form");
			form.submit();
			//var req = new Ajax.Request(i2b2.DataDownloader.proxyUrl + "?format=" + format, commOpts);
			//iframe.location = url;
		//} else {
		//	var req = new Ajax.Request(i2b2.DataDownloader.proxyUrl, commOpts);
		//}
	//}
};
