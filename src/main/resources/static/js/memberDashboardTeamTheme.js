let currentLocation = document.location.protocol + "//" + document.location.host;
$(function () {
    $('.currentTheme').on('click', function (event) {
        allThemes = document.querySelectorAll(".currentTheme")
        for (let i = 0; i < allThemes.length; i++) {
            allThemes[i].classList.remove("list_active")
        }
        label = event.currentTarget
        label.classList.add("list_active")


        createAjaxQuery("/event/member/getTheme/" + label.id, changeDescription)
    });
});

function changeDescription(data){

    document.querySelector("#name").innerText= data.name
    document.querySelector("#description").innerText= data.description
    document.querySelector("#themeForm").action = "/event/member/selectTheme/" + data.id;
}


function createAjaxQuery(url, toFunction) {
    console.log(currentLocation + url);
    jQuery.ajax({
        type: 'POST',
        url: currentLocation + url,
        contentType: 'application/json',
        success: toFunction
    });
}
