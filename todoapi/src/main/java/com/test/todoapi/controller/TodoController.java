package com.test.todoapi.controller;

import com.henheang.securityapi.controller.BaseController;
import com.test.todoapi.payload.TodoListRequest;
import com.test.todoapi.service.TodoService;
import com.test.todoapi.util.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todo/v1")
@RequiredArgsConstructor
public class TodoController extends BaseController {

    private final TodoService todoService;

    @PostMapping("/create")
    public Object createTodoList(@Valid @RequestBody TodoListRequest request) {
     todoService.createTodoList(request);
        return ok();
    }
}