$(function() {
  /* global BootstrapDialog */
  "use strict";
  var $duplicateBtns = $(".duplicate-btn"),
      confirmBtn = {},
      dialog = {},
      duplicateUrl = $("#duplicateUrl").data("url");

  dialog = new BootstrapDialog({
    buttons: [{
      id: "duplicateConfirm",
      label: "Duplicate"
    }]
  });

  dialog.setTitle("Duplicate Drawing");
  dialog.setType(BootstrapDialog.TYPE_DEFAULT);
  dialog.setClosable(true);
  dialog.setAutodestroy(false);
  dialog.setMessage([
    "<label for='duplicateDate'>New drawing date (server time)</label>",
    "<div class='input-group date'>",
    "<input class='form-control' id='duplicateDate' readonly>",
    "<span class='input-group-addon'><i class='glyphicon glyphicon-calendar'></i></span> ",
    "</div>"
  ].join(''));

  dialog.realize();

  confirmBtn = dialog.getButton("duplicateConfirm");
  confirmBtn.attr("data-loading-text", "Duplicating...");
  confirmBtn.click(function() {
    var dt = $("#duplicateDate", dialog.getModalBody()).val();

    dialog.setClosable(false);
    confirmBtn.button("loading");

    $.ajax({
      url: duplicateUrl,
      data: JSON.stringify({drawingId: dialog.getData("drawingId"), startDateTime: dt}),
      type: "POST",
      contentType: "application/json",
      success: function(data, status, jqXHR){
        window.location.href = window.location;
      }
    });
  });

  $duplicateBtns.on("click", function() {
    var $this = $(this),
        drawingId = $this.closest("tr").data("drawingId");

    $("#duplicateDate", dialog.getModalBody()).datetimepicker({
      autoclose: true,
      format: "yyyy-mm-dd @ hh:ii -0800"
    });

    dialog.setData("drawingId", drawingId);
    dialog.open();
  });
});