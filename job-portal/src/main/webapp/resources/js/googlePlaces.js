// window.onload = loadGoogleApiScript;
//var autocomplete, placesSearch;

function initializeGoogleApi() {
    // console.log("initialize google api called")
    initializeGoogleAutoFillPlaces()

}
var googlePlacesOptions = {

    componentRestrictions : {
        country : 'in'
    }
}

// this is statically adding google location to all elements of class
// autocompleteAddress
function initializeGoogleAutoFillPlaces() {
    // console.log("initialize google places called")
    // $(document).delegate('.autocompleteAddress','focus',function(){
    // callGetAutoCompleteObjectForHtmlObject($(this)[0]);
    //		
    // });
    var acInputsList = document.getElementsByClassName('autocompleteAddress');
    console.log(acInputsList.length)

    for (var i = 0; i < acInputsList.length; i++) {
        callGetAutoCompleteObjectForHtmlObject(acInputsList[i])
    }
}

function callGetAutoCompleteObjectForHtmlObject(htmlObject) {
    // console.log("zzzzzzzz callGetAutoCompleteObj");
    var autoComplete = getAutoCompleteObject(htmlObject);
    addGoogleEventHandlerToObject(autoComplete, 'place_changed', function() {
        fillInAddress(autoComplete, htmlObject.id)
    });

}

function getAutoCompleteObject(htmlObject) {
    // console.log("zzzzz getAutoCObj")
    return new google.maps.places.Autocomplete(htmlObject, googlePlacesOptions);
}

function addGoogleEventHandlerToObject(autoCompleteObject, event, callBackFunc) {
    google.maps.event.addListener(autoCompleteObject, event, callBackFunc)
}

// the components should represent form ids to be filled, currently we are not
// using it
var componentForm = {
    street_number : 'short_name',
    route : 'long_name',
    locality : 'long_name',
    administrative_area_level_1 : 'short_name',
    country : 'long_name',
    postal_code : 'short_name'
};

// Currently not using fillInAddress, it is supposed to be a callback function
function fillInAddress(autoCompleteObject, elementId) {
    // console.log("fillInAddress called "+elementId)

    var place = autoCompleteObject.getPlace();
    console.log(place);

}