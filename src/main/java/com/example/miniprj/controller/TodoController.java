package com.example.miniprj.controller;

import com.example.miniprj.entity.Todo;
import com.example.miniprj.repository.TodoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // TASK 3 - Bảo mật cơ bản: tránh NullPointerException
    private boolean isSessionInvalid(HttpSession session) {
        return session.getAttribute("ownerName") == null;
    }

    // TASK 1 - READ: Hiển thị danh sách toàn bộ task
    // TASK 3 - SESSION: Kiểm tra session trước khi vào trang
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        if (isSessionInvalid(session)) {
            return "redirect:/welcome";
        }
        List<Todo> todos = todoRepository.findAll();
        model.addAttribute("todos", todos);
        return "index";
    }

    // TASK 1 - CREATE: Hiển thị form thêm mới
    @GetMapping("/todos/create")
    public String showCreateForm(HttpSession session, Model model) {
        if (isSessionInvalid(session)) return "redirect:/welcome";
        model.addAttribute("todo", new Todo());
        model.addAttribute("formAction", "/todos/create");
        model.addAttribute("isEdit", false);
        return "form";
    }

    // TASK 1 - CREATE: Xử lý submit thêm mới + Validation
    @PostMapping("/todos/create")
    public String createTodo(@Valid @ModelAttribute("todo") Todo todo,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (isSessionInvalid(session)) return "redirect:/welcome";

        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/todos/create");
            model.addAttribute("isEdit", false);
            return "form";
        }

        todoRepository.save(todo);
        redirectAttributes.addFlashAttribute("message", "success.create");
        return "redirect:/";
    }

    // TASK 2 - UPDATE: Hiển thị form sửa, nạp dữ liệu cũ vào form
    @GetMapping("/todos/edit/{id}")
    public String showEditForm(@PathVariable Long id, HttpSession session, Model model) {
        if (isSessionInvalid(session)) return "redirect:/welcome";

        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("todo", optionalTodo.get());
        model.addAttribute("formAction", "/todos/update");
        model.addAttribute("isEdit", true);
        return "form";
    }

    // TASK 2 - UPDATE: Xử lý submit cập nhật + giữ nguyên Validation
    @PostMapping("/todos/update")
    public String updateTodo(@Valid @ModelAttribute("todo") Todo todo,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (isSessionInvalid(session)) return "redirect:/welcome";

        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/todos/update");
            model.addAttribute("isEdit", true);
            return "form";
        }

        todoRepository.save(todo);
        redirectAttributes.addFlashAttribute("message", "success.update");
        return "redirect:/";
    }

    // TASK 2 - DELETE: Xóa task theo ID + Flash Attributes
    @GetMapping("/todos/delete/{id}")
    public String deleteTodo(@PathVariable Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (isSessionInvalid(session)) return "redirect:/welcome";

        try {
            if (!todoRepository.existsById(id)) {
                redirectAttributes.addFlashAttribute("error", "error.notfound");
                return "redirect:/";
            }
            todoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "success.delete");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "error.delete");
        }
        return "redirect:/";
    }
}
