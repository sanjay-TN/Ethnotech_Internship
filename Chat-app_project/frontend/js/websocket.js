let stompClient = null;

function connectWebSocket() {
  const socket = new SockJS("http://localhost:8080/chat");
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function () {
    console.log("WebSocket Connected");

    // Messages
    stompClient.subscribe("/topic/messages", function (msg) {
      const message = JSON.parse(msg.body);
      displayMessage(message);
    });

    // Typing
    stompClient.subscribe("/topic/typing", function (msg) {
      const data = JSON.parse(msg.body);
      showTyping(data);
    });

    // 🔥 Online users
    stompClient.subscribe("/topic/online", function (msg) {
      const data = JSON.parse(msg.body);
      updateOnlineStatus(data);
    });
  });
}

function sendMessageWS(message) {
  stompClient.send("/app/sendMessage", {}, JSON.stringify(message));
}

function sendTypingWS(data) {
  stompClient.send("/app/typing", {}, JSON.stringify(data));
}

// 🔥 Send online status
function sendOnlineStatus(data) {
  stompClient.send("/app/online", {}, JSON.stringify(data));
}
