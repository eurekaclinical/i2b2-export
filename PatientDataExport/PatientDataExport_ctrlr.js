/**
 * @projectDescription  Allows users to export patient data and configure the output format
 * @inherits	i2b2
 * @namespace	i2b2.PatientDataExport
 * @author	Michel Mansour
 * @version 	1.0
 * ----------------------------------------------------------------------------------------
 * updated 2013-04-15: 	Initial Launch [Michel Mansour] 
 */

i2b2.PatientDataExport.SERVICE_URL = 'http://192.168.232.128/I2b2PatientDataExportService/rest';
// global row index counter because the concept drop handlers
// seem to fail if a handler is applied to the same object twice in the same session
i2b2.PatientDataExport.ROW_INDEX = 1;

i2b2.PatientDataExport.Init = function(loadedDiv) {
    // register DIV as valid DragDrop target for Patient Record Sets (PRS) objects
    var op_trgt = {dropTarget:true};
    i2b2.sdx.Master.AttachType('PatientDataExport-CONCPTDROP-1', 'CONCPT', op_trgt);
    i2b2.sdx.Master.AttachType('PatientDataExport-PRSDROP', 'PRS', op_trgt);
    // drop event handlers used by this plugin
    i2b2.sdx.Master.setHandlerCustom('PatientDataExport-CONCPTDROP-1', 'CONCPT', 'DropHandler', function(sdxData) { i2b2.PatientDataExport.conceptDropped(sdxData, 1); });
    i2b2.sdx.Master.setHandlerCustom('PatientDataExport-PRSDROP', 'PRS', 'DropHandler', i2b2.PatientDataExport.prsDropped);

    // initialize configuration object
    i2b2.PatientDataExport.model.configuration = {};
    i2b2.PatientDataExport.model.configuration.columnConfigs = [];
    
    // initialize the first configuration
    i2b2.PatientDataExport.initColumnConfig('1');
    
    $('PatientDataExport-userConfigsList').addEventListener('change', i2b2.PatientDataExport.userConfigSelected);
    $('PatientDataExport-upBtn-1').addEventListener('click', i2b2.PatientDataExport.moveUp);
    $('PatientDataExport-dnBtn-1').addEventListener('click', i2b2.PatientDataExport.moveDown);
    $('PatientDataExport-dispfmt-select-1').addEventListener('change', i2b2.PatientDataExport.onDispFmtChange);
    $('PatientDataExport-addColumnBtn').addEventListener('click', i2b2.PatientDataExport.addColumnConfig);
    $('PatientDataExport-previewBtn').addEventListener('click', i2b2.PatientDataExport.generatePreview);
    $('PatientDataExport-saveonly').addEventListener('click', i2b2.PatientDataExport.saveConfig);
    $('PatientDataExport-export').addEventListener('click', i2b2.PatientDataExport.exportData);
    $('PatientDataExport-deleteConfigBtn').addEventListener('click', i2b2.PatientDataExport.deleteConfig);
    $('PatientDataExport-loadConfigBtn').addEventListener('click', i2b2.PatientDataExport.loadConfig);
    
    // populate the list of configurations
    i2b2.PatientDataExport.populateConfigList();
    
    // manage YUI tabs
    this.yuiTabs = new YAHOO.widget.TabView('PatientDataExport-TABS', {activeIndex:0});
};

i2b2.PatientDataExport.Unload = function() {
    // purge old data
    i2b2.PatientDataExport.model.configuration = false;
    i2b2.PatientDataExport.model.prsRecord = false;
    return true;
};

i2b2.PatientDataExport.conceptDropped = function(sdxData, index) {
    sdxData = sdxData[0]; // only interested in first concept
    i2b2.PatientDataExport.model.configuration.columnConfigs[index.toString()].conceptRecord = sdxData;
    // let the user know that the drop was successful by displaying the name of the concept
    $('PatientDataExport-CONCPTDROP-' + index).innerHTML = i2b2.h.Escape(sdxData.sdxInfo.sdxDisplayName);
    // temporarly change background color to give GUI feedback of a successful drop occuring
    $('PatientDataExport-CONCPTDROP-' + index).style.background = '#CFB';
    setTimeout("$('PatientDataExport-CONCPTDROP-" + index + "').style.background='#DEEBEF'", 250);	
    $('PatientDataExport-CONCPTDROP-' + index).className = 'droptrgt SDX-CONCPT';
    
    // populate the column name field with the name of the concept
    $('PatientDataExport-columnName-' + index).value = i2b2.h.Escape(sdxData.sdxInfo.sdxDisplayName);
    
    // populate the display format dropdown based on the type of concept dropped	
    i2b2.PatientDataExport.populateDispFmtSelect(index, sdxData);		
    
    // clear out the display format specific options
    $('PatientDataExport-timerange-container-' + index).style.display = 'none';
    $('PatientDataExport-units-container-' + index).style.display = 'none';
    $('PatientDataExport-aggregation-container-' + index).style.display = 'none';
    $('PatientDataExport-howmany-container-' + index).style.display = 'none';
}

