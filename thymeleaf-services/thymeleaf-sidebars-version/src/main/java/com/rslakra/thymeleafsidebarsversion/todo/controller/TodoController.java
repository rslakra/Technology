package com.rslakra.thymeleafsidebarsversion.todo.controller;

import com.rslakra.thymeleafsidebarsversion.framework.controller.AbstractController;
import com.rslakra.thymeleafsidebarsversion.task.persistence.entity.Task;
import com.rslakra.thymeleafsidebarsversion.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/{version}/todos")
public class TodoController extends AbstractController {

    private final TaskService taskService;

    /**
     * @param taskService
     */
    @Autowired
    public TodoController(final TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * @param model
     * @param keyword
     * @return
     */
    @GetMapping
    public String getAll(@PathVariable("version") String version, Model model, @Param("keyword") String keyword) {
        try {
            List<Task> todos = new ArrayList<>();
            if (keyword == null) {
                taskService.findByType("TODO").forEach(todos::add);
            } else {
                taskService.findByTypeAndNameContainsIgnoreCase("TODO", keyword).forEach(todos::add);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("todos", todos);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return version + "/todo/listTodos";
    }

    /**
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addTodo(@PathVariable("version") String version, Model model) {
        Task todo = new Task();
        todo.setCompleted(false);
        todo.setType("TODO");
        model.addAttribute("todo", todo);
        model.addAttribute("pageTitle", "Create Todo");

        return version + "/todo/editTodo";
    }

    /**
     * @param todo
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/save")
    public String saveTodo(@PathVariable("version") String version, Task todo, RedirectAttributes redirectAttributes) {
        try {
            if (todo.getType() == null || todo.getType().isEmpty()) {
                todo.setType("TODO");
            }
            todo = taskService.create(todo);
            redirectAttributes.addFlashAttribute("message", "The todo has been saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
        }

        return "redirect:/" + version + "/todos";
    }

    /**
     * @param id
     * @param model
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/{id}")
    public String editTodo(@PathVariable("version") String version, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Task todo = taskService.findById(id);
            model.addAttribute("todo", todo);
            model.addAttribute("pageTitle", "Edit Todo (ID: " + id + ")");

            return version + "/todo/editTodo";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/" + version + "/todos";
        }
    }

    /**
     * @param id
     * @param model
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("version") String version, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            taskService.delete(id);
            redirectAttributes.addFlashAttribute("message",
                    "The todo with id: " + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/" + version + "/todos";
    }

    /**
     * @param id
     * @param completed
     * @param model
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/{id}/completed/{status}")
    public String updateTodoStatus(@PathVariable("version") String version, @PathVariable("id") Long id, @PathVariable("status") boolean completed,
                                   Model model, RedirectAttributes redirectAttributes) {
        try {
            taskService.updateTaskStatus(id, completed);
            String status = completed ? "completed" : "pending";
            String message = "The todo id=" + id + " has been " + status;
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/" + version + "/todos";
    }
}

