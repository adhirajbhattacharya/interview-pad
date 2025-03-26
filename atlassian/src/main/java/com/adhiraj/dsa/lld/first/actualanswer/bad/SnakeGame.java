package com.adhiraj.dsa.lld.first.actualanswer.bad;

import java.util.*;

public class SnakeGame {
    Queue<int[]> body = new ArrayDeque<>();
    int size = 3;
    int[] head = new int[] {0, 2};
    int moveCount = 0;
    boolean gameOver = false;

    SnakeService service;

    public SnakeGame(SnakeService service) {
        this.service = service;
    }

    public void moveSnake(MoveDirection direction) {


    }

    public boolean isGameOver() {
        return gameOver;
    }
}