i2b2.PatientDataExport.prsDropped = function(sdxData) {
    sdxData = sdxData[0];	// only interested in first record
    // save the info to our local data model
    i2b2.PatientDataExport.model.prsRecord = sdxData;
    // let the user know that the drop was successful by displaying the name of the patient set
    $('PatientDataExport-PRSDROP').innerHTML = i2b2.h.Escape(sdxData.sdxInfo.sdxDisplayName);
    // temporarly change background color to give GUI feedback of a successful drop occuring
    $('PatientDataExport-PRSDROP').style.background = '#CFB';
    setTimeout("$('PatientDataExport-PRSDROP').style.background='#DEEBEF'", 250);	
    // enable the export button
    $('PatientDataExport-export').disabled = false;
};

i2b2.PatientDataExport.createI2b2AuthRequestObject = function() {
    return {
	domain: i2b2.h.getDomain(), 
	username: i2b2.h.getUser(), 
	passwordNode: i2b2.h.getPass(), 
	projectId: i2b2.h.getProject()
    };
};


i2b2.PatientDataExport.populateConfigList = function() {
    new Ajax.Request(i2b2.PatientDataExport.SERVICE_URL + '/config/getAll', {
	method: 'POST',
	contentType: 'application/json',
	postBody: Object.toJSON(i2b2.PatientDataExport.createI2b2AuthRequestObject()),
	requestHeaders: {'Accept': 'application/json'},
	asynchronous: true,
	onSuccess: function(response) {
	    var select = $('PatientDataExport-userConfigsList');
	    var configSummaries = response.responseJSON;
	    while (select.hasChildNodes()) {
		select.removeChild(select.firstChild);
	    }
	    configSummaries.forEach(function(config) {
		var opt = document.createElement('option');
		opt.value = config.configurationId;
		opt.text = config.configurationName;
		select.appendChild(opt);
	    });
	    $('PatientDataExport-loadConfigBtn').disabled = true;
	    $('PatientDataExport-deleteConfigBtn').disabled = true;
	}	
    });
};

i2b2.PatientDataExport.userConfigSelected = function(evt) {
    $('PatientDataExport-loadConfigBtn').disabled = false;
    $('PatientDataExport-deleteConfigBtn').disabled = false;
    for (var i = 0; i < evt.target.options.length; i++) {
		if (evt.target.options[i].selected === true) {
	    	$('PatientDataExport-saveas').value = evt.target.options[i].text;
	    	return;
		}
    }	
};

i2b2.PatientDataExport.initColumnConfig = function(index) {
    i2b2.PatientDataExport.model.configuration.columnConfigs[index] = {};
    i2b2.PatientDataExport.model.configuration.columnConfigs[index].conceptRecord = null;
    i2b2.PatientDataExport.model.configuration.columnConfigs[index].columnName = '';
    i2b2.PatientDataExport.model.configuration.columnConfigs[index].displayFormat = '';
    i2b2.PatientDataExport.model.configuration.columnConfigs[index].howMany = 0;
    i2b2.PatientDataExport.model.configuration.columnConfigs[index].includeTimeRange = false;
    i2b2.PatientDataExport.model.configuration.columnConfigs[index].includeUnits = false;
    i2b2.PatientDataExport.model.configuration.columnConfigs[index].aggregation = '';
};

i2b2.PatientDataExport.populateDispFmtSelect = function(index, sdxData) {
    var dispFmtSel = $('PatientDataExport-dispfmt-select-' + index);
    while (dispFmtSel.hasChildNodes()) {
        dispFmtSel.removeChild(dispFmtSel.lastChild);
    }

    var selectOpt = document.createElement('option');
    selectOpt.disabled = true;
    selectOpt.selected = true;
    selectOpt.text = 'Select one:';
    dispFmtSel.appendChild(selectOpt);
    
    var existOpt = document.createElement('option');
    existOpt.value = 'existence';
    existOpt.text = 'Existence';
    
    var valOpt = document.createElement('option');
    valOpt.value = 'value';
    valOpt.text = 'Value';
    
    dispFmtSel.appendChild(existOpt);
    dispFmtSel.appendChild(valOpt);
    
    var lvMetaDatas = i2b2.h.XPath(sdxData.origData.xmlOrig, 'metadataxml/ValueMetadata');
    if (lvMetaDatas.length > 0) {
	var aggOpt = document.createElement('option');
        aggOpt.value = 'aggregation';
        aggOpt.text = 'Aggregation';
	
        dispFmtSel.appendChild(aggOpt);
    }
};

i2b2.PatientDataExport.onDispFmtChange = function(evt) {
    var dispFmt = evt.target.value;
    var index = evt.target.id.split('-')[3];
    i2b2.PatientDataExport.showHideDispFmt(index, dispFmt);
};

