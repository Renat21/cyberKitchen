// замена данных для модального окна
$(".dashboard-task").on("click", function () {
  const taskTitle = $(this).find(".task-title").text();

  $("#modal-title").text(taskTitle);
  $("#task-data").modal("show");
});

$("#feedback").trumbowyg({
  btns: [
    ['viewHTML'],
    ['strong', 'em', 'del'],
    ['justifyLeft', 'justifyCenter', 'justifyRight', 'justifyFull'],
    ['unorderedList', 'orderedList'],
    ['horizontalRule'],
    ['fullscreen']
  ],
});
