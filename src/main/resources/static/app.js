document.addEventListener('DOMContentLoaded', (event) => {
    const startGameButton = document.getElementById('startGameButton');
    const gameStateDiv = document.getElementById('gameState');

    let stompClient = null;

    function connect() {
        const socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/game', (message) => {
                console.log('Received message: ' + message.body);
                showGameState(JSON.parse(message.body));
            });

            // 연결이 성공적으로 설정된 후에만 이벤트 리스너를 설정합니다.
            startGameButton.addEventListener('click', () => {
                startGame();
            });
        });
    }

    function startGame() {
        const payload = {
            roomNumber: '1234',
            players: [
                { id: '1', name: 'Player 1' },
                { id: '2', name: 'Player 2' },
                { id: '3', name: 'Player 3' },
                { id: '4', name: 'Player 4' }
            ]
        };
        console.log('Sending startGame message with payload:', payload);
        stompClient.send('/app/startGame', {}, JSON.stringify(payload));
    }

    function showGameState(gameState) {
        const players = gameState.players.map(player => formatPlayer(player)).join('\n\n');
        const mainBoard = formatMainBoard(gameState.mainBoard);
        const gameInfo = `Game ID: ${gameState.gameID}\nCurrent Round: ${gameState.currentRound}`;

        gameStateDiv.innerHTML = `<div class="section-title">Game Info</div>\n${gameInfo}\n\n<div class="section-title">Players</div>\n${players}\n\n<div class="section-title">Main Board</div>\n${mainBoard}`;
    }

    function formatPlayer(player) {
        return `Player ID: ${player.id}\nName: ${player.name}\nResources: ${JSON.stringify(player.resources, null, 2)}\nOccupation Cards: ${formatCards(player.occupationCards)}\nMinor Improvement Cards: ${formatCards(player.minorImprovementCards)}\nActive Cards: ${formatCards(player.activeCards)}\nPlayer Board:\n${formatPlayerBoard(player.playerBoard)}`;
    }

    function formatCards(cards) {
        return cards.map(card => `  - ${card.name}: ${card.description}`).join('\n');
    }

    function formatPlayerBoard(board) {
        return `  Tiles:\n${formatBoard(board.tiles)}\n  Family Members:\n${formatBoard(board.familyMembers)}\n  Animals:\n${formatBoard(board.animals)}\n  Fences: ${JSON.stringify(board.fences)}\n  Fence Areas: ${JSON.stringify(board.fenceAreas)}`;
    }

    function formatBoard(board) {
        return board.map(row => row.map(tile => tile ? JSON.stringify(tile, null, 2) : 'null').join(', ')).join('\n');
    }

    function formatMainBoard(mainBoard) {
        return `Cards:\n${mainBoard.cards.map(card => `  - ${card.name}: ${card.description}`).join('\n')}`;
    }

    connect();
});
