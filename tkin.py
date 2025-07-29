import tkinter as tk
from tkinter import messagebox
import math
import random
import time

class TicTacToeGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Tic-Tac-Toe")
        self.root.resizable(False, False)

        self.board = [' ' for _ in range(9)]
        self.human_player = 'X'
        self.ai_player = 'O'
        self.game_over = False
        
        self.ai_thinking_messages = ["Hmm, let's see...", "My turn.", "Thinking...", "Okay, my move."]
        self.ai_mistake_probability = 0.2

        main_frame = tk.Frame(self.root, padx=15, pady=15, bg='#f0f0f0')
        main_frame.pack()
        
        self.status_label = tk.Label(main_frame, text="Your turn (X)", font=('Helvetica', 14), bg='#f0f0f0')
        self.status_label.pack(pady=(0, 15))

        board_frame = tk.Frame(main_frame, bg='#f0f0f0')
        board_frame.pack()

        self.buttons = []
        for i in range(9):
            button = tk.Button(
                board_frame, 
                text=' ', 
                font=('Helvetica', 24, 'bold'), 
                width=5, 
                height=2,
                bg='#ffffff',
                activebackground='#e0e0e0',
                relief='ridge',
                command=lambda i=i: self.handle_player_move(i)
            )
            button.grid(row=i//3, column=i%3, padx=5, pady=5)
            self.buttons.append(button)

        reset_button = tk.Button(main_frame, text="New Game", font=('Helvetica', 12), command=self.reset_game, bg='#d0d0d0')
        reset_button.pack(pady=(15, 0))

    def handle_player_move(self, index):
        if self.board[index] == ' ' and not self.game_over:
            self.make_move(index, self.human_player)

            if not self.game_over:
                delay = random.randint(400, 1200) 
                self.root.after(delay, self.ai_turn)

    def ai_turn(self):
        if not self.game_over:
            thinking_message = random.choice(self.ai_thinking_messages)
            self.status_label.config(text=thinking_message)
            self.root.update_idletasks()

            ai_move = self.find_best_move()
            if ai_move != -1:
                self.make_move(ai_move, self.ai_player)

    def make_move(self, index, player):
        self.board[index] = player
        player_color = '#4a90e2' if player == 'X' else '#e24a4a'
        self.buttons[index].config(text=player, state='disabled', disabledforeground=player_color)

        if self.check_winner(player):
            self.end_game(f"{'You win!' if player == self.human_player else 'The AI wins!'}")
        elif self.is_board_full():
            self.end_game("It's a draw!")
        else:
            next_player = self.ai_player if player == self.human_player else self.human_player
            turn_text = "AI's turn (O)" if next_player == self.ai_player else "Your turn (X)"
            self.status_label.config(text=turn_text)

    def check_winner(self, player):
        win_conditions = [
            [0, 1, 2], [3, 4, 5], [6, 7, 8],
            [0, 3, 6], [1, 4, 7], [2, 5, 8],
            [0, 4, 8], [2, 4, 6]
        ]
        for condition in win_conditions:
            if all(self.board[i] == player for i in condition):
                for i in condition:
                    self.buttons[i].config(bg='#a7f3d0')
                return True
        return False

    def is_board_full(self):
        return ' ' not in self.board

    def end_game(self, message):
        self.game_over = True
        self.status_label.config(text=message)
        messagebox.showinfo("Game Over", message)
        for button in self.buttons:
            if button['state'] == 'normal':
                button.config(state='disabled')

    def reset_game(self):
        self.board = [' ' for _ in range(9)]
        self.game_over = False
        self.status_label.config(text="Your turn (X)")
        for button in self.buttons:
            button.config(text=' ', state='normal', bg='#ffffff')

    def find_best_move(self):
        available_moves = self.get_available_moves()

        if random.random() < self.ai_mistake_probability and len(available_moves) > 0:
            return random.choice(available_moves)

        best_score = -math.inf
        best_move = -1
        for move in available_moves:
            self.board[move] = self.ai_player
            score = self.minimax(list(self.board), 0, False)
            self.board[move] = ' '
            if score > best_score:
                best_score = score
                best_move = move
        return best_move

    def minimax(self, current_board, depth, is_maximizing):
        if self.check_winner_logic(current_board, self.ai_player):
            return 10 - depth
        if self.check_winner_logic(current_board, self.human_player):
            return depth - 10
        if ' ' not in current_board:
            return 0

        if is_maximizing:
            best_score = -math.inf
            for move in self.get_available_moves_logic(current_board):
                current_board[move] = self.ai_player
                score = self.minimax(current_board, depth + 1, False)
                current_board[move] = ' '
                best_score = max(score, best_score)
            return best_score
        else:
            best_score = math.inf
            for move in self.get_available_moves_logic(current_board):
                current_board[move] = self.human_player
                score = self.minimax(current_board, depth + 1, True)
                current_board[move] = ' '
                best_score = min(score, best_score)
            return best_score
            
    def get_available_moves(self):
        return [i for i, spot in enumerate(self.board) if spot == ' ']

    def check_winner_logic(self, board_state, player):
        win_conditions = [[0,1,2],[3,4,5],[6,7,8],[0,3,6],[1,4,7],[2,5,8],[0,4,8],[2,4,6]]
        return any(all(board_state[i] == player for i in cond) for cond in win_conditions)

    def get_available_moves_logic(self, board_state):
        return [i for i, spot in enumerate(board_state) if spot == ' ']


if __name__ == "__main__":
    root = tk.Tk()
    app = TicTacToeGUI(root)
    root.mainloop()
