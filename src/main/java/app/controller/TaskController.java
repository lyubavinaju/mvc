package app.controller;

import app.dao.TaskListRepository;
import app.dao.TaskRepository;
import app.model.Task;
import app.model.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @GetMapping("/")
    public String addTaskListPage(Model map) {
        map.addAttribute("newTaskList", new TaskList());
        map.addAttribute("taskLists", taskListRepository.findAll());
        map.addAttribute("newTask", new Task());
        return "index";
    }

    @PostMapping("/add-task-list")
    public String addTaskList(@ModelAttribute("newTaskList") TaskList newTaskList, Model model) {
        try {
            taskListRepository.save(newTaskList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Cannot add task list. " + e.getMessage());
            return "index";
        }

        return "redirect:/";
    }

    @PostMapping("delete-task-list/{taskListId}")
    public String deleteTaskList(@PathVariable("taskListId") Long taskListId, Model model) {
        try {
            taskListRepository.deleteById(taskListId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Cannot delete task list. " + e.getMessage());
            return "index";
        }
        return "redirect:/";
    }

    @PostMapping("/task-list/{taskListId}/add-task")
    public String addTask(@PathVariable("taskListId") Long taskListId, @ModelAttribute("newTask") Task newTask,
                          Model model) {
        try {
            TaskList taskList = taskListRepository.findById(taskListId).orElseThrow();
            newTask.setTaskList(taskList);
            taskRepository.save(newTask);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Cannot add task. " + e.getMessage());
            return "index";
        }
        return "redirect:/";
    }

    @PostMapping("/done-task/{taskId}")
    public String doneTask(@PathVariable("taskId") Long taskId, Model model) {
        try {
            Task task = taskRepository.findById(taskId).orElseThrow();
            task.setDone(true);
            taskRepository.save(task);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Cannot mark task as done. " + e.getMessage());
            return "index";
        }

        return "redirect:/";
    }
}
