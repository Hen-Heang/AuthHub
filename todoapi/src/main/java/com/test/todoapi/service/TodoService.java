package com.test.todoapi.service;

import com.test.todoapi.payload.TodoListRequest;
import com.test.todoapi.payload.TodoListResponse;
import jakarta.validation.Valid;

public interface TodoService {

    TodoListResponse createTodoList(@Valid TodoListRequest request, Long userId);
}
