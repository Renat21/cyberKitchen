let currentLocation = document.location.protocol + "//" + document.location.host;
let tableTasks = document.querySelector("#tableTasks")


var fixHelperModified = function(e, tr) {
        var $originals = tr.children();
        var $helper = tr.clone();
        $helper.children().each(function(index) {$(this).width($originals.eq(index).width())});
        return $helper;},
updateIndex = function(e, ui) {
    $('td.index', ui.item.parent()).each(function (i) {
        $(this).html(i+1);
    });
};

$("#myTable tbody").sortable({
    helper: fixHelperModified,
    stop: updateIndex
}).disableSelection();

$("tbody").sortable({
    distance: 5,
    delay: 100,
    opacity: 0.6,
    cursor: 'move',
    update: function() {}
});

document.querySelector("#defineNumeration").addEventListener('click' , function (){
    defineNumeration()
}, true)


function defineNumeration(){
    let numeration = {}
    for (let i = 0; i < tableTasks.children.length; i++) {
        numeration[tableTasks.children[i].id] = i
    }

    createAjaxQueryWithData("/event/organizer/defineNumeration", redirect, numeration)
}

function redirect(data) {
    // if (data) {
    //     window.location.href = currentLocation + data;
    // } else {
    //     // data.form contains the HTML for the replacement form
    //     // $("#myform").replaceWith(data.form);
    // }
}

function createAjaxQueryWithData(url, toFunction, request) {
    console.log(request)
    jQuery.ajax({
        type: 'POST',
        url: currentLocation + url,
        contentType: 'application/json',
        data: JSON.stringify(request),
        success: toFunction
    });
}

$(function () {
    $('.tasks').on('click', function (event) {
        label = event.currentTarget.querySelectorAll(".value")
        document.querySelector("#nameChange").value = label[1].innerText
        document.querySelector("#descriptionChange").value = label[2].innerText
        document.querySelector("#maxScoreChange").value = label[3].innerText
        document.querySelector("#changeForm").action = "/event/organizer/changeTask/" + event.currentTarget.id
        console.log(document.querySelector("#changeForm").action)
    });
});
