package com.bedmil.todolist;

import com.bedmil.todolist.entity.Todo;
import com.bedmil.todolist.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodolistApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void setup() {
        // 테스트 전 데이터베이스 정리
        todoRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldCreateTodo() throws Exception {
        String newTodo = "{\"title\": \"New Todo\", \"memo\": \"This is a new todo\", \"completed\": false}";

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Todo"))
                .andExpect(jsonPath("$.memo").value("This is a new todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldGetAllTodos() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setMemo("Test Memo");
        todo.setCompleted(false);
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Test Todo")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldGetTodoById() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setMemo("Test Memo");
        todo.setCompleted(false);
        todo = todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/" + todo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.memo").value("Test Memo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldUpdateTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setMemo("Test Memo");
        todo.setCompleted(false);
        todo = todoRepository.save(todo);

        String updatedTodo = "{\"title\": \"Updated Todo\", \"memo\": \"Updated Memo\", \"completed\": true}";

        mockMvc.perform(put("/api/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTodo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Todo"))
                .andExpect(jsonPath("$.memo").value("Updated Memo"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldDeleteTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setMemo("Test Memo");
        todo.setCompleted(false);
        todo = todoRepository.save(todo);

        mockMvc.perform(delete("/api/todos/" + todo.getId()))
                .andExpect(status().isOk());

        boolean exists = todoRepository.existsById(todo.getId());
        assertFalse(exists);

        mockMvc.perform(get("/api/todos/" + todo.getId()))
                .andExpect(status().isNotFound());
    }
}
