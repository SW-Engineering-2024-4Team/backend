document.addEventListener('DOMContentLoaded', (event) => {
    const startGameButton = document.getElementById('startGameButton');
    const gameStateDiv = document.getElementById('gameState');
    const cardIdInput = document.getElementById('cardIdInput');
    const sendCardIdButton = document.getElementById('sendCardIdButton');

    let stompClient = null;
    let gameID = '1234'; // Example game ID
    let currentPlayerID = null; // Current player ID to be set dynamically

    function connect() {
        const socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/game', (message) => {
                console.log('Received message: ' + message.body);
                handleGameState(JSON.parse(message.body));
            });

            // Subscribe to receive card list messages dynamically after setting currentPlayerID
            if (currentPlayerID) {
                stompClient.subscribe('/topic/cards/' + currentPlayerID, (message) => {
                    console.log('Received card options: ' + message.body);
                    handleCardOptions(JSON.parse(message.body));
                });
            }

            // 추가된 부분: validPositions 구독
            stompClient.subscribe('/topic/validPositions', (message) => {
                console.log('Received valid positions: ' + message.body);
                handleValidPositions(JSON.parse(message.body));
            });

            // 추가된 부분: choiceRequest 구독
            stompClient.subscribe('/topic/choiceRequest', (message) => {
                console.log('Received choice request: ' + message.body);
                handleChoiceRequest(JSON.parse(message.body));
            });

            startGameButton.addEventListener('click', () => {
                startGame();
            });

            sendCardIdButton.addEventListener('click', () => {
                sendCardId();
            });
        });
    }

    function startGame() {
        const payload = {
            roomNumber: gameID,
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

    function handleGameState(gameState) {
        if (gameState.message) {
            gameStateDiv.innerHTML += `<p>${gameState.message}</p>`;
        }

        if (gameState.currentRound) {
            const players = gameState.players.map(player => formatPlayer(player)).join('\n\n');
            const mainBoard = formatMainBoard(gameState.mainBoard);
            const gameInfo = `Game ID: ${gameState.gameID}\nCurrent Round: ${gameState.currentRound}`;

            gameStateDiv.innerHTML += `<div class="section-title">Game Info</div>\n${gameInfo}\n\n<div class="section-title">Players</div>\n${players}\n\n<div class="section-title">Main Board</div>\n${mainBoard}`;
        }

        if (gameState.playerId && gameState.availableCards) {
            currentPlayerID = gameState.playerId;
            handlePlayerTurn(gameState.playerId, gameState.availableCards);

            // Re-subscribe to card list topic with the correct currentPlayerID
            stompClient.subscribe('/topic/cards/' + currentPlayerID, (message) => {
                console.log('Received card options: ' + message.body);
                handleCardOptions(JSON.parse(message.body));
            });
        }

        if (gameState.playerId && gameState.exchangeableCards) {
            handleExchangeableCards(gameState.playerId, gameState.exchangeableCards);
        }
    }

    function handlePlayerTurn(playerId, availableCards) {
        if (playerId === currentPlayerID) {
            let cardOptions = availableCards.map(card => `<button class="card-button" onclick="selectCard(${card.id})">${card.name}</button>`).join('');
            gameStateDiv.innerHTML += `<div class="section-title">Your Turn</div>\n${cardOptions}`;
        }
    }

    function handleExchangeableCards(playerId, exchangeableCards) {
        if (playerId === currentPlayerID) {
            let cardOptions = exchangeableCards.map(card => `<button class="card-button" onclick="exchangeCard(${card.id})">${card.name}</button>`).join('');
            gameStateDiv.innerHTML += `<div class="section-title">Exchangeable Cards</div>\n${cardOptions}`;
        }
    }

    function handleCardOptions(cardOptions) {
        let cardListHtml = cardOptions.map(card => `<button class="card-button" onclick="selectOccupationCard(${card.id})">${card.name}</button>`).join('');
        gameStateDiv.innerHTML += `<div class="section-title">Select a Card</div>\n${cardListHtml}`;
    }

    function selectCard(cardId) {
        const payload = {
            playerId: currentPlayerID,
            cardId: cardId
        };
        console.log('Selecting card with ID:', cardId);
        stompClient.send('/app/receivePlayerTurn', {}, JSON.stringify(payload));
    }

    function sendCardId() {
        const cardId = cardIdInput.value;
        if (cardId) {
            selectCard(cardId);
        } else {
            alert('Please enter a card ID');
        }
    }

    function exchangeCard(cardId) {
        // Implement the exchange card logic
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

    function selectOccupationCard(cardId) {
        const payload = {
            playerId: currentPlayerID,
            cardId: cardId
        };
        console.log('Selecting occupation card with ID:', cardId);
        stompClient.send('/app/selectedCard', {}, JSON.stringify(payload));
    }

    // Add the function definition to make it available globally
    window.selectOccupationCard = selectOccupationCard;

    // 추가된 부분: 좌표 선택 함수 정의 및 글로벌로 만들기
    function handleValidPositions(validPositionsMessage) {
        const { playerId, actionType, validPositions } = validPositionsMessage;

        if (playerId === currentPlayerID) {
            let positionOptions = validPositions.map(pos =>
                `<button class="position-button" onclick="selectPosition(${pos.x}, ${pos.y})">(${pos.x}, ${pos.y})</button>`
            ).join('');

            if (actionType === 'plow') {
                gameStateDiv.innerHTML += `<div class="section-title">Select a Position to Plow</div>\n${positionOptions}`;
            } else if (actionType === 'house') {
                gameStateDiv.innerHTML += `<div class="section-title">Select a Position to Build a House</div>\n${positionOptions}`;
            }
        }
    }

    // 선택된 좌표를 저장하는 함수
    function selectPosition(x, y) {
        const payload = {
            playerId: currentPlayerID,
            x: parseInt(x, 10),  // 좌표를 정수형으로 변환
            y: parseInt(y, 10)   // 좌표를 정수형으로 변환
        };
        console.log('Selecting position:', x, y);
        stompClient.send('/app/receiveSelectedPosition', {}, JSON.stringify(payload));
    }

    // JavaScript 수정 (옵션 선택 시)
    function handleChoiceRequest(choiceRequest) {
        const { playerId, choiceType, options } = choiceRequest;

        if (playerId === currentPlayerID && choiceType === 'AndOr') {
            let choiceOptions = Object.entries(options).map(([key, value], index) =>
                `<button class="choice-button" onclick="selectChoice('${choiceType}', ${index})">${value}</button>`
            ).join('');
            gameStateDiv.innerHTML += `<div class="section-title">Make a Choice</div>\n${choiceOptions}`;
        }
    }

    function selectChoice(choiceType, choice) {
        const payload = {
            playerId: currentPlayerID,
            choiceType: choiceType,
            choice: choice
        };
        console.log('Selecting choice:', choiceType, choice);
        stompClient.send('/app/playerChoice', {}, JSON.stringify(payload));
    }

    window.selectCard = selectCard;
    window.selectPosition = selectPosition;
    window.selectChoice = selectChoice;

    connect();
});
