package snake;

import com.adhiraj.dsa.lld.first.actualanswer.bad.MoveDirection;
import com.adhiraj.dsa.lld.first.actualanswer.bad.SnakeService;
import com.adhiraj.dsa.lld.first.actualanswer.bad.SnakeServiceImpl;

import java.util.ArrayDeque;
import java.util.Queue;

public class SnakeServiceImplTest {

    SnakeService snakeService = new SnakeServiceImpl();

    void testMoveSnakeUp() {
        Queue<int[]> body = getTestBody();
        Queue<int[]> expectedBody = getExpectedBody();
        int moveCount = 0;

        moveCount = snakeService.moveSnake(body, MoveDirection.UP, moveCount);

        assert expectedBody.size() == body.size();
        assert moveCount == 1;
        assert expectedBody.equals(body);
        assert !snakeService.isGameOver(body);
    }

    private Queue<int[]> getExpectedBody() {
        Queue<int[]> body = new ArrayDeque<>();
        body.offer(new int[] {0, 1});
        body.offer(new int[] {0, 2});
        body.offer(new int[] {9, 2});
        return body;
    }

    private Queue<int[]> getTestBody() {
        Queue<int[]> body = new ArrayDeque<>();
        body.offer(new int[] {0, 0});
        body.offer(new int[] {0, 1});
        body.offer(new int[] {0, 2});
        return body;
    }
}


//@Override
//public int moveSnake(Queue<int[]> body, MoveDirection direction, int moveCount) {
//    int[] currHead = body.peek();
//    int[] expectedHead = new int[] {currHead[0] + direction.getX(), currHead[1] + direction.getY()};
//    body.offer(expectedHead);
//    moveCount++;
//    if (moveCount % 5 != 0) {
//        body.poll();
//    }
//    return moveCount;
//}
