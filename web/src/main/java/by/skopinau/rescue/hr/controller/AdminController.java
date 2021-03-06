package by.skopinau.rescue.hr.controller;

import by.skopinau.rescue.hr.dto.CreateEmployeeRequest;
import by.skopinau.rescue.hr.dto.SearchRequest;
import by.skopinau.rescue.hr.dto.UpdateUserRequest;
import by.skopinau.rescue.hr.entity.Employee;
import by.skopinau.rescue.hr.entity.State;
import by.skopinau.rescue.hr.entity.User;
import by.skopinau.rescue.hr.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    public static final int PAGE_SIZE = 10;
    private final EmployeeService employeeService;
    private final StateService stateService;
    private final RanksLogService ranksLogService;
    private final PositionsLogService positionsLogService;
    private final UserService userService;

    @Autowired
    public AdminController(EmployeeService employeeService, StateService stateService, RanksLogService ranksLogService, PositionsLogService positionsLogService, UserService userService) {
        this.employeeService = employeeService;
        this.stateService = stateService;
        this.ranksLogService = ranksLogService;
        this.positionsLogService = positionsLogService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showIndexView() {
        return "admin";
    }

    @GetMapping(path = "/admin/users/{page}")
    public String showUsers(@PathVariable("page") int pageNumber, Model model) {
        List<User> users = userService.findAllPageable(pageNumber - 1, PAGE_SIZE);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("users", users);
        return "viewUsersAdmin";
    }

    @GetMapping(path = "/admin/user/delete/{userId}")
    public String deleteUser(@PathVariable("userId") int userId) {
        userService.deleteById(userId);
        return "redirect:/admin/users/1";
    }

    @GetMapping(path = "/admin/user/update/{userId}")
    public String showUpdateUserView(@PathVariable("userId") int userId, Model model) {
        User user = userService.findById(userId);
        List<String> rolesNames = userService.getRolesNames(user);
        model.addAttribute("user", user);
        model.addAttribute("rolesNames", rolesNames);
        return "updateUser";
    }

    @PostMapping(path = "/admin/user/update/{userId}")
    public String updateUser(@PathVariable("userId") int userId, UpdateUserRequest updateUserRequest) {
        userService.updateUser(userId, updateUserRequest);
        return "redirect:/admin/users/1";
    }


    @GetMapping(path = "/admin/employees/{page}")
    public String showEmployees(@PathVariable("page") int pageNumber, Model model) {
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("employees", employeeService.findAllPageable(pageNumber - 1, PAGE_SIZE));
        return "viewEmployeesAdmin";
    }

    @GetMapping(path = "/admin/employee/{employeeId}")
    public String showOneEmployee(@PathVariable("employeeId") int employeeId, Model model) {
        Employee employee = employeeService.findById(employeeId);
        model.addAttribute("employee", employee);
        model.addAttribute("rankLogs", ranksLogService.findByEmployee(employee));
        model.addAttribute("positionLogs", positionsLogService.findByEmployee(employee));
        return "viewOneEmployeeAdmin";
    }

    @GetMapping("/admin/employees/create")
    public String showCreateEmployeeView(Model model) {
        return "createEmployee";
    }

    @PostMapping("/admin/employees/create")
    public String createNewEmployee(CreateEmployeeRequest createEmployeeRequest) {
        employeeService.createEmployee(createEmployeeRequest);
        return "redirect:/admin/employees/1";
    }

    @GetMapping(path = "/admin/employee/delete/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") int employeeId) {
        employeeService.deleteById(employeeId);
        return "redirect:/admin/employees/1";
    }

    @GetMapping(path = "/admin/employee/update/{employeeId}")
    public String showUpdateEmployeeView(@PathVariable("employeeId") int employeeId, Model model) {
        Employee employee = employeeService.findById(employeeId);
        model.addAttribute("employee", employee);
        return "updateEmployee";
    }

    @PostMapping(path = "/admin/employee/update/{employeeId}")
    public String updateEmployee(@PathVariable("employeeId") int employeeId, CreateEmployeeRequest createEmployeeRequest) {
        employeeService.updateEmployee(employeeId, createEmployeeRequest);
        return "redirect:/admin/employee/{employeeId}";
    }

    @GetMapping(path = "/admin/state")
    public String showState(Model model) {
        List<State> list = stateService.findAll();
        Map<State, Integer> mapWithActualPositionsAmounts = new HashMap<>();
        Map<State, Integer> mapWithFreePositionsAmounts = new HashMap<>();
        for (State state: list) {
            mapWithActualPositionsAmounts.put(state, stateService.getActualPositionAmount(state));
            mapWithFreePositionsAmounts.put(state, stateService.getFreePositionAmount(state));
        }
        model.addAttribute("stateList", list);
        model.addAttribute("actualAmounts", mapWithActualPositionsAmounts);
        model.addAttribute("freeAmounts", mapWithFreePositionsAmounts);
        return "viewStateAdmin";
    }

    @GetMapping(path = "/admin/ranks-log/{page}")
    public String showRanksLog(@PathVariable("page") int pageNumber, Model model) {
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("rankLogs", ranksLogService.findAllPageable(pageNumber - 1, PAGE_SIZE));
        return "viewRanksLogAdmin";
    }

    @GetMapping(path = "/admin/positions-log/{page}")
    public String showPositionsLog(@PathVariable("page") int pageNumber, Model model) {
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("positionLogs", positionsLogService.findAllPageable(pageNumber - 1, PAGE_SIZE));
        return "viewPositionsLogAdmin";
    }

    @GetMapping(path = "/admin/search/results/{page}")
    public String showSearchResults(@PathVariable("page") int pageNumber, Model model, SearchRequest request) {
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("employees", employeeService.searchAllPageable(request,pageNumber - 1, PAGE_SIZE));
        return "viewEmployeesAdmin";
    }
}