// document.addEventListener('DOMContentLoaded', (event) => {
//     const startGameButton = document.getElementById('startGameButton');
//     const gameStateDiv = document.getElementById('gameState');
//     const cardIdInput = document.getElementById('cardIdInput');
//     const sendCardIdButton = document.getElementById('sendCardIdButton');
//
//     let stompClient = null;
//     let gameID = '1234'; // Example game ID
//     let currentPlayerID = null; // Current player ID to be set dynamically
//
//     function connect() {
//         const socket = new SockJS('/gs-guide-websocket');
//         stompClient = Stomp.over(socket);
//         stompClient.connect({}, (frame) => {
//             console.log('Connected: ' + frame);
//             stompClient.subscribe('/topic/game', (message) => {
//                 console.log('Received message: ' + message.body);
//                 handleGameState(JSON.parse(message.body));
//             });
//
//             stompClient.subscribe('/topic/cards/' + currentPlayerID, (message) => {
//                 console.log('Received card options: ' + message.body);
//                 handleCardOptions(JSON.parse(message.body));
//             });
//
//             stompClient.subscribe('/topic/validPositions', (message) => {
//                 console.log('Received valid positions: ' + message.body);
//                 handleValidPositions(JSON.parse(message.body));
//             });
//
//             stompClient.subscribe('/topic/choiceRequest', (message) => {
//                 console.log('Received choice request: ' + message.body);
//                 handleChoiceRequest(JSON.parse(message.body));
//             });
//
//             stompClient.subscribe('/topic/majorImprovementCards', (message) => {
//                 console.log('Received major improvement cards: ' + message.body);
//                 handleMajorImprovementCards(JSON.parse(message.body));
//             });
//             // 플레이어 자원 업데이트를 받는 부분 추가
//             stompClient.subscribe('/topic/playerResources', (message) => {
//                 console.log('Received player resources: ' + message.body);
//                 handlePlayerResources(JSON.parse(message.body));
//             });
//
//             stompClient.subscribe('/topic/activeCards', (message) => {
//                 console.log('Received active cards: ' + message.body);
//                 handleActiveCards(JSON.parse(message.body));
//             });
//
//             startGameButton.addEventListener('click', () => {
//                 startGame();
//             });
//
//             sendCardIdButton.addEventListener('click', () => {
//                 sendCardId();
//             });
//         });
//     }
//
//     function startGame() {
//         const payload = {
//             roomNumber: gameID,
//             players: [
//                 { id: '1', name: 'Player 1' },
//                 { id: '2', name: 'Player 2' },
//                 { id: '3', name: 'Player 3' },
//                 { id: '4', name: 'Player 4' }
//             ]
//         };
//         console.log('Sending startGame message with payload:', payload);
//         stompClient.send('/app/startGame', {}, JSON.stringify(payload));
//     }
//
//     function handleGameState(gameState) {
//         if (gameState.message) {
//             gameStateDiv.innerHTML += `<p>${gameState.message}</p>`;
//         }
//
//         if (gameState.currentRound) {
//             const players = gameState.players.map(player => formatPlayer(player)).join('\n\n');
//             const mainBoard = formatMainBoard(gameState.mainBoard);
//             const gameInfo = `Game ID: ${gameState.gameID}\nCurrent Round: ${gameState.currentRound}`;
//
//             gameStateDiv.innerHTML += `<div class="section-title">Game Info</div>\n${gameInfo}\n\n<div class="section-title">Players</div>\n${players}\n\n<div class="section-title">Main Board</div>\n${mainBoard}`;
//         }
//
//         if (gameState.playerId && gameState.availableCards) {
//             currentPlayerID = gameState.playerId;
//             handlePlayerTurn(gameState.playerId, gameState.availableCards);
//
//             stompClient.subscribe('/topic/cards/' + currentPlayerID, (message) => {
//                 console.log('Received card options: ' + message.body);
//                 handleCardOptions(JSON.parse(message.body));
//             });
//         }
//
//         if (gameState.playerId && gameState.exchangeableCards) {
//             handleExchangeableCards(gameState.playerId, gameState.exchangeableCards);
//         }
//     }
//
//     function handlePlayerTurn(playerId, availableCards) {
//         if (playerId === currentPlayerID) {
//             let cardOptions = availableCards.map(card => `<button class="card-button" onclick="selectCard(${card.id})">${card.name}</button>`).join('');
//             gameStateDiv.innerHTML += `<div class="section-title">Your Turn</div>\n${cardOptions}`;
//         }
//     }
//
//     function handleExchangeableCards(playerId, exchangeableCards) {
//         if (playerId === currentPlayerID) {
//             let cardOptions = exchangeableCards.map(card => `<button class="card-button" onclick="exchangeCard(${card.id})">${card.name}</button>`).join('');
//             gameStateDiv.innerHTML += `<div class="section-title">Exchangeable Cards</div>\n${cardOptions}`;
//         }
//     }
//
//     function handleCardOptions(cardOptions) {
//         let cardListHtml = cardOptions.map(card => `<button class="card-button" onclick="selectOccupationCard(${card.id})">${card.name}</button>`).join('');
//         gameStateDiv.innerHTML += `<div class="section-title">Select a Card</div>\n${cardListHtml}`;
//     }
//
//     function selectCard(cardId) {
//         const payload = {
//             playerId: currentPlayerID,
//             cardId: cardId
//         };
//         console.log('Selecting card with ID:', cardId);
//         stompClient.send('/app/receivePlayerTurn', {}, JSON.stringify(payload));
//     }
//
//     function sendCardId() {
//         const cardId = cardIdInput.value;
//         if (cardId) {
//             selectCard(cardId);
//         } else {
//             alert('Please enter a card ID');
//         }
//     }
//
//     function handleMajorImprovementCards(message) {
//         const { playerId, majorImprovementCards } = message;
//
//         if (playerId === currentPlayerID) {
//             let cardListHtml = majorImprovementCards.map(card => `<li>${card.name}: ${card.description}</li>`).join('');
//             gameStateDiv.innerHTML += `<div class="section-title">Your Major Improvement Cards</div>\n<ul>${cardListHtml}</ul>`;
//             console.log('Major improvement cards displayed for player: ', currentPlayerID, majorImprovementCards);
//         } else {
//             console.log('Received major improvement cards for player: ', playerId, majorImprovementCards);
//         }
//     }
//
//     function exchangeCard(cardId) {
//         // Implement the exchange card logic
//     }
//
//     function formatPlayer(player) {
//         return `Player ID: ${player.id}\nName: ${player.name}\nResources: ${JSON.stringify(player.resources, null, 2)}\nOccupation Cards: ${formatCards(player.occupationCards)}\nMinor Improvement Cards: ${formatCards(player.minorImprovementCards)}\nActive Cards: ${formatCards(player.activeCards)}\nPlayer Board:\n${formatPlayerBoard(player.playerBoard)}`;
//     }
//
//     function formatCards(cards) {
//         return cards.map(card => `  - ${card.name}: ${card.description}`).join('\n');
//     }
//
//     function formatPlayerBoard(board) {
//         return `  Tiles:\n${formatBoard(board.tiles)}\n  Family Members:\n${formatBoard(board.familyMembers)}\n  Animals:\n${formatBoard(board.animals)}\n  Fences: ${JSON.stringify(board.fences)}\n  Fence Areas: ${JSON.stringify(board.fenceAreas)}`;
//     }
//
//     function formatBoard(board) {
//         return board.map(row => row.map(tile => tile ? JSON.stringify(tile, null, 2) : 'null').join(', ')).join('\n');
//     }
//
//     function formatMainBoard(mainBoard) {
//         return `Cards:\n${mainBoard.cards.map(card => `  - ${card.name}: ${card.description}`).join('\n')}`;
//     }
//
//     function selectOccupationCard(cardId) {
//         const payload = {
//             playerId: currentPlayerID,
//             cardId: cardId
//         };
//         console.log('Selecting card with ID:', cardId);
//         stompClient.send('/app/selectedCard', {}, JSON.stringify(payload));
//     }
//
//     window.selectOccupationCard = selectOccupationCard;
//
//     function handlePlayerResources(message) {
//         const { playerId, resources } = message;
//
//         if (playerId === currentPlayerID) {
//             let resourceListHtml = Object.entries(resources).map(([key, value]) => `<li>${key}: ${value}</li>`).join('');
//             gameStateDiv.innerHTML += `<div class="section-title">Your Resources</div>\n<ul>${resourceListHtml}</ul>`;
//             console.log('Player resources updated for player: ', currentPlayerID, resources);
//         } else {
//             console.log('Received player resources for player: ', playerId, resources);
//         }
//     }
//
//     function handleValidPositions(validPositionsMessage) {
//         const { playerId, actionType, validPositions } = validPositionsMessage;
//
//         console.log('handleValidPositions called with:', validPositionsMessage);
//
//         if (playerId === currentPlayerID) {
//             let positionOptions = validPositions.map(pos => {
//                 return `<button class="position-button" onclick="selectPosition(${pos.x}, ${pos.y})">(${pos.x}, ${pos.y})</button>`;
//             }).join('');
//
//             console.log('Generated position options:', positionOptions);
//
//             if (actionType === 'plow') {
//                 gameStateDiv.innerHTML += `<div class="section-title">Select a Position to Plow</div>\n${positionOptions}`;
//             } else if (actionType === 'house') {
//                 gameStateDiv.innerHTML += `<div class="section-title">Select a Position to Build a House</div>\n${positionOptions}`;
//             } else if (actionType === 'barn') {
//                 gameStateDiv.innerHTML += `<div class="section-title">Select a Position to Build a Barn</div>\n${positionOptions}`;
//             } else {
//                 console.log(`Unknown actionType: ${actionType}`);
//             }
//
//             console.log('Updated gameStateDiv:', gameStateDiv.innerHTML);
//         }
//     }
//
//
//
//     function selectPosition(x, y) {
//         const payload = {
//             playerId: currentPlayerID,
//             x: parseInt(x, 10),  // 좌표를 정수형으로 변환
//             y: parseInt(y, 10)   // 좌표를 정수형으로 변환
//         };
//         console.log('Selecting position:', x, y);
//         stompClient.send('/app/receiveSelectedPosition', {}, JSON.stringify(payload));
//     }
//
//     function handleChoiceRequest(choiceRequest) {
//         const { playerId, choiceType, options } = choiceRequest;
//
//         console.log(`Received choice request: playerId=${playerId}, choiceType=${choiceType}, options=${JSON.stringify(options)}`); // 디버깅 메시지 추가
//
//         if (playerId === currentPlayerID && (choiceType === 'AndOr' || choiceType === 'Then' || choiceType === 'Or')) {
//             let choiceOptions = Object.entries(options).map(([key, value], index) =>
//                 `<button class="choice-button" onclick="selectChoice('${choiceType}', ${index})">${value}</button>`
//             ).join('');
//
//             // 현재 gameStateDiv의 내용을 로그로 출력
//             console.log('Before appending choice options, gameStateDiv content:', gameStateDiv.innerHTML);
//
//             gameStateDiv.innerHTML += `<div class="section-title">Make a Choice</div>\n${choiceOptions}`;
//
//             // 업데이트된 gameStateDiv의 내용을 로그로 출력
//             console.log('Choice options generated and appended to gameStateDiv:', choiceOptions);
//             console.log('After appending choice options, gameStateDiv content:', gameStateDiv.innerHTML); // 디버깅 메시지 추가
//         } else {
//             console.log('Choice request ignored for player:', playerId, 'current player:', currentPlayerID); // 디버깅 메시지 추가
//         }
//     }
//
//     function selectChoice(choiceType, choice) {
//         let payload;
//         if (choiceType === 'AndOr') {
//             payload = {
//                 playerId: currentPlayerID,
//                 choiceType: choiceType,
//                 choice: choice
//             };
//         } else if (choiceType === 'Then' || choiceType === 'Or') {
//             const booleanChoice = choice === 0;
//             payload = {
//                 playerId: currentPlayerID,
//                 choiceType: choiceType,
//                 choice: booleanChoice
//             };
//         }
//         console.log('Selecting choice:', choiceType, choice);
//         console.log('Payload to send:', payload); // 디버깅 메시지 추가
//         stompClient.send('/app/playerChoice', {}, JSON.stringify(payload));
//     }
//
//     // 활성 카드 목록 처리 및 표시 함수
//     function handleActiveCards(message) {
//         const { playerId, majorImprovementCards } = message;
//
//         if (playerId === currentPlayerID) {
//             let cardListHtml = majorImprovementCards.map(card => `<li>${card.name}: ${card.description}</li>`).join('');
//             gameStateDiv.innerHTML += `<div class="section-title">Your Active Cards</div>\n<ul>${cardListHtml}</ul>`;
//             console.log('Active cards displayed for player: ', currentPlayerID, majorImprovementCards);
//         } else {
//             console.log('Received active cards for player: ', playerId, majorImprovementCards);
//         }
//     }
//
//
//
//
//     window.selectCard = selectCard;
//     window.selectPosition = selectPosition;
//     window.selectChoice = selectChoice;
//
//     connect();
// });

