document.addEventListener("DOMContentLoaded", () => {
    const connectButton = document.getElementById("connectButton");
    const disconnectButton = document.getElementById("disconnectButton");
    const messageInput = document.getElementById("messageInput");
    const sendButton = document.getElementById("sendButton");
    const messageArea = document.getElementById("messageArea");
    let socket;

    connectButton.addEventListener("click", () => {
        socket = new WebSocket("ws://localhost:8080/websocket-endpoint");

        socket.onopen = (event) => {
            console.log("WebSocket connected");
            connectButton.disabled = true;
            disconnectButton.disabled = false;
            sendButton.disabled = false;
        };

        socket.onmessage = (event) => {
            const message = event.data;
            messageArea.innerHTML += `<p>${message}</p>`;
        };

        socket.onclose = (event) => {
            console.log("WebSocket disconnected");
            connectButton.disabled = false;
            disconnectButton.disabled = true;
            sendButton.disabled = true;
        };
    });

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
});
