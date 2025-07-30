package com.test.todoapi.service;


import com.test.todoapi.domain.TodoList;
import com.test.todoapi.payload.TodoListRequest;
import com.test.todoapi.repository.TodoRepository;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;

    @Override
    public void createTodoList(TodoListRequest request) {

        TodoList todoList = new TodoList();
        todoList.setUser(userPrincipal);
        todoList.setTitle(request.getTitle());
        todoList.setDescription(request.getDescription());
        todoList.setColor(request.getColor());
        todoList.setPosition(request.getDescription());
        todoRepository.save(todoList);
    }
}
