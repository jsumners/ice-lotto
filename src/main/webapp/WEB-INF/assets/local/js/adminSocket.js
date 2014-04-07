var AdminSocketManager = (function() {
  /* global SocketManager */
  "use strict";
  var obj = {},
      endpoint = $("meta[name=app-path]").attr("content") + "admin";

  obj.socketManager = new SocketManager(endpoint);
  obj.socketManager.connect();

  obj.reconnect = function() {
    obj.socketManager.connect();
  };

  return obj;
}());
