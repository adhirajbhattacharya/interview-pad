package com.adhiraj.dsa.lld.first;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Value
public class SnakeGame implements SnakeGameI {
    Snake snake;

    public SnakeGame(int rows, int columns) {
        snake = new Snake(rows, columns);
    }

    @Override
    public void moveSnake(MoveDirection direction) {
        snake.move(direction);
    }

    @Override
    public boolean isGameOver() {
        return snake.isGameOver();
    }
}

interface SnakeGameI {
    void moveSnake(MoveDirection direction);
    boolean isGameOver();
}

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
class Snake {
    List<Cell> body;
    @NonFinal
    int size;
    @NonFinal
    int moves;
    @NonFinal
    boolean isGameOver;
    int colmax;
    int rowmax;

    Snake(int rowmax, int colmax) {
        body = new ArrayList<>();
        body.add(new Cell(0, 0));
        body.add(new Cell(0, 1));
        body.add(new Cell(0, 2));
        size = 3;
        moves = 0;
        isGameOver = false;
        this.rowmax = rowmax;
        this.colmax = colmax;
    }

    void move(MoveDirection direction) {
        Cell head = getHead();
        Cell newHead = direction.moveCell(head, rowmax, colmax);
        moves++;
        if (moves % 5 != 0) {
            body.removeFirst();
        } else {
            size++;
        }
        body.add(newHead);
    }

    boolean isGameOver() {
        if (isGameOver) return true;

        Cell head = getHead();
        this.isGameOver = body.stream().filter(cell -> Objects.equals(cell, head)).count() > 1L;
        return isGameOver;
    }

    private Cell getHead() {
        return body.getLast();
    }
}

@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
enum MoveDirection {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    int rowAdder, colAdder;
    MoveDirection(int rowAdder, int colAdder) {
        this.rowAdder = rowAdder;
        this.colAdder = colAdder;
    }

    Cell moveCell(Cell cell, int rowmax, int colmax) {
        int newrow = cell.getRow() + rowAdder;
        if (newrow == -1) newrow = rowmax - 1;
        if (newrow == rowmax) newrow = 0;

        int newcol = cell.getCol() + colAdder;
        if (newcol == -1) newcol = colmax - 1;
        if (newcol == colmax) newcol = 0;

        return new Cell(newrow, cell.getCol() + colAdder, cell.getType());
    }
}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
class Cell {
    int row, col;
    CellType type;

    Cell(int row, int col) {
        this(row, col, CellType.EMPTY);
    }

    Cell(Cell cell) {
        this(cell.row, cell.col, cell.type);
    }
}

enum CellType {
    EMPTY, SNAKE_BODY;
}
