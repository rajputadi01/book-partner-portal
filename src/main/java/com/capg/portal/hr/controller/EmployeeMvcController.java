package com.capg.portal.hr.controller;

import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/employees")
public class EmployeeMvcController
{
    private final EmployeeService employeeService;

    public EmployeeMvcController(EmployeeService employeeService)
    {
        this.employeeService = employeeService;
    }

    // --- LEVEL 3: OPERATIONS MENU ---
    @GetMapping("/operations")
    public String showOperationsMenu()
    {
        return "employees/employee-operations";
    }

    // --- 1. getAllEmployees (GET) ---
    @GetMapping("/get-all")
    public String getAllEmployees(Model model)
    {
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("pageTitle", "All Employees (GET /employees)");
        return "employees/employee-list";
    }

    // --- 2. getEmployeeById (GET) ---
    @GetMapping("/get-by-id")
    public String requestEmployeeIdForGet(Model model)
    {
        model.addAttribute("actionUrl", "/web/employees/get-by-id/result");
        model.addAttribute("formTitle", "Execute: getEmployeeById (GET)");
        return "employees/employee-id-request";
    }

    @GetMapping("/get-by-id/result")
    public String getEmployeeByIdResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "employees/employee-details";
    }

    // --- 3. createEmployee (POST) ---
    @GetMapping("/create")
    public String showCreateForm(Model model)
    {
        Employee employee = new Employee();
        employee.setJob(new Job());
        employee.setPublisher(new Publisher());

        model.addAttribute("employee", employee);
        model.addAttribute("formTitle", "Execute: createEmployee (POST)");
        model.addAttribute("actionUrl", "/web/employees/create/save");
        model.addAttribute("isUpdate", false);
        return "employees/employee-form";
    }

    @PostMapping("/create/save")
    public String createEmployeeSave(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("formTitle", "Execute: createEmployee (POST)");
            model.addAttribute("actionUrl", "/web/employees/create/save");
            model.addAttribute("isUpdate", false);
            return "employees/employee-form";
        }
        try 
        {
            employeeService.createEmployee(employee);
        } 
        catch (Exception e) 
        {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("formTitle", "Execute: createEmployee (POST)");
            model.addAttribute("actionUrl", "/web/employees/create/save");
            model.addAttribute("isUpdate", false);
            return "employees/employee-form";
        }
        return "redirect:/web/employees/get-all";
    }

    // --- 4. updateEmployee (PUT) ---
    @GetMapping("/update")
    public String requestEmployeeIdForUpdate(Model model)
    {
        model.addAttribute("actionUrl", "/web/employees/update/form");
        model.addAttribute("formTitle", "Execute: updateEmployee (PUT) - Enter ID");
        return "employees/employee-id-request";
    }

    @GetMapping("/update/form")
    public String showUpdateForm(@RequestParam("id") String id, Model model)
    {
        Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee.getJob() == null) existingEmployee.setJob(new Job());
        if (existingEmployee.getPublisher() == null) existingEmployee.setPublisher(new Publisher());

        model.addAttribute("employee", existingEmployee);
        model.addAttribute("formTitle", "Execute: updateEmployee (PUT)");
        model.addAttribute("actionUrl", "/web/employees/update/save");
        model.addAttribute("isUpdate", true);
        return "employees/employee-form";
    }

    @PostMapping("/update/save")
    public String updateEmployeeSave(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("formTitle", "Execute: updateEmployee (PUT)");
            model.addAttribute("actionUrl", "/web/employees/update/save");
            model.addAttribute("isUpdate", true);
            return "employees/employee-form";
        }
        try
        {
            employeeService.updateEmployee(employee.getEmpId(), employee);
        }
        catch (Exception e)
        {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("formTitle", "Execute: updateEmployee (PUT)");
            model.addAttribute("actionUrl", "/web/employees/update/save");
            model.addAttribute("isUpdate", true);
            return "employees/employee-form";
        }
        return "redirect:/web/employees/get-all";
    }

    // --- 5. patchEmployee (PATCH) ---
    @GetMapping("/patch")
    public String requestEmployeeIdForPatch(Model model)
    {
        model.addAttribute("actionUrl", "/web/employees/patch/form");
        model.addAttribute("formTitle", "Execute: patchEmployee (PATCH) - Enter ID");
        return "employees/employee-id-request";
    }

    @GetMapping("/patch/form")
    public String showPatchForm(@RequestParam("id") String id, Model model)
    {
        Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee.getJob() == null) existingEmployee.setJob(new Job());
        if (existingEmployee.getPublisher() == null) existingEmployee.setPublisher(new Publisher());

        model.addAttribute("employee", existingEmployee);
        model.addAttribute("formTitle", "Execute: patchEmployee (PATCH)");
        model.addAttribute("actionUrl", "/web/employees/patch/save");
        model.addAttribute("isUpdate", true);
        return "employees/employee-form";
    }

    @PostMapping("/patch/save")
    public String patchEmployeeSave(@ModelAttribute("employee") Employee employee, Model model)
    {
        try
        {
            employeeService.patchEmployee(employee.getEmpId(), employee);
        }
        catch (Exception e)
        {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("formTitle", "Execute: patchEmployee (PATCH)");
            model.addAttribute("actionUrl", "/web/employees/patch/save");
            model.addAttribute("isUpdate", true);
            return "employees/employee-form";
        }
        return "redirect:/web/employees/get-all";
    }

    // --- 6, 7, 8. Filter Methods (GET) ---
    @GetMapping("/filter/{type}")
    public String requestFilterParam(@PathVariable("type") String type, Model model)
    {
        model.addAttribute("actionUrl", "/web/employees/filter/" + type + "/result");
        String label = type.equals("job") ? "jobId" : type.equals("job-level") ? "maxLvl" : "pubId";
        model.addAttribute("paramName", label);
        model.addAttribute("inputType", type.equals("pubId") ? "text" : "number");
        model.addAttribute("formTitle", "Filter by " + type.toUpperCase().replace("-", " "));
        return "employees/employee-single-param-request";
    }

    @GetMapping("/filter/job/result")
    public String filterJobResult(@RequestParam("jobId") Short jobId, Model model)
    {
        model.addAttribute("employees", employeeService.getEmployeesByJobId(jobId));
        model.addAttribute("pageTitle", "Employees with Job ID: " + jobId);
        return "employees/employee-list";
    }

    @GetMapping("/filter/job-level/result")
    public String filterJobLevelResult(@RequestParam("maxLvl") Integer maxLvl, Model model)
    {
        model.addAttribute("employees", employeeService.getEmployeesBelowJobLevel(maxLvl));
        model.addAttribute("pageTitle", "Employees with Level < " + maxLvl);
        return "employees/employee-list";
    }

    @GetMapping("/filter/publisher/result")
    public String filterPublisherResult(@RequestParam("pubId") String pubId, Model model)
    {
        model.addAttribute("employees", employeeService.getEmployeesByPublisher(pubId));
        model.addAttribute("pageTitle", "Employees at Publisher ID: " + pubId);
        return "employees/employee-list";
    }

    // --- 9, 10, 11. Relational Intelligence (GET) ---
    @GetMapping("/relational/{endpoint}")
    public String requestEmployeeIdForRelational(@PathVariable("endpoint") String endpoint, Model model)
    {
        String endpointPath = endpoint.replace("-", "/"); // Handle publisher-titles -> publisher/titles
        model.addAttribute("actionUrl", "/web/employees/" + endpointPath + "/result");
        model.addAttribute("formTitle", "Execute Relational Hop: /employees/{id}/" + endpointPath);
        return "employees/employee-id-request";
    }

    @GetMapping("/job/result")
    public String getEmployeeJobResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("job", employeeService.getJobByEmployeeId(id));
        model.addAttribute("targetId", id);
        return "employees/employee-job";
    }

    @GetMapping("/publisher/result")
    public String getEmployeePublisherResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("publisher", employeeService.getPublisherByEmployeeId(id));
        model.addAttribute("targetId", id);
        return "employees/employee-publisher";
    }

    @GetMapping("/publisher/titles/result")
    public String getEmployeePublisherTitlesResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("titles", employeeService.getTitlesByEmployeePublisher(id));
        model.addAttribute("targetId", id);
        return "employees/employee-publisher-titles";
    }
}