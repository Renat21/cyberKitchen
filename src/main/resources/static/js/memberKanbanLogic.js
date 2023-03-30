let currentLocation = document.location.protocol + "//" + document.location.host;
let currentSolution = 0
let solutionsKanban = document.querySelector("#solutionsKanban")
let messagesModalWindow = document.querySelector("#messagesModalWindow")
document.querySelector("#sendMessageButton").addEventListener('click', function (){
    sendMessage()
})


// SENDING MESSAGE WITH DOCS

async function createDocument(documents) {
    var request = {};
    let elem;
    request.documents = documents; // some data

    let formData = new FormData();
    for (let i = 0; i < documents.length; i++) {
        formData.append("file", documents[i]);
    }
    const response = await fetch(currentLocation + "/document/create", {
        method: "POST",
        body: formData,
    }).then((data) => {
        elem = data.json();
    })
    return elem
}



async function sendMessage() {

    var documentValue
    let documents = document.querySelector("#documentList").files
    console.log(documents)
    if (documents.length !== 0) {
        let documentConverted = createDocument(documents)
        await documentConverted.then(async function (value) {
            documentValue = value
        })
    }

    let text = document.querySelector("#feedback").value
    const message = {
        id: currentSolution,
        data: text,
        documents: documentValue != null ? documentValue : null
    }


    createAjaxQueryWithData("/event/member/sendMessage", addMessageToListAfterMessage, message)

    document.querySelector("#documentList").value = ''
    document.querySelector("#feedback").innerHTML = ""
}




function addMessageToListAfterMessage(message){
    addMessageToList(message)
    document.querySelector("#task-status").innerHTML = message["solution"]["state"]
    refreshKanban()
}


function addMessageToList(message){
    let mess = document.createElement("div")
    mess.classList.add("comment", "border", "rounded", "p-2", "mb-3")
    mess.innerHTML = "   <header class=\"comment-header border-bottom pb-2 mb-2\">\n" +
        "                  <h6 class=\"mb-0\">" + message["user"]["username"] + "</h6>\n" +
        "                  <i>Разработчик</i>\n" +
        "                </header>\n" +
        "\n" +
        "                <div class=\"comment-body\">\n"
                             + message["data"] +
        "                   <div class=\"attachment-mail\">\n" +
        "                        <p>\n" +
        "                            <a>All attachments</a>\n" +
        "                        </p>\n" +
        "                        <div id=\"attachmentsDocuments" + message["id"] + "\" class=\"attachments\">\n" +
        "                        </div>\n" +
        "                    </div>" +
        "               </div>  "


    messagesModalWindow.appendChild(mess)

    let attachmentsDocuments = messagesModalWindow.querySelector("#attachmentsDocuments" + message["id"])

    let extension;
    let fileName;
    let div;
    for (let i = 0; i < message["documents"].length; i++) {
        let source = message["documents"][i] != null ? '/image/' + message["documents"][i]["id"] : 'https://bootdey.com/img/Content/avatar/avatar1.png';
        extension = message["documents"][i]["originalFileName"].split(".")[1]
        fileName = message["documents"][i]["originalFileName"].length <= 13 ? message["documents"][i]["originalFileName"] : message["documents"][i]["originalFileName"].slice(0, 13) + ".."


        div = document.createElement('div')
        div.classList.add("attachment")

        div.innerHTML = "<span class=\"badge\">" + extension + "</span> <a href=\"" + source + "\" download=\"" + message["documents"][i]["originalFileName"] + "\">" + fileName + "</a> <i>(" + (message["documents"][i]["size"] / 1024 / 1024).toFixed(2) + "MB)</i>"
        let attachFile = div.querySelector(".badge")

        if (extension === "pdf" || extension === "docx" || extension === "txt")
            attachFile.style.backgroundColor = "#17a2b8"
        else if (extension === "xls")
            attachFile.style.backgroundColor = "#28a745"
        else
            attachFile.style.backgroundColor = "#dc3545"
        attachmentsDocuments.appendChild(div)
    }

    messagesModalWindow.scrollTop = messagesModalWindow.scrollHeight;

}

function updateModalWindow(data){
    document.querySelector("#modal-title").innerHTML = data["solution"]["task"]["name"]
    document.querySelector("#modal-description").innerHTML = data["solution"]["task"]["description"]
    document.querySelector("#curScore").innerHTML =  data["solution"]["curScore"]

    for (let i = 0; i < data["messages"].length; i++) {
        addMessageToList(data["messages"][i])
    }


    document.querySelector("#task-status").innerHTML = data["solution"]["state"]
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


function addKanbanItem(solution, numeration){
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
                "            <div class=\"card-body task-title\">Task " + numeration + "    Score: " + solution.curScore + "</div>\n" +
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
        refreshKanban()
    }
)

function refreshKanban(){
    solutionsKanban.innerHTML = "<tr>\n" +
        "        <td></td>\n" +
        "        <td></td>\n" +
        "        <td></td>\n" +
        "        <td></td>\n" +
        "      </tr>"
    createAjaxQuery("/event/member/getKanbanBoard", setRefreshedKanban)
}


function setRefreshedKanban(data){
    for (let i = 0; i < data.length ; i++) {
        addKanbanItem(data[i], i + 1)
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