$(function() {
  /* global BootstrapDialog */
  "use strict";
  var $addCharacterBtn = $("#addCharacterBtn"),
      $addCharacterScript = $("#addCharacterScript"),
      $charactersList = $("#charactersList"),
      $input = {},
      accountUrl = $addCharacterScript.data("accountUrl"),
      confirmBtn = {},
      dialog = {};

  dialog = new BootstrapDialog({
    buttons: [{
      id: "addConfirm",
      label: "Add"
    }]
  });

  dialog.setTitle("Add Character");
  dialog.setType(BootstrapDialog.TYPE_DEFAULT);
  dialog.setClosable(true);
  dialog.setAutodestroy(false);
  dialog.setMessage("<input class='form-control' placeholder='Character Name'> ");

  dialog.realize();

  $input = $("input", dialog.getModalBody());
  confirmBtn = dialog.getButton("addConfirm");
  confirmBtn.attr("data-loading-text", "Adding...");
  confirmBtn.click(function(){
    var name = $input.val();

    dialog.setClosable(false);
    confirmBtn.button("loading");

    $.ajax({
      url: accountUrl + "/add/character",
      data: name,
      type: "PUT",
      contentType: "application/json",
      dataType: "json",
      success: function(data, status, jqXHR) {
        if (data.id === null || data.name === null) {
          return;
        }

        $charactersList.append([
          "<li data-character-id='",
          data.id,
          "'>",
          data.name,
          "</li>"
        ].join(''));
      },
      complete: function(jqXHR, status) {
        dialog.setClosable(true);
        confirmBtn.button("reset");
        dialog.close();
        $input.val('');
      }
    });
  });

  $addCharacterBtn.on("click", function() {
    dialog.open();
  });
});