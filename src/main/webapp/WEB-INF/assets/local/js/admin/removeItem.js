(function($) {
  /* global AdminSocketManager */
  "use strict";
  var $drawingContainer = $(".drawing-container"),
      $addItemTemplate = $($("#addItemBtnTemplate").html().trim()),
      socketManager = AdminSocketManager.socketManager;

  $drawingContainer.on("click", ".remove-item-btn", function() {
    var $this = $(this),
        $td = $this.closest("td"),
        msg = {},
        subscription = {};

    msg.itemId = $td.find("i[data-id]").data("id");
    msg.tierId = $this.closest("tr").data("tierId");
    msg.poolId = $this.closest("div.prize-pool").data("poolId");
    msg.drawingId = $drawingContainer.data("drawingId");
    msg.tierPosition = $td.data("position");

    subscription = socketManager.subscribe("/topic/admin/drawing/item/del", function(response) {
      var result = JSON.parse(response.body);
      subscription.unsubscribe();

      if (result.successful) {
        $this.closest("td").html($addItemTemplate.clone());
      } else {
        alert(result.message);
      }
    });

    socketManager.send("/ws/admin/drawing/item/del", {}, JSON.stringify(msg));
  });
}(jQuery));