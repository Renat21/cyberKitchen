let currentLocation = document.location.protocol + "//" + document.location.host;
let teamsNode = document.getElementById('teams')
let currentRole

document.querySelector("#searchTeam").addEventListener('click' , function (){
    defineTeamsByRole()
}, true)

function addTeamToTeamNode(team){
    div = document.createElement('div')
    div.classList.add("card", "d-flex")
    div.style.width = "300px"
    div.style.marginTop = "20px"
    div.style.marginLeft = "20px"

    div.innerHTML =
        "                <div class=\"card-body\">\n" +
        "                    <h5 class=\"card-title\">" + team["name"] + "</h5>\n" +
        "                </div>\n" +
        "                <ul class=\"list-group list-group-flush\">\n" +
        "                </ul>"

    let members = div.querySelector(".list-group")
    for (let i = 0; i < team.members.length; i++) {
        li = document.createElement('li')
        li.classList.add("list-group-item")
        li.innerText = team.members[i].user.name + " " + team.members[i].role
        members.appendChild(li)
    }

    for (let i = 0; i < maxMembersInTeam - team.members.length; i++) {
        li = document.createElement('li')
        li.classList.add("list-group-item")
        li.innerHTML = "<button onclick='enterToTeam(" + team.id + ")' class=\"card-link\">Вступить</button>"
        members.appendChild(li)
    }
    return div;
}


function successDefineTeamsHandler(data){

    let k = teamsNode.children.length
    for (let i = 1; i < k; i++) {
        teamsNode.removeChild(teamsNode.children[1])
    }


    for (let i = 0; i < data.length; i++) {
        teamsNode.appendChild(addTeamToTeamNode(data[i]))
    }
}


function defineTeamsByRole(){
    let role = document.getElementById('yourRole').value;
    currentRole = role
    createAjaxQueryWithData("/event/member/getTeams", successDefineTeamsHandler, {role: role})
}


function enterToTeam(data){
    let role = document.getElementById('yourRole').value;
    currentRole = role
    createAjaxQueryWithData("/event/member/enterTeam/" + data, null, {role: role})
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
        type: 'POST',
        url: currentLocation + url,
        contentType: 'application/json',
        success: toFunction
    });
}