i2b2.PatientDataExport.showHideDispFmt = function(index, dispFmt) {
    if (dispFmt === 'existence') {
		$('PatientDataExport-timerange-container-' + index).style.display = 'none';
		$('PatientDataExport-howmany-container-' + index).style.display = 'none';
		$('PatientDataExport-units-container-' + index).style.display = 'none';
		$('PatientDataExport-aggregation-container-' + index).style.display = 'none';
    } else if (dispFmt === 'value') {
		$('PatientDataExport-timerange-container-' + index).style.display = 'block';
		$('PatientDataExport-timerange-container-' + index).style.marginBottom = '5px';
		$('PatientDataExport-howmany-container-' + index).style.display = 'block';
		var lvMetaDatas = i2b2.h.XPath(i2b2.PatientDataExport.model.configuration.columnConfigs[index].conceptRecord.origData.xmlOrig, 'metadataxml/ValueMetadata');
		if (lvMetaDatas.length > 0) {
	    	$('PatientDataExport-units-container-' + index).style.display = 'block';
		} else {
	    	$('PatientDataExport-units-container-' + index).style.display = 'none';
		}
		$('PatientDataExport-aggregation-container-' + index).style.display = 'none';
    } else if (dispFmt === 'aggregation') {
		$('PatientDataExport-timerange-container-' + index).style.display = 'none';
		$('PatientDataExport-howmany-container-' + index).style.display = 'none';
		$('PatientDataExport-units-container-' + index).style.display = 'block';
		$('PatientDataExport-aggregation-container-' + index).style.display = 'block';
    }
};

i2b2.PatientDataExport.updateColumnConfigFirstRow = function() {
    var table = $('PatientDataExport-configTable');
    var delCell = table.rows[1].cells[0];
    while (delCell.hasChildNodes()) {
		delCell.removeChild(delCell.lastChild);
    }
    var img = document.createElement('img');
    
    // if only one config row is left, we don't want to allow deletion of it
    if (table.rows.length === 2) {
		img.src = 'http://placehold.it/35/dbe8ff';
		delCell.appendChild(img);
    } else {
		img.src = 'http://placehold.it/35/dbe8ff/ff0000&text=x';
		img.addEventListener('click', i2b2.PatientDataExport.deleteBtnClickListener);
        var anchor = document.createElement('a');
		anchor.href = '#';
        anchor.appendChild(img);
		delCell.className = 'deleteBtn';
		delCell.appendChild(anchor);
    }	
};

i2b2.PatientDataExport.addColumnConfig = function() {
    var table = $('PatientDataExport-configTable');
    var newIndex = i2b2.PatientDataExport.addColumnConfigRow(table);
    i2b2.PatientDataExport.updateColumnConfigFirstRow();
    
    return newIndex;
};

i2b2.PatientDataExport.deleteBtnClickListener = function(evt) {
    var node = evt.target;
    while (node.nodeName !== 'TR') {
		node = node.parentNode;
    }
    var tr = node;
    tr.parentNode.removeChild(tr);
    var index = tr.id.split('-')[2];
    delete i2b2.PatientDataExport.model.configuration.columnConfigs[index];
    i2b2.PatientDataExport.updateColumnConfigFirstRow();
};

i2b2.PatientDataExport.moveDown = function(evt) {
    var element = evt.target.ancestors()[2];
    var next = element.next();
    if (next) {
		next.remove();
		element.insert({before:next});
    }
};

i2b2.PatientDataExport.moveUp = function(evt) {
    var element = evt.target.ancestors()[2];
    var prev = element.previous();
    if (prev && prev.id !== 'PatientDataExport-headerRow') {
		prev.remove();
		element.insert({after:prev});
    }
};

