package com.bedmil.todolist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bedmil.todolist.entity.Todo;
import com.bedmil.todolist.repository.TodoRepository;

@Service
public class TodoService {
	
	@Autowired
	private TodoRepository todoRepository;
	
	// 모든 투두 task 조회
	public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }
	
	// id로 개별 task 조회
	public Todo getTodoById(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        return todo.orElse(null);
    }
	
	// 새로운 투두 task 삽입
	public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }
	
	// 투두 task 수정
	public Todo updateTodo(Long id, Todo todo) {
        todo.setId(id);
        return todoRepository.save(todo);
    }
	
	// 특정 아이디로 투두 task 삭제
	public void deleteTodoById(Long id) {
        todoRepository.deleteById(id);
    }
	
}
