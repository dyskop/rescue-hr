package by.skopinau.rescue.hr.controller;

import by.skopinau.rescue.hr.dto.SearchDto;
import by.skopinau.rescue.hr.entity.Employee;
import by.skopinau.rescue.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static by.skopinau.rescue.hr.config.WebConfig.PAGE_SIZE;

@Controller
public class SearchLineController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/search")
    public String showSearchResults(@RequestParam(defaultValue = "1") int page,
                                    Model model, SearchDto dto) {
        List<Employee> employees = employeeService.search(dto, page - 1, PAGE_SIZE);
        model.addAttribute("page", page);
        model.addAttribute("employees", employees);

        return "employees";
    }
}