i2b2.PatientDataExport.addColumnConfigRow = function(table) {
    i2b2.PatientDataExport.ROW_INDEX += 1; // increment the global row index counter
    var index = i2b2.PatientDataExport.ROW_INDEX;
    i2b2.PatientDataExport.initColumnConfig(index);
    var tr = table.insertRow(-1);
    tr.id = 'PatientDataExport-columnConfig-' + index;
    
    var delCell = document.createElement('td');
    var img = document.createElement('img');
    img.src = 'http://placehold.it/35/dbe8ff/ff0000&text=x';
    img.addEventListener('click', i2b2.PatientDataExport.deleteBtnClickListener);
    var anchor = document.createElement('a');
    anchor.href = '#';
    anchor.appendChild(img);
    delCell.className = 'deleteBtn';
    delCell.appendChild(anchor);
    tr.appendChild(delCell);
    
    var reorderCell = document.createElement('td');
    reorderCell.className = 'deleteBtn';
    var upImg = document.createElement('img');
    upImg.src = 'http://placehold.it/20/dbe8ff/00ff00&text=UP';
    upImg.addEventListener('click', i2b2.PatientDataExport.moveUp);
    var upAnchor = document.createElement('a');
    upAnchor.href = '#';
    upAnchor.appendChild(upImg);
    var dnImg = document.createElement('img');
    dnImg.src = 'http://placehold.it/20/dbe8ff/00ff00&text=DN';
    dnImg.addEventListener('click', i2b2.PatientDataExport.moveDown);
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
    concptSpan.className = 'droptrgtInit';
    concptSpan.textContent = 'Drop a concept here';
    concptDiv.className = 'droptrgtInit SDX-CONCPT';
    concptDiv.id = 'PatientDataExport-CONCPTDROP-' + index;
    concptDiv.appendChild(concptSpan);
    concptCell.className = 'droptrgtCell';
    concptCell.appendChild(concptDiv);
    tr.appendChild(concptCell);
    
    i2b2.sdx.Master.AttachType('PatientDataExport-CONCPTDROP-' + index, 'CONCPT', {dropTarget:true});
    i2b2.sdx.Master.setHandlerCustom('PatientDataExport-CONCPTDROP-' + index, 'CONCPT', 'DropHandler', 
		function(sdxData) { i2b2.PatientDataExport.conceptDropped(sdxData, index); });

    var colNameCell = document.createElement('td');
    colNameCell.className = 'columnNameCell';
    var colNameText = document.createElement('input');
    colNameText.type = 'text';
    colNameText.id = 'PatientDataExport-columnName-' + index;
    colNameText.name = 'columnName-' + index;
    colNameText.placeholder = 'Column name';
    colNameText.size = '16';
    colNameText.maxLength = '32';
    colNameCell.appendChild(colNameText);
    tr.appendChild(colNameCell);
    
    var dispfmtCell = document.createElement('td');
    dispfmtCell.className = 'dispfmt';
    var dispfmtSelect = document.createElement('select');
    dispfmtSelect.id = 'PatientDataExport-dispfmt-select-' + index;
    dispfmtSelect.name = 'dispfmt-' + index;
    dispfmtSelect.addEventListener('change', i2b2.PatientDataExport.onDispFmtChange);
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
    showTimeDiv.id = 'PatientDataExport-timerange-container-' + index;
    showTimeDiv.style.display = 'none';
    var showTimeLbl = document.createElement('label');
    showTimeLbl.htmlFor = 'PatientDataExport-timerange-' + index;
    showTimeLbl.textContent = 'Include time range';
    var showTimeCheck = document.createElement('input');
    showTimeCheck.type = 'checkbox';
    showTimeCheck.name = 'timerange-' + index;
    showTimeCheck.id = 'PatientDataExport-timerange-' + index;
    showTimeDiv.appendChild(showTimeCheck);
    showTimeDiv.appendChild(showTimeLbl);
    
    var unitsDiv = document.createElement('div');
    unitsDiv.className = 'options';
    unitsDiv.id = 'PatientDataExport-units-container-' + index;
    unitsDiv.style.display = 'none';
    var unitsLbl = document.createElement('label');
    unitsLbl.htmlFor = 'PatientDataExport-units-' + index;
    unitsLbl.textContent = 'Include units';
    var unitsCheck = document.createElement('input');
    unitsCheck.type = 'checkbox';
    unitsCheck.name = 'units-' + index;
    unitsCheck.id = 'PatientDataExport-units-' + index;
    unitsDiv.appendChild(unitsCheck);
    unitsDiv.appendChild(unitsLbl);
    
    var howManyDiv = document.createElement('div');
    howManyDiv.className = 'options';
    howManyDiv.id = 'PatientDataExport-howmany-container-' + index;
    howManyDiv.style.display = 'none';
    
    var howManyLbl = document.createElement('label');
    howManyLbl.style.paddingLeft = '5px';
    howManyLbl.htmlFor = 'PatientDataExport-howmany-' + index;
    howManyLbl.textContent = 'How many? ';
    
    var howManyText = document.createElement('input');
    howManyText.id = 'PatientDataExport-howmany-' + index;
    howManyText.type = 'text';
    howManyText.name = 'howmany-' + index;
    howManyText.value = '1';
    howManyText.size = '4';
    
    howManyDiv.appendChild(howManyLbl);
    howManyDiv.appendChild(howManyText);
    
    var aggDiv = document.createElement('div');
    aggDiv.className = 'options';
    aggDiv.id = 'PatientDataExport-aggregation-container-' + index;
    aggDiv.style.display = 'none';
    var minLbl = document.createElement('label');
    minLbl.htmlFor = 'PatientDataExport-min-' + index;
    minLbl.className = 'aggMargin';
    minLbl.textContent = 'Min';
    var minRadio = document.createElement('input');
    minRadio.type = 'radio';
    minRadio.name = 'aggregation-' + index;
    minRadio.id = 'PatientDataExport-min-' + index;
    minRadio.value = 'min';
    
    var maxLbl = document.createElement('label');
    maxLbl.htmlFor = 'PatientDataExport-max-' + index;
    maxLbl.className = 'aggMargin';
    maxLbl.textContent = 'Max';
    var maxRadio = document.createElement('input');
    maxRadio.type = 'radio';
    maxRadio.name = 'aggregation-' + index;
    maxRadio.id = 'PatientDataExport-max-' + index;
    maxRadio.value = 'max';
    
    var avgLbl = document.createElement('label');
    avgLbl.htmlFor = 'PatientDataExport-avg-' + index;
    avgLbl.className = 'aggMargin';
    avgLbl.textContent = 'Avg';
    var avgRadio = document.createElement('input');
    avgRadio.type = 'radio';
    avgRadio.name = 'aggregation-' + index;
    avgRadio.id = 'PatientDataExport-avg-' + index;
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
    
    return index;	
};

