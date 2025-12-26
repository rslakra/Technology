package com.rslakra.thymeleafsidebarsversion.task.controller;

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
@RequestMapping("/{version}/tasks")
public class TaskController extends AbstractController {

    private final TaskService taskService;

    /**
     * @param taskService
     */
    @Autowired
    public TaskController(final TaskService taskService) {
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
            List<Task> tasks = new ArrayList<>();
            if (keyword == null) {
                taskService.findByType("TASK").forEach(tasks::add);
            } else {
                taskService.findByTypeAndNameContainsIgnoreCase("TASK", keyword).forEach(tasks::add);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("tasks", tasks);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return version + "/task/listTasks";
    }

    /**
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addTask(@PathVariable("version") String version, Model model) {
        Task task = new Task();
        task.setCompleted(false);
        task.setType("TASK");
        model.addAttribute("task", task);
        model.addAttribute("pageTitle", "Create Task");

        return version + "/task/editTask";
    }

    /**
     * @param task
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/save")
    public String saveTask(@PathVariable("version") String version, Task task, RedirectAttributes redirectAttributes) {
        try {
            if (task.getType() == null || task.getType().isEmpty()) {
                task.setType("TASK");
            }
            task = taskService.create(task);
            redirectAttributes.addFlashAttribute("message", "The task has been saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
        }

        return "redirect:/" + version + "/tasks";
    }

    /**
     * @param id
     * @param model
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("version") String version, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Task task = taskService.findById(id);
            model.addAttribute("task", task);
            model.addAttribute("pageTitle", "Edit Task (ID: " + id + ")");

            return version + "/task/editTask";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/" + version + "/tasks";
        }
    }

    /**
     * @param id
     * @param model
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/delete/{id}")
    public String deleteTutorial(@PathVariable("version") String version, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            taskService.delete(id);
            redirectAttributes.addFlashAttribute("message",
                    "The task with id: " + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/" + version + "/tasks";
    }

    /**
     * @param id
     * @param completed
     * @param model
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/{id}/completed/{status}")
    public String updateTaskStatus(@PathVariable("version") String version, @PathVariable("id") Long id, @PathVariable("status") boolean completed,
                                   Model model, RedirectAttributes redirectAttributes) {
        try {
            taskService.updateTaskStatus(id, completed);
            String status = completed ? "completed" : "pending";
            String message = "The tasks id=" + id + " has been " + status;
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/" + version + "/tasks";
    }
}
