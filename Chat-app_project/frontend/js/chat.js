connectWebSocket();

const token = localStorage.getItem("token");
const loggedInUserId = parseInt(localStorage.getItem("userId"));

let currentUserId = null;
let currentChatType = "private";
let currentGroupId = null;

// 🔴 unread counts
let unreadCounts = {};

// 🔹 Load users
async function loadUsers() {
  const res = await fetch("http://localhost:8080/api/users/all", {
    headers: { Authorization: "Bearer " + token },
  });

  const users = await res.json();

  const container = document.getElementById("userList");
  container.innerHTML = "";

  users.forEach((user) => {
    if (user.id === loggedInUserId) return;

    const div = document.createElement("div");
    div.className = "user-item";
    div.id = "user-" + user.id;

    div.innerHTML = `
      ${user.username}
      <span id="online-${user.id}" style="color:gray;">●</span>
      <span id="unread-${user.id}" style="color:red; font-size:12px;"></span>
    `;

    div.onclick = () => {
      unreadCounts[user.id] = 0;
      updateUnreadUI(user.id);
      selectUser(user.id, user.username);
    };

    container.appendChild(div);
  });
}

// 🔹 Load groups
async function loadGroups() {
  const res = await fetch("http://localhost:8080/api/groups/all", {
    headers: { Authorization: "Bearer " + token },
  });

  const groups = await res.json();

  const container = document.getElementById("groupList");
  container.innerHTML = "";

  groups.forEach((group) => {
    const div = document.createElement("div");
    div.className = "user-item";

    div.innerText = group.groupName;

    div.onclick = () => selectGroup(group.id, group.groupName);

    container.appendChild(div);
  });
}

// 🔹 Select user
function selectUser(id, name) {
  currentUserId = id;
  currentChatType = "private";

  document.getElementById("chatWith").innerText = name;

  loadMessages();
}

// 🔹 Select group
function selectGroup(id, name) {
  currentGroupId = id;
  currentChatType = "group";

  document.getElementById("chatWith").innerText = name;

  loadGroupMessages();
}

// 🔹 Send message
function sendMessage() {
  const content = document.getElementById("messageInput").value.trim();
  if (!content) return;

  let message;

  if (currentChatType === "group") {
    message = {
      senderId: loggedInUserId,
      senderName: localStorage.getItem("email"),
      groupId: currentGroupId,
      content,
    };
  } else {
    message = {
      senderId: loggedInUserId,
      receiverId: currentUserId,
      content,
    };
  }

  sendMessageWS(message);

  fetch("http://localhost:8080/api/chat/send", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify(message),
  });

  document.getElementById("messageInput").value = "";
}

// 🔹 Load private messages
async function loadMessages() {
  const res = await fetch(
    `http://localhost:8080/api/chat/messages?senderId=${loggedInUserId}&receiverId=${currentUserId}`,
    { headers: { Authorization: "Bearer " + token } },
  );

  const messages = await res.json();

  const container = document.getElementById("messages");
  container.innerHTML = "";

  messages.forEach(displayMessage);
}

// 🔹 Load group messages
async function loadGroupMessages() {
  const res = await fetch(
    `http://localhost:8080/api/chat/group?groupId=${currentGroupId}`,
    { headers: { Authorization: "Bearer " + token } },
  );

  const messages = await res.json();

  const container = document.getElementById("messages");
  container.innerHTML = "";

  messages.forEach(displayMessage);
}

// 🔹 Display message
function displayMessage(msg) {
  if (currentChatType === "private") {
    if (msg.receiverId !== currentUserId && msg.senderId !== currentUserId) {
      // 🔴 unread
      if (msg.receiverId === loggedInUserId) {
        unreadCounts[msg.senderId] = (unreadCounts[msg.senderId] || 0) + 1;
        updateUnreadUI(msg.senderId);
      }
      return;
    }
  }

  if (currentChatType === "group") {
    if (msg.groupId !== currentGroupId) return;
  }

  const container = document.getElementById("messages");

  const div = document.createElement("div");
  div.className =
    "message " + (msg.senderId === loggedInUserId ? "sent" : "received");

  div.innerHTML = `
        
        ${msg.groupId ? `<b>${msg.senderName ? msg.senderName : "User"}</b><br>` : ""}
        <div>${msg.content}</div>
        <div class="timestamp">${new Date(msg.timestamp).toLocaleTimeString()}</div>
    `;

  container.appendChild(div);
  container.scrollTop = container.scrollHeight;
}

// 🔴 Update unread UI
function updateUnreadUI(userId) {
  const badge = document.getElementById("unread-" + userId);
  const count = unreadCounts[userId] || 0;
  badge.innerText = count > 0 ? `(${count})` : "";
}

// 🟢 Online status update
function updateOnlineStatus(data) {
  const dot = document.getElementById("online-" + data.userId);
  if (dot) {
    dot.style.color = data.online ? "green" : "gray";
  }
}

// 🔹 Init
loadUsers();
loadGroups();

// 🔥 send online status
sendOnlineStatus({
  userId: loggedInUserId,
  online: true,
});

// 🔹 Logout
function logout() {
  localStorage.clear();
  window.location.href = "index.html";
}