i2b2.PatientDataExport.assembleColumnConfig = function(index) {
    // the concept object is populated at drop-time, so we just focus on the other fields here
    var config = i2b2.PatientDataExport.model.configuration.columnConfigs[index];
    config.columnName = $('PatientDataExport-columnName-' + index).value;
    config.displayFormat = $('PatientDataExport-dispfmt-select-' + index).value;
    switch (config.displayFormat) {
    case 'value':
		config.howMany = parseInt($('PatientDataExport-howmany-' + index).value);
		config.includeTimeRange = $('PatientDataExport-timerange-' + index).checked;
		config.includeUnits = $('PatientDataExport-units-' + index).checked;
		break;
    case 'aggregation':
		var aggs = document.getElementsByName('aggregation-' + index);
		var checked = false;
		for (var i = 0; i < aggs.length; i++) {
			if (aggs[i].checked) {
				config.aggregation = aggs[i].value;
				checked = true;
				break;
			}
		}
		if (!checked) {
			alert('Select an aggregation for concept: ' + config.conceptRecord.sdxInfo.sdxDisplayName);
			return false;
		}
		config.includeUnits = $('PatientDataExport-units-' + index).checked;
		break;
    case 'existence':
		break;
    default:
		alert('Select a display format for concept: ' + config.conceptRecord.sdxInfo.sdxDisplayName);
		return false;
    }

	return true;
};

i2b2.PatientDataExport.assembleConfig = function() {
    var table = $('PatientDataExport-configTable');
    for (var i = 1; i < table.rows.length; i++) {
	var index = table.rows[i].id.split('-')[2];
	if (i2b2.PatientDataExport.model.configuration.columnConfigs[index].conceptRecord) {
	    if (i2b2.PatientDataExport.assembleColumnConfig(index)) {
		    i2b2.PatientDataExport.model.configuration.columnConfigs[index].order = i;
		} else {
			return false;
		}
	}
    }
    i2b2.PatientDataExport.model.configuration.rowDimension = $('PatientDataExport-rowdim').value;
    i2b2.PatientDataExport.model.configuration.whitespace = $('PatientDataExport-whitespace').value;
    i2b2.PatientDataExport.model.configuration.delimiter = $('PatientDataExport-delimiter').value;
	i2b2.PatientDataExport.model.configuration.quote = $('PatientDataExport-quote').checked;
    i2b2.PatientDataExport.model.configuration.missing = $('PatientDataExport-missing').value;

	return true;
};

/*
 * This function has been deprecated by i2b2.PatientDataExport.generatePreview
 * below, which retrieves the preview as a service call.
 */
i2b2.PatientDataExport.generatePreviewLocal = function() {
    if (i2b2.PatientDataExport.assembleConfig()) {
    	var previewStr = '';
    	switch (i2b2.PatientDataExport.model.configuration.rowDimension) {
    	case 'patient':
			previewStr += 'Patient_id';
			break;
	    case 'visit':
			previewStr += 'Patient_id'
			previewStr += 'Visit_id';
			previewStr += 'Visit_start';
			previewStr += 'Visit_end';
			break;
		case 'provider':
			previewStr += 'Provider_name';
			break;
		}
		i2b2.PatientDataExport.model.configuration.columnConfigs.forEach(function(config) {
			if (config.conceptRecord) {
				var colName = config.columnName;
				if (i2b2.PatientDataExport.model.configuration.whitespace !== '') {
					colName = colName.replace(' ', i2b2.PatientDataExport.model.configuration.whitespace, 'g');
	    		}
				if (config.displayFormat === 'value') {
					var howMany = config.howMany;
					if (!howMany || howMany < 1) {
		    			howMany = 1;
					}
					for (var i = 0; i < howMany; i++) {
					    previewStr += i2b2.PatientDataExport.model.configuration.delimiter + colName + '_value';
		    
		    			if (config.includeUnits) {
							previewStr += i2b2.PatientDataExport.model.configuration.delimiter + colName + '_unit';
		    			}
					    if (config.includeTimeRange) {
							previewStr += i2b2.PatientDataExport.model.configuration.delimiter + colName + '_start' + i2b2.PatientDataExport.model.configuration.delimiter + colName + '_end';
					    }	
					}
	    		} else if (config.displayFormat === 'aggregation') {
					colName += '_' + config.aggregation;
					previewStr += i2b2.PatientDataExport.model.configuration.delimiter + colName;
					if (config.includeUnits) {
					    previewStr += i2b2.PatientDataExport.model.configuration.delimiter + colName + '_unit';
					}
			    } else {
					previewStr += i2b2.PatientDataExport.model.configuration.delimiter + colName;
	    		} 
			}	
    	});	
    	$('PatientDataExport-previewText').textContent = previewStr;
	}
};

