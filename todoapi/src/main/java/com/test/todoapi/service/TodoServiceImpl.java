package com.test.todoapi.service;


import com.henheang.securityapi.repository.UserRepository;
import com.test.todoapi.domain.TodoList;
import com.test.todoapi.payload.TodoListRequest;
import com.test.todoapi.payload.TodoListResponse;
import com.test.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor

public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;


    @Override
    public TodoListResponse createTodoList(TodoListRequest request) {
        TodoList todoList = new TodoList();
        todoList.setTitle(request.getTitle());
        todoList.setDescription(request.getDescription());
        todoList.setColor(request.getColor());
        todoList.setPosition(request.getPosition() != null ? request.getPosition().toString() : "0");

        TodoList savedTodoList = todoRepository.save(todoList);

        return mapToResponse(savedTodoList);
    }

    private TodoListResponse mapToResponse(TodoList savedTodoList) {
        return TodoListResponse.builder()
                .listId(String.valueOf(savedTodoList.getListId()))
                .title(savedTodoList.getTitle())
                .description(savedTodoList.getDescription())
                .color(savedTodoList.getColor())
                .position(savedTodoList.getPosition())
                .isArchived(savedTodoList.getIsArchived())
                .createdAt(savedTodoList.getCreatedAt())
                .updatedAt(savedTodoList.getUpdatedAt())
                .itemCount(savedTodoList.getTodoItems().size())
                .build();
    }

}
