package com.test.todoapi.controller;

import com.henheang.securityapi.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/todo/v1")
@RequiredArgsConstructor


public class TodoController extends BaseController {


}