i2b2.PatientDataExport.colConfigComp = function(a, b) { 
    return a.columnOrder - b.columnOrder; 
}

i2b2.PatientDataExport.createConfigObject = function() {
    if (i2b2.PatientDataExport.assembleConfig()) {
		var rawConfig = i2b2.PatientDataExport.model.configuration;
		var config = {};
		config.name = $('PatientDataExport-saveas').value;
		config.rowDimension = rawConfig.rowDimension.toUpperCase();
		config.whitespaceReplacement = rawConfig.whitespace;
		config.separator = rawConfig.delimiter;
		config.quoteChar = rawConfig.quote ? '"' : '';
		config.missingValue = rawConfig.missing;
		config.columnConfigs = [];
    
		rawConfig.columnConfigs.forEach(function(colConfigRaw) {
        	if (colConfigRaw.conceptRecord) {
            	var columnConfig = {};
				columnConfig.columnOrder = colConfigRaw.order;
				columnConfig.columnName = colConfigRaw.columnName;
				columnConfig.displayFormat = colConfigRaw.displayFormat.toUpperCase();
				if (colConfigRaw.displayFormat === 'value') {
					var howMany = colConfigRaw.howMany;
                	if (!howMany || howMany < 1) {
                    	howMany = 1;
                	}
                	columnConfig.howMany = howMany;
                	columnConfig.includeTimeRange = colConfigRaw.includeTimeRange;
                	columnConfig.includeUnits = colConfigRaw.includeUnits;
            	} else if (colConfigRaw.displayFormat === 'aggregation') {
                	columnConfig.includeUnits = colConfigRaw.includeUnits;
                	columnConfig.aggregation = colConfigRaw.aggregation.toUpperCase();
            	}                            
	    		var conceptData = colConfigRaw.conceptRecord.origData;                           
            	columnConfig.i2b2Concept = {
					i2b2Key: conceptData.key,
                	level: parseInt(conceptData.level),
                	tableName: conceptData.table_name,
					columnName: conceptData.column_name,
            	    dimensionCode: conceptData.dim_code,
                	isSynonym: 'N',
					hasChildren: conceptData.hasChildren,
					icd9: conceptData.icd9,
					name: conceptData.name,
					operator: conceptData.operator,
					displayName: colConfigRaw.conceptRecord.sdxInfo.sdxDisplayName,
					tooltip: conceptData.tooltip,
					xmlOrig: i2b2.h.Xml2String(conceptData.xmlOrig)
				};         
            
            	config.columnConfigs.push(columnConfig);        
        	}
    	});
    
		// ensure we save the column configs in the correct order
		config.columnConfigs.sort(i2b2.PatientDataExport.colConfigComp);
    
		return config;
	} else {
		return null;
	}
}

i2b2.PatientDataExport.createRequestObject = function() {
	var config = i2b2.PatientDataExport.createConfigObject();
	if (config) {
		var i2b2AuthMetadata = i2b2.PatientDataExport.createI2b2AuthRequestObject();

		var request = {};
		request.outputConfiguration = config;
		request.i2b2AuthMetadata = i2b2AuthMetadata;

		return request;
	} else {
		return null;
	}
}

i2b2.PatientDataExport.modalDialog = function(id, msg) {
	var modalPanel = $(id);
	if (modalPanel) {
		$('PatientDataExport-mainDiv').removeChild(modalPanel.parentNode);
	}
    modalPanel = new YAHOO.widget.Panel(id,
		{ width: "240px",
          fixedcenter: true,
          close: false,
          draggable: false,
          zindex: 4,
          modal: true,
          visible: false
        }
    );
	
	modalPanel.setHeader(msg);
	modalPanel.setBody('');
	modalPanel.body.addClassName('pleaseWait');
    modalPanel.render($('PatientDataExport-mainDiv'));
	
	return modalPanel;
}

i2b2.PatientDataExport.generatePreview = function() {
	var config = i2b2.PatientDataExport.createConfigObject();
	if (config) {
		var pleaseWait = i2b2.PatientDataExport.modalDialog('PatientDataExport-pleaseWaitPreview', 
							'Fetching preview, please wait...');
		pleaseWait.show();
		new Ajax.Request(i2b2.PatientDataExport.SERVICE_URL + '/export/preview', {
			method: 'POST',
        	contentType: 'application/json',
			postBody: Object.toJSON(config),
			requestHeaders: {'Accept': 'text/plain'},
			asynchronous: true,
			onSuccess: function(response) {
				$('PatientDataExport-previewText').textContent = response.responseText;
        	},
			onComplete: function(response) {
				pleaseWait.hide();
			}
    	});
	}
}