document.addEventListener('DOMContentLoaded', (event) => {
    const startGameButton = document.getElementById('startGameButton');
    const viewExCardsButton = document.getElementById('viewExCardsButton');
    const gameStateDiv = document.getElementById('gameState');
    const cardIdInput = document.getElementById('cardIdInput');
    const sendCardIdButton = document.getElementById('sendCardIdButton');
    const exchangePopup = document.getElementById('exchangePopup');
    const exchangeButtons = document.getElementById('exchangeButtons');
    const closePopupButton = document.getElementById('closePopupButton');

    closePopupButton.addEventListener('click', () => {
        exchangePopup.style.display = 'none';
    });

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

            stompClient.subscribe('/topic/cards/' + currentPlayerID, (message) => {
                console.log('Received card options: ' + message.body);
                handleCardOptions(JSON.parse(message.body));
            });

            stompClient.subscribe('/topic/validPositions', (message) => {
                console.log('Received valid positions: ' + message.body);
                handleValidPositions(JSON.parse(message.body));
            });

            stompClient.subscribe('/topic/choiceRequest', (message) => {
                console.log('Received choice request: ' + message.body);
                handleChoiceRequest(JSON.parse(message.body));
            });

            stompClient.subscribe('/topic/majorImprovementCards', (message) => {
                console.log('Received major improvement cards: ' + message.body);
                handleMajorImprovementCards(JSON.parse(message.body));
            });

            // 플레이어 자원 업데이트를 받는 부분 추가
            stompClient.subscribe('/topic/playerResources', (message) => {
                console.log('Received player resources: ' + message.body);
                handlePlayerResources(JSON.parse(message.body));
            });

            stompClient.subscribe('/topic/activeCards', (message) => {
                console.log('Received active cards: ' + message.body);
                handleActiveCards(JSON.parse(message.body));
            });

            // 교환 가능 카드 정보를 받는 부분 추가
            stompClient.subscribe('/topic/exchangeableCards', (message) => {
                console.log('Received exchangeable cards info: ' + message.body);
                handleExchangeableCards(JSON.parse(message.body));
            });

            startGameButton.addEventListener('click', () => {
                startGame();
            });

            viewExCardsButton.addEventListener('click', () => {
                viewExchangeableCards();
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
        stompClient.send('/app/1/start', {}, JSON.stringify(payload));
    }

    function viewExchangeableCards() {
        const payload = {
            playerId: currentPlayerID
        };
        console.log('Requesting exchangeable cards for player:', currentPlayerID);
        stompClient.send('/app/viewExchangeableCards', {}, JSON.stringify(payload));
    }

    // function handleGameState(gameState) {
    //     if (gameState.message) {
    //         gameStateDiv.innerHTML += `<p>${gameState.message}</p>`;
    //     }
    //
    //     if (gameState.currentRound) {
    //         const players = gameState.players.map(player => formatPlayer(player)).join('\n\n');
    //         const mainBoard = formatMainBoard(gameState.mainBoard);
    //         const gameInfo = `Game ID: ${gameState.gameID}\nCurrent Round: ${gameState.currentRound}`;
    //
    //         gameStateDiv.innerHTML += `<div class="section-title">Game Info</div>\n${gameInfo}\n\n<div class="section-title">Players</div>\n${players}\n\n<div class="section-title">Main Board</div>\n${mainBoard}`;
    //     }
    //
    //     if (gameState.playerId && gameState.availableCards) {
    //         currentPlayerID = gameState.playerId;
    //         handlePlayerTurn(gameState.playerId, gameState.availableCards);
    //
    //         stompClient.subscribe('/topic/cards/' + currentPlayerID, (message) => {
    //             console.log('Received card options: ' + message.body);
    //             handleCardOptions(JSON.parse(message.body));
    //         });
    //     }
    //
    //     if (gameState.playerId && gameState.exchangeableCards) {
    //         handleExchangeableCards(gameState.playerId, gameState.exchangeableCards);
    //     }
    // }
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
            currentPlayerID = gameState.playerId; // Ensure currentPlayerID is set
            handlePlayerTurn(gameState.playerId, gameState.availableCards);

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

    function handleMajorImprovementCards(message) {
        const { playerId, majorImprovementCards } = message;

        if (playerId === currentPlayerID) {
            let cardListHtml = majorImprovementCards.map(card => `<li>${card.name}: ${card.description}</li>`).join('');
            gameStateDiv.innerHTML += `<div class="section-title">Your Major Improvement Cards</div>\n<ul>${cardListHtml}</ul>`;
            console.log('Major improvement cards displayed for player: ', currentPlayerID, majorImprovementCards);
        } else {
            console.log('Received major improvement cards for player: ', playerId, majorImprovementCards);
        }
    }

    window.exchangeCard = function(cardId) {
        const payload = {
            playerId: currentPlayerID,
            cardId: cardId
        };
        console.log('Exchanging card with ID:', cardId);
        stompClient.send('/app/exchangeResources', {}, JSON.stringify(payload));
    }


    function viewExchangeableCards() {
        if (currentPlayerID !== null) { // Ensure currentPlayerID is not null
            const payload = {
                playerId: currentPlayerID
            };
            console.log('Requesting exchangeable cards for player:', currentPlayerID);
            stompClient.send('/app/viewExchangeableCards', {}, JSON.stringify(payload));
        } else {
            console.log('Current player ID is null');
        }
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
        console.log('Selecting card with ID:', cardId);
        stompClient.send('/app/selectedCard', {}, JSON.stringify(payload));
    }

    window.selectOccupationCard = selectOccupationCard;

    function handlePlayerResources(message) {
        const { playerId, resources } = message;

        if (playerId === currentPlayerID) {
            let resourceListHtml = Object.entries(resources).map(([key, value]) => `<li>${key}: ${value}</li>`).join('');
            gameStateDiv.innerHTML += `<div class="section-title">Your Resources</div>\n<ul>${resourceListHtml}</ul>`;
            console.log('Player resources updated for player: ', currentPlayerID, resources);
        } else {
            console.log('Received player resources for player: ', playerId, resources);
        }
    }

    function handleValidPositions(validPositionsMessage) {
        const { playerId, actionType, validPositions } = validPositionsMessage;

        console.log('handleValidPositions called with:', validPositionsMessage);

        if (playerId === currentPlayerID) {
            let positionOptions = validPositions.map(pos => {
                return `<button class="position-button" onclick="selectPosition(${pos.x}, ${pos.y})">(${pos.x}, ${pos.y})</button>`;
            }).join('');

            console.log('Generated position options:', positionOptions);

            if (actionType === 'plow') {
                gameStateDiv.innerHTML += `<div class="section-title">Select a Position to Plow</div>\n${positionOptions}`;
            } else if (actionType === 'house') {
                gameStateDiv.innerHTML += `<div class="section-title">Select a Position to Build a House</div>\n${positionOptions}`;
            } else if (actionType === 'barn') {
                gameStateDiv.innerHTML += `<div class="section-title">Select a Position to Build a Barn</div>\n${positionOptions}`;
            } else {
                console.log(`Unknown actionType: ${actionType}`);
            }

            console.log('Updated gameStateDiv:', gameStateDiv.innerHTML);
        }
    }

    function selectPosition(x, y) {
        const payload = {
            playerId: currentPlayerID,
            x: parseInt(x, 10),  // 좌표를 정수형으로 변환
            y: parseInt(y, 10)   // 좌표를 정수형으로 변환
        };
        console.log('Selecting position:', x, y);
        stompClient.send('/app/receiveSelectedPosition', {}, JSON.stringify(payload));
    }

    function handleChoiceRequest(choiceRequest) {
        const { playerId, choiceType, options } = choiceRequest;

        console.log(`Received choice request: playerId=${playerId}, choiceType=${choiceType}, options=${JSON.stringify(options)}`); // 디버깅 메시지 추가

        if (playerId === currentPlayerID && (choiceType === 'AndOr' || choiceType === 'Then' || choiceType === 'Or')) {
            let choiceOptions = Object.entries(options).map(([key, value], index) =>
                `<button class="choice-button" onclick="selectChoice('${choiceType}', ${index})">${value}</button>`
            ).join('');

            // 현재 gameStateDiv의 내용을 로그로 출력
            console.log('Before appending choice options, gameStateDiv content:', gameStateDiv.innerHTML);

            gameStateDiv.innerHTML += `<div class="section-title">Make a Choice</div>\n${choiceOptions}`;

            // 업데이트된 gameStateDiv의 내용을 로그로 출력
            console.log('Choice options generated and appended to gameStateDiv:', choiceOptions);
            console.log('After appending choice options, gameStateDiv content:', gameStateDiv.innerHTML); // 디버깅 메시지 추가
        } else {
            console.log('Choice request ignored for player:', playerId, 'current player:', currentPlayerID); // 디버깅 메시지 추가
        }
    }

    function selectChoice(choiceType, choice) {
        let payload;
        if (choiceType === 'AndOr') {
            payload = {
                playerId: currentPlayerID,
                choiceType: choiceType,
                choice: choice
            };
        } else if (choiceType === 'Then' || choiceType === 'Or') {
            const booleanChoice = choice === 0;
            payload = {
                playerId: currentPlayerID,
                choiceType: choiceType,
                choice: booleanChoice
            };
        }
        console.log('Selecting choice:', choiceType, choice);
        console.log('Payload to send:', payload); // 디버깅 메시지 추가
        stompClient.send('/app/playerChoice', {}, JSON.stringify(payload));
    }

    function handleExchangeableCards(exchangeableCardsInfo) {
        const { playerId, exchangeableCards } = exchangeableCardsInfo;
        if (playerId === currentPlayerID) {
            exchangeButtons.innerHTML = exchangeableCards.map(card =>
                `<button class="card-button" onclick="exchangeCard(${card.id})">${card.name} (Max: ${card.maxExchangeAmount})</button>`).join('');
            exchangePopup.style.display = 'block';
        }
    }


    // 활성 카드 목록 처리 및 표시 함수
    function handleActiveCards(message) {
        const { playerId, majorImprovementCards } = message;

        if (playerId === currentPlayerID) {
            let cardListHtml = majorImprovementCards.map(card => `<li>${card.name}: ${card.description}</li>`).join('');
            gameStateDiv.innerHTML += `<div class="section-title">Your Active Cards</div>\n<ul>${cardListHtml}</ul>`;
            console.log('Active cards displayed for player: ', currentPlayerID, majorImprovementCards);
        } else {
            console.log('Received active cards for player: ', playerId, majorImprovementCards);
        }
    }

    window.selectCard = selectCard;
    window.selectPosition = selectPosition;
    window.selectChoice = selectChoice;

    connect();
});

