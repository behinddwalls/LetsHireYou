// ============COMMON JS START=============
// supplly the entire div that needs to be removed here
function clearHtmlDom(htmlObj, jqueryObject) {
	$(htmlObj).detach();
}

// check only String
function checkEmpty(obj) {
	if (obj == null || obj == "") {
		return true;
	}
	return false;
}
function isProfileComplete(contextPath) {
	$.ajax({
		url : contextPath + "/jobseeker/profile/complete/",
	}).done(function(data, status, xhr) {
		console.log(data);
		if (data != null && data == true) {
			$('.profileComplete').removeClass('hidden');
			$('.profileInComplete').addClass('hidden');
		} else {
			$('.profileComplete').addClass('hidden');
			$('.profileInComplete').removeClass('hidden');
		}
	}).fail(function(xhr, status, errorThrown) {
		console.log('error');
	}).always(function(xhr) {
		stopLoader();
	});
}
// call this for dynamically added input
function genericTypeahead(selector, classGroupName, bloodHoundObjForSearch) {
	$(selector).typeahead({
		highlight : true,
		hint : true,
		minLength : 1
	}, {
		name : classGroupName,
		source : bloodHoundObjForSearch
	});
}

function getLocalizedDateString(dateString) {
	var monthNames = [ "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" ];
	var date = new Date(dateString);
	return monthNames[date.getMonth()] + " " + date.getFullYear();
}

// ============COMMON JS END=============

