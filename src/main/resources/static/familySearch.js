document.addEventListener("DOMContentLoaded", () => {
    const connectButton = document.getElementById("connectButton");
    const disconnectButton = document.getElementById("disconnectButton");
    const messageInput = document.getElementById("messageInput");
    const sendButton = document.getElementById("sendButton");
    const messageArea = document.getElementById("messageArea");
    let socket;

    connectSocket();

    function connectSocket() {
        socket = new WebSocket("ws://localhost:8080/websocket-FamilySearch");

        socket.onopen = (event) => {
            console.log("WebSocket connected");
            connectButton.disabled = true;
            disconnectButton.disabled = false;
            sendButton.disabled = false;
        };

        socket.onmessage = (event) => {
            const message = event.data;
            const nickNames = message.split(', ');

            // Clear previous messages and add new messages with fade-in effect
            messageArea.innerHTML = '';
            for (const nickName of nickNames) {
                const messageItem = document.createElement("p");
                messageItem.textContent = nickName;
                messageItem.classList.add("fade-in"); // Add fade-in class
                messageArea.appendChild(messageItem);
            }

            // Remove fade-in class after animation duration
            setTimeout(() => {
                messageArea.querySelectorAll(".fade-in").forEach(item => {
                    item.classList.remove("fade-in");
                });
            }, 500); // Animation duration is 0.5s (500ms)
        };

        socket.onclose = (event) => {
            console.log("WebSocket disconnected");
            connectButton.disabled = false;
            disconnectButton.disabled = true;
            sendButton.disabled = true;
        };
    }

    disconnectButton.addEventListener("click", () => {
        if (socket) {
            socket.close();
        }
    });

    sendButton.addEventListener("click", () => {
        const message = messageInput.value;
        if (socket && message) {
            socket.send(message);
            messageInput.value = "";
        }
    });

    // 입력이 변경될 때마다 자동으로 요청 보내기
    messageInput.addEventListener("input", () => {
        const message = messageInput.value;
        if (socket && message) {
            socket.send(message);
        }
    });
});
