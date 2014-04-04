(function($){
  /* global AdminSocketManager */
  "use strict";
  var $endBtn = $("#endBtn"),
      $drawingContainer = $(".drawing-container"),
      $startBtn = $("#startBtn"),
      socketManager = AdminSocketManager.socketManager;

  $endBtn.on("click", function() {
    var msg = {};

    $endBtn.toggleClass("disabled");
    msg.drawingId = $drawingContainer.data("drawingId");

    socketManager.send("/ws/admin/drawing/end", {}, JSON.stringify(msg));
  });

  $startBtn.on("click", function() {
    var data = {};

    $startBtn.toggleClass("disabled");
    $endBtn.toggleClass("disabled");
    $(".add-item-btn").addClass("disabled");

    // Deal with the item draw buttons
    $(".prize-pool-tier-row").each(function() {
      var $this = $(this);
      if ($this.data("entrants") === 0 ||
          $this.find("i").length === 0) // Each valid prize item will have an info node
      {
        $(".draw-btn", $this).remove();
      }
    });
    $(".draw-btn").removeClass("invisible");

    // Deal with the money draw buttons
    $(".deposit-btn").remove();
    $(".money-draw-btn").each(function() {
      var $this = $(this);

      if (parseInt($this.siblings(".pool-pot").text(), 10) === 0) {
        $this.remove();
      } else {
        $this.removeClass("hidden");
      }
    });

    data.drawingId = $drawingContainer.data("drawingId");
    socketManager.send("/ws/admin/drawing/start", {}, JSON.stringify(data));
  });

  $(".draw-btn").on("click", function() {
    var $this = $(this),
        data = {};

    $this.toggleClass("disabled").text("Drawn");

    data.tierId = $this.closest(".prize-pool-tier-row").data("tierId");
    data.poolId = $this.closest(".prize-pool").data("poolId");
    socketManager.send("/ws/admin/drawing/draw/tier", {}, JSON.stringify(data));

    console.dir(data);
  });

  $(".money-draw-btn").on("click", function() {
    var $this = $(this),
        $pool = $($this.closest(".prize-pool")),
        msg = {};

    msg.drawingId = $drawingContainer.data("drawingId");
    msg.poolName = ($pool.hasClass("small-pool")) ? "small" : "large";

    $this.toggleClass("disabled").text("Drawing");
    socketManager.send("/ws/admin/drawing/draw/pool/money", {}, JSON.stringify(msg));
  });
}(jQuery));