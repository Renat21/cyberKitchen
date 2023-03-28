let currentLocation = document.location.protocol + "//" + document.location.host;
let currentSolution = 0
let teams
let solutionsKanban = document.querySelector("#solutionsKanban")
let messagesModalWindow = document.querySelector("#messagesModalWindow")
let teamNames = document.querySelector("#teamNames")
let task_status = document.querySelector("#task_status")
let task_score = document.querySelector("#scoreSolution")

document.querySelector("#sendMessageButton").addEventListener('click', function (){
    sendMessageAjax()
})

document.querySelector("#changeTeam").addEventListener('click', function (){
    refreshTeamsKanban(teamNames.value)
})

document.querySelector("#saveStatus").addEventListener('click', function (){
    let value = task_status.value
    let score = task_score.value
    createAjaxQueryWithData("/event/expert/setStatus/" + currentSolution, refreshAfterState, {state: value, curScore: score})
})

function refreshAfterState(){
    refreshTeamsKanban(teamNames.value)
}


function sendMessageAjax(){
    let text = document.querySelector("#feedback").value
    document.querySelector("#feedback").innerHTML = ""
    createAjaxQueryWithData("/event/expert/sendMessage", addMessageToList,
        {data: text, id: currentSolution})

    refreshTeamsKanban(teamNames.value)
}


function addMessageToList(message){
    let div = document.createElement("div")
    div.classList.add("comment", "border", "rounded", "p-2", "mb-3")
    div.innerHTML = "   <header class=\"comment-header border-bottom pb-2 mb-2\">\n" +
        "                  <h6 class=\"mb-0\">" + message["user"]["username"] + "</h6>\n" +
        "                  <i>Разработчик</i>\n" +
        "                </header>\n" +
        "\n" +
        "                <div class=\"comment-body\">\n"
        + message["data"] +
        "                </div>"
    messagesModalWindow.appendChild(div)


    messagesModalWindow.scrollTop = messagesModalWindow.scrollHeight;
}

function updateModalWindow(data){
    console.log(data)
    document.querySelector("#modal-title").innerHTML = data["solution"]["task"]["name"]
    document.querySelector("#modal-description").innerHTML = data["solution"]["task"]["description"]

    for (let i = 0; i < data["messages"].length; i++) {
        addMessageToList(data["messages"][i])
    }

    currentSolution = data["solution"]["id"]

}

function getSolutionById(id){
    createAjaxQuery("/event/member/getSolution/" + id, updateModalWindow)
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


function getColorByStatus(state){
    switch (state){
        case "NOT_STARTED":
            return "border-primary"
        case "IN_PROGRESS":
            return "border-success"
        case "REVIEW":
            return "border-warning"
        case "CLOSED":
            return "border-info"
    }
}


function addKanbanItem(solution){
    let last = false
    for (let i = 0; i < solutionsKanban.children.length; i++) {
        if (solutionsKanban.children[i].children[getColumnIdByStatus(solution.state)].innerHTML === "") {
            last = true
        }else if (i + 1 === solutionsKanban.children.length) {
            tr = document.createElement('tr')
            tr.innerHTML = "<td></td><td></td><td></td><td></td>"
            solutionsKanban.appendChild(tr)
            i = i + 1
            last = true
        }

        if (last) {
            solutionsKanban.children[i].children[getColumnIdByStatus(solution.state)].innerHTML = "<div id='solution" + solution.id + "' class=\"card border " + getColorByStatus(solution.state) + " dashboard-task\" data-bs-toggle=\"modal\" data-bs-target=\"#task-data\">\n" +
                "            <div class=\"card-body task-title\">Task " + solution.task.numeration + "    Score: " + solution.curScore + "</div>\n" +
                "          </div>"
            document.querySelector("#solution" + solution.id).addEventListener("click", function () {
                getSolutionById(solution.id)
            })
            break;
        }
    }
}


$(
    function (){
        getAllTeams()
    }
)

function setTeams(data){
    teams = data
    if (data.length > 0) {
        refreshTeamsKanban(data[0]["id"])
    }
}


function getAllTeams(){
    createAjaxQuery("/event/expert/getTeams", setTeams)
}

function refreshTeamsKanban(id){
    solutionsKanban.innerHTML = "<tr>\n" +
        "        <td></td>\n" +
        "        <td></td>\n" +
        "        <td></td>\n" +
        "        <td></td>\n" +
        "      </tr>"
    createAjaxQuery("/event/expert/getTeamKanban/" + id, setRefreshedKanban)
}


function setRefreshedKanban(data){
    for (let i = 0; i < data.length ; i++) {
        addKanbanItem(data[i])
    }
}


function createAjaxQueryWithData(url, toFunction, request) {
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