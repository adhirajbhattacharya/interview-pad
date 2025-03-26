package com.adhiraj.dsa.lld.first.actualanswer.bad;

import java.util.Queue;

public class SnakeServiceImpl implements SnakeService {

    @Override
    public int moveSnake(Queue<int[]> body, MoveDirection direction, int moveCount) {
        int[] currHead = body.peek();
        int[] expectedHead = new int[] {currHead[0] + direction.getX(), currHead[1] + direction.getY()};
        body.offer(expectedHead);
        moveCount++;
        if (moveCount % 5 != 0) {
            body.poll();
        }
        return moveCount;
    }

    @Override
    public boolean isGameOver(Queue<int[]> body) {
        int[] currHead = body.peek();
        int count = 0;
        for (int[] point : body) {
            if (point[0] == currHead[0] && point[1] == currHead[1]) {
                count++;
            }
        }
        return count > 1;
    }
}
