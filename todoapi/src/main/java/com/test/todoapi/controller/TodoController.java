package com.test.todoapi.controller;

import com.henheang.securityapi.controller.BaseController;
import com.henheang.securityapi.service.UserService;
import com.test.todoapi.payload.TodoListRequest;
import com.test.todoapi.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/todo/v1")
@RequiredArgsConstructor
public class TodoController extends BaseController {

    private final TodoService todoService;
    private final UserService userService;

    @PostMapping("/create")
    public Object createTodoList(@Valid @RequestBody TodoListRequest request) {
        todoService.createTodoList( request);
        return ok();

        }
    }
