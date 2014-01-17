var selectedElement;
       jQuery(this).flushCache();
function getListValue() {
    selectedElement = document.forms[0].con_country.value;
    
    searchArray();
}
function chkCountry(country) {
    getListValue();
    selectedElement = country;
    //alert(selectedElement);
    searchArray();
}

function ajaxDisplay(display) {
    
    $.ajax({
        type: "POST",
        url: "../ajaxReturn.emma",
        data: "display=" + display,
        cache: false,
        success: function(msg){
             //alert( "Data Saved: " + msg );
            $("#taOptions").html(msg);
        }
    });
}

var euCountriesList = ['Austria','Belgium','Bulgaria','Cyprus','Czech Republic','Denmark','Estonia','Finland','France','Germany','Greece','Hungary','Ireland','Italy','Latvia','Lithuania','Luxembourg','Malta','Netherlands','Poland','Portugal','Romania','Slovakia','Slovenia','Spain','Sweden','United Kingdom'];
var assocCountriesList = ['Albania','Croatia','Iceland','Israel','Liechtenstein','Macedonia','Montenegro','Norway','Serbia','Switzerland','Turkey'];


function searchArray(){
    //SEARCH OVER ALL ELEMENTS
    var id;
    var index;// = euCountriesList.indexOf(selectedElement)
    
    for (id in euCountriesList) {
        
        if (euCountriesList[id] == selectedElement) {
            index = 1;
            break;
        } else {
            index=-1;
        }
    }
   // alert("index values is :: " + index);
    if (index == -1) {
        //index = assocCountriesList.indexOf(selectedElement)
        var id;
        for (id in assocCountriesList) {
            if (assocCountriesList[id] == selectedElement) {
                index = 1;
                break;
            } else {
                index=-1;
            }
        }
    }
    if (index != -1)
    {
        //allow display
        // show('taOptions');
        //$( "#vat" ).show();
        //show('vat');
        //alert("one::-" + document.forms[0].eligible_country.value);
        document.forms[0].eligible_country.value="yes";
        //alert("two::-" + document.forms[0].eligible_country.value);
        //call ajax function
       // ajaxDisplay("enabled");
        
    } else if (index == -1) {
        //    hide('taOptions');
        $( "#vat" ).hide();
        //hide('vat');
       // hide('taProjDesc');
        document.forms[0].eligible_country.value="no";
        
        //call ajax function
       // ajaxDisplay("disabled");
    }
}


