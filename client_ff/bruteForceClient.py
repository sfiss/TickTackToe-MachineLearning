import requests
import json
import time
import copy
from random import randint

INVALID_MOVE = 'invalid_move'
VERY_LOW = -50000
board_dimension = 4
player = 1
max_player = 2
max_level = 2

best_move = [-1, -1]
nodes_found = 0

games_won = 0
games_drawn = 0
games_lost = 0


def reset_game():
    url = 'http://192.168.178.48:8080/game' 
    response = requests.get(url)
    return json.loads(response.text)


def get_game_status():
    url = 'http://192.168.178.48:8080/game/status'
    response = requests.get(url)
    board = json.loads(response.text)
    return board


def make_move(move_x, move_y):
    url = 'http://192.168.178.48:8080/game/play?row='
    url = url + str(move_x) + '&column=' + str(move_y) + '&player=' + str(player)
    response = requests.post(url)
    return response.text


def is_move_valid(board, move_x, move_y):
    if move_x < 0 or move_y < 0:
        return False
    if board[move_x][move_y] != 0:
        return False
    else:
        return True

def give_random_move(board):
    tries = 0
    while True:
        move_x = randint(0,board_dimension - 1)
        move_y = randint(0,board_dimension - 1)
        if is_move_valid(board, move_x, move_y) == True:
            return [move_x, move_y]
        tries += 1
        if tries > 1000000:
            break
          
    
def get_winner(board):
    x = 0
    y = 0
    while x < board_dimension:
        winner = board[x][0]
        y = 0
        while y < board_dimension:
            if board[x][y] != winner:
                winner = 0
                break
            y += 1
        if winner != 0:
            return winner
        x += 1
    
    x = 0
    y = 0
    while y < board_dimension:
        x = 0
        winner = board[0][y]
        while x < board_dimension and winner != 0:
            if board[x][y] != winner:
                winner = 0
                break
            x += 1
        if winner != 0:
            return winner
        y += 1
    
    x = 0
    y = 0
    winner = board[0][0]
    while x < board_dimension and y < board_dimension and winner != 0:
        if board[x][y] != winner:
            winner = 0
        x += 1
        y += 1
    if winner != 0:
        return winner
    
    x = 0
    y = board_dimension - 1
    winner = board[x][y]
    while x < board_dimension and y >= 0 and winner != 0:
        if board[x][y] != winner:
            winner = 0
        x += 1
        y -= 1
    
    return winner
    
def get_next_player(current_player):
    current_player += 1
    if current_player > max_player:
        current_player = 1
    return current_player
        
def is_full(board):
    for row in board:
        for cell in row:
            if cell == 0:
                return False
    return True
    

    
    
def get_possible_moves(board):
    r = []
    x = 0
    while x < board_dimension:
        y = 0
        while y < board_dimension:
            if is_move_valid(board, x, y) == True:
                r.append([x, y])
            y += 1
        x += 1
    return r
    
    
level_1_calls = 0
level_2_calls = 0
level_3_calls = 0
level_4_calls = 0

def minmax(board, current_player, level):
    global best_move
    global nodes_found
    global level_1_calls
    global level_2_calls
    global level_3_calls
    global level_4_calls
    level += 1
    if level == 1:
        level_1_calls += 1
    if level == 2:
        level_2_calls += 1
    if level == 3:
        level_3_calls += 1
    if level == 4:
        level_4_calls += 1
    #print(str(level_4_calls/504) + "%")
    moves = get_possible_moves(board)
    #print("Move Count " + str(len(moves)) + " on level " + str(level))
    if len(moves) == 0 or level == max_level or get_winner(board) != 0 or is_full(board):
        nodes_found += 1
        if get_winner(board) == player:
            return 10
        elif get_winner(board) == 0:
            return 0
        else:
            return -10
    else:
        move_list = []
        score_list = []
        next_player = get_next_player(current_player)
        for move in moves:
            new_board = copy.deepcopy(board)
            new_board[move[0]][move[1]] = current_player
            v = minmax(new_board, next_player, level)
            score_list.append(v)
            move_list.append(move)
        if level == 1:
            print(str(move_list))
            print(str(score_list))
            
        if current_player == player:
            max_index = score_list.index(max(score_list))
            best_move = move_list[max_index]
            return score_list[max_index]
        else:
            min_index = score_list.index(min(score_list))
            best_move = move_list[min_index]
            return score_list[min_index]
        

                
while True:
    status = get_game_status()
    to_play = status['toPlay']
    print('Current game status ' + str(status))
    if to_play == player:
        current_board = status['game']
        
        level_1_calls = 0
        level_2_calls = 0
        level_3_calls = 0
        level_4_calls = 0
        nodes_found = 0
        
        dim = board_dimension // 2
        move = [dim, dim]
        if is_move_valid(current_board, dim, dim) is False:
            best_move = [-1, -1]
            value = minmax(current_board, player, 0)
            move = best_move
            print('Brute Force suggests move ' + str(move) + ' with value ' + str(value))
        if is_move_valid(current_board, move[0], move[1]) == False:
          move = give_random_move(current_board)
          print('Making random move ' + str(move))
        else:
            print('Making move ' + str(move))
        resp = make_move(move[0], move[1])
    time.sleep(.1)