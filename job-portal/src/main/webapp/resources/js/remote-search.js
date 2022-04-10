function getBloodHoundObject(url) {
    return new Bloodhound({
        datumTokenizer : Bloodhound.tokenizers.whitespace,
        queryTokenizer : Bloodhound.tokenizers.whitespace,
        remote : {
            url : url + '%QUERY',
            wildcard : '%QUERY'
        },
        limit : 20
    });

}

function getPreFetchBloodHoundObject(url) {
    return new Bloodhound({
        datumTokenizer : Bloodhound.tokenizers.nonword,
        queryTokenizer : Bloodhound.tokenizers.nonword,
        // url points to a json file that contains an
                                // array of country names, see
        // https://github.com/twitter/typeahead.js/blob/gh-pages/data/countries.json
        prefetch : url
    });

}
$(function() {
    initializeGoogleApi();
});