i2b2.PatientDataExport.saveConfig = function() {
	var config = i2b2.PatientDataExport.createRequestObject();
	if (config) {
		var saveAs = $('PatientDataExport-saveas').value;
		if (saveAs.length <= 0) {
			alert('Please name this configuration');
		} else {
			var pleaseWait = i2b2.PatientDataExport.modalDialog('PatientDataExport-pleaseWaitSave', 
								    'Saving, please wait...');
			pleaseWait.show();
			new Ajax.Request(i2b2.PatientDataExport.SERVICE_URL + '/config/save', {
	    		method: 'POST',
				contentType: 'application/json',
				postBody: Object.toJSON(config),
				requestHeaders: {'Accept': 'application/json'},
				asynchronous: true,
				onSuccess: function(response) {
					i2b2.PatientDataExport.populateConfigList();
	    		},
				onComplete: function(response) {
					pleaseWait.hide();
				}
			});
    	}
	}
};

i2b2.PatientDataExport.exportData = function() {
	var config = i2b2.PatientDataExport.createConfigObject();
    if (config) {
		var pleaseWaitPanel = i2b2.PatientDataExport.modalDialog('PatientDataExport-pleaseWaitExport', 
                                                             'Exporting, please wait...');
		var pleaseWaitListener = function(evt) {
			pleaseWaitPanel.hide();
			$('PatientDataExport-export').disabled = false;
			window.removeListener('beforeunload', pleaseWaitListener);
		}
		window.addEventListener('beforeunload', pleaseWaitListener);
        pleaseWaitPanel.show();
		$('PatientDataExport-export').disabled = true;

		var downloadForm = $('PatientDataExport-downloadForm');
        if (downloadForm) {
        	$('PatientDataExport-saveRunPanel').removeChild(downloadForm);
		}

		downloadForm = document.createElement('form');
		downloadForm.id = 'PatientDataExport-downloadForm';
		downloadForm.display = 'none';

                var i2b2Domain = document.createElement('input');
                i2b2Domain.type = 'hidden';
                i2b2Domain.name = 'i2b2-domain';
                i2b2Domain.value = i2b2.h.getDomain();
                downloadForm.appendChild(i2b2Domain);

                var i2b2User = document.createElement('input');
                i2b2User.type = 'hidden';
                i2b2User.name = 'i2b2-user';
                i2b2User.value = i2b2.h.getUser();
                downloadForm.appendChild(i2b2User);

                var i2b2Pass = document.createElement('input');
                i2b2Pass.type = 'hidden';
                i2b2Pass.name = 'i2b2-pass';
                i2b2Pass.value = i2b2.h.getPass();
                downloadForm.appendChild(i2b2Pass);

                var i2b2Project = document.createElement('input');
                i2b2Project.type = 'hidden';
                i2b2Project.name = 'i2b2-project';
                i2b2Project.value = i2b2.h.getProject();
                downloadForm.appendChild(i2b2Project);

				var configJson = document.createElement('input');
                configJson.type = 'hidden';
                configJson.name = 'config-json';
                configJson.value = Object.toJSON(config);
                downloadForm.appendChild(configJson);

                var patientSetCollId = document.createElement('input');
                patientSetCollId.type = 'hidden';
                patientSetCollId.name = 'patient-set-coll-id';
                patientSetCollId.value = parseInt(i2b2.PatientDataExport.model.prsRecord.origData.PRS_id);
                downloadForm.appendChild(patientSetCollId);

                var patientSetSize = document.createElement('input');
                patientSetSize.type = 'hidden';
                patientSetSize.name = 'patient-set-size';
                patientSetSize.value = parseInt(i2b2.PatientDataExport.model.prsRecord.origData.size);
                downloadForm.appendChild(patientSetSize);

                document.getElementById('PatientDataExport-saveRunPanel').appendChild(downloadForm);
                var f = $('PatientDataExport-downloadForm');
                f.action = i2b2.PatientDataExport.SERVICE_URL + '/export/configDetails';
                f.method = 'POST';
                f.submit();
	}
};

i2b2.PatientDataExport.loadConfig = function() {
    var selectedConfigId = parseInt($('PatientDataExport-userConfigsList').value);
    if (!selectedConfigId) {
		alert('Please select a configuration to load.');
    } else {
		var pleaseWait = i2b2.PatientDataExport.modalDialog('PatientDataExport-pleaseWaitLoad', 
															'Loading, please wait...');
		pleaseWait.show();
		new Ajax.Request(i2b2.PatientDataExport.SERVICE_URL + '/config/load', {
	    	method: 'POST',
		    contentType: 'application/json',
		    postBody: Object.toJSON({'authMetadata': i2b2.PatientDataExport.createI2b2AuthRequestObject(), 'outputConfigurationId': selectedConfigId}),
	    	requestHeaders: {'Accept': 'application/json'},
		    asynchronous: true,
			onComplete: function(response) {
				pleaseWait.hide();
			},
		    onSuccess: function(response) {
				var config = response.responseJSON;
				$('PatientDataExport-whitespace').value = config.whitespaceReplacement;
				$('PatientDataExport-delimiter').value = config.separator;
				$('PatientDataExport-quote').checked = config.quoteChar === '"';
				$('PatientDataExport-missing').value = config.missingValue;
				var rowDimOptions = $('PatientDataExport-rowdim').options;
				for (var i = 0; i < rowDimOptions.length; i++) {
				    var option = rowDimOptions.item(i);
				    if (option.value === config.rowDimension.toLowerCase()) {
						option.selected = true;
						break;
		    		}
				}

				// remove all existing table rows
				var table = $('PatientDataExport-configTable');
				var rowHolder = table.rows[0].parentNode;
				while (rowHolder.children.length > 1) {
				    // we want to keep the header row
				    rowHolder.removeChild(rowHolder.lastChild);
				}
		
				i2b2.PatientDataExport.model.configuration.columnConfigs = [];
				// ensure we are displaying the column configs in the correct order
				config.columnConfigs.sort(i2b2.PatientDataExport.colConfigComp);
				config.columnConfigs.forEach(function(colConfig) {
				    var index = i2b2.PatientDataExport.addColumnConfig();
		    
				    // repopulate the concept record for this column
				    var c = colConfig.i2b2Concept;
				    var crOrigData = {
						key: c.i2b2Key,
						level: c.level,
						dim_code: c.dimensionCode,
						table_name: c.tableName,
						column_name: c.columnName,
						hasChildren: c.hasChildren,
						icd9: c.icd9,
						name: c.name,
						operator: c.operator,
						tooltip: c.tooltip,
						xmlOrig: i2b2.h.parseXml(c.xmlOrig).getElementsByTagName('concept')[0]
				    };
				    var crSdxInfo = {
						sdxControlCell: 'ONT',
						sdxDisplayName: c.displayName,
						sdxKeyName: 'key',
						sdxKeyValue: c.i2b2Key,
						sdxType: 'CONCPT'
		    		};
				    var cr = { origData: crOrigData, sdxInfo: crSdxInfo, renderData: {}};
				    i2b2.PatientDataExport.model.configuration.columnConfigs[index].conceptRecord = cr;
				    // let the user know that the drop was successful by displaying the name of the concept
	    	        $('PatientDataExport-CONCPTDROP-' + index).innerHTML = i2b2.h.Escape(cr.sdxInfo.sdxDisplayName);
	        	    $('PatientDataExport-CONCPTDROP-' + index).className = 'droptrgt SDX-CONCPT';
		    
				    $('PatientDataExport-columnName-' + index).value = colConfig.columnName;
				    i2b2.PatientDataExport.populateDispFmtSelect(index, cr);
				    var dispFmtOptions = $('PatientDataExport-dispfmt-select-' + index).options;
				    for (var i = 0; i < dispFmtOptions.length; i++) {
						var option = dispFmtOptions.item(i);
						if (option.value === colConfig.displayFormat.toLowerCase()) {
						    option.selected = true;
						    break;
						}
		    		}
				    i2b2.PatientDataExport.showHideDispFmt(index, colConfig.displayFormat.toLowerCase());
				    if (colConfig.displayFormat === 'VALUE') {
						$('PatientDataExport-howmany-' + index).value = colConfig.howMany;
						$('PatientDataExport-timerange-' + index).checked = colConfig.includeTimeRange;
						$('PatientDataExport-units-' + index).checked = colConfig.includeUnits;
		    		} else if (colConfig.displayFormat === 'AGGREGATION') {
						$('PatientDataExport-' + colConfig.aggregation.toLowerCase() + '-' + index).checked = true;	
						$('PatientDataExport-units-' + index).checked = colConfig.includeUnits;
		    		}
				});
	    	}
		}); // end AJAX request
    }
};

i2b2.PatientDataExport.deleteConfig = function() {
    var selectedConfigId = parseInt($('PatientDataExport-userConfigsList').value);
    if (!selectedConfigId) {
		alert('Please select a configuration to delete.');
    } else {
		var configsList = $('PatientDataExport-userConfigsList');
		var configName = '';
		for (var i = 0; i < configsList.options.length; i++) {
	    	if (configsList.options[i].selected) {
				configName = configsList.options[i].text;
				break;
		    }
		}
		if (confirm("Are you sure want to delete this configuration: '" + configName + "'?")) {
			var pleaseWait = i2b2.PatientDataExport.modalDialog('PatientDataExport-pleaseWaitDelete', 
																'Deleting, please wait...');
			pleaseWait.show();
	    	new Ajax.Request(i2b2.PatientDataExport.SERVICE_URL + '/config/delete', {
				method: 'POST',
				contentType: 'application/json',
				postBody: Object.toJSON({'authMetadata': i2b2.PatientDataExport.createI2b2AuthRequestObject(), 'outputConfigurationId': selectedConfigId}),
				requestHeaders: {'Accept': 'application/json'},
				asynchronous: true,
				onSuccess: function(response) {
				    i2b2.PatientDataExport.populateConfigList();
				},
				onComplete: function(response) {
					pleaseWait.hide();
				}
	    	});
		}
    }
};
