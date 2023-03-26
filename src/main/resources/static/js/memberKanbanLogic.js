let currentLocation = document.location.protocol + "//" + document.location.host;

let solutionsKanban = document.querySelector("#solutionsKanban")



function changeModalWindow(data){
    console.log(data)
}


function getColumnIdByStatus(state){
    switch (state){
        case "NOT_STARTED":
            return 0
        case "IN_PROGRESS":
            return 1
        case "REVIEW":
            return 2
        case "CLOSED":
            return 3
    }
}

function addKanbanItem(solution){
    let last = false
    for (let i = 0; i < solutionsKanban.children.length; i++) {
        if (i + 1 === solutionsKanban.children.length) {
            tr = document.createElement('tr')
            tr.innerHTML = "<td></td><td></td><td></td><td></td>"
            solutionsKanban.appendChild(tr)
            i = i + 1
            last = true
        }

        solutionsKanban.children[i + 1].children[getColumnIdByStatus(solution.state)].innerHTML = "<div id='" + solution.id + "' class=\"card border border-primary dashboard-task\" data-bs-toggle=\"modal\" data-bs-target=\"#task-data\">\n" +
            "            <div class=\"card-body task-title\">Task " + solution.task.numeration + "</div>\n" +
            "          </div>"
        document.querySelector("#solution" + solution.id).addEventListener("click", function (){changeModalWindow(solution.id)})
        if (last)
            break;
    }

}


$(
    function (){
        refreshKanban()
    }
)


function setRefreshedKanban(data){
    for (let i = 0; i < data.length ; i++) {
        addKanbanItem(data[i])
    }
    for (let i = 0; i < data.length ; i++) {
        addKanbanItem(data[i])
    }
}

function refreshKanban(){
    createAjaxQuery("/event/member/getKanbanBoard", setRefreshedKanban)
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


function createAjaxQuery(url, toFunction) {
    console.log(currentLocation + url);
    jQuery.ajax({
        type: 'GET',
        url: currentLocation + url,
        contentType: 'application/json',
        success: toFunction
    });
}