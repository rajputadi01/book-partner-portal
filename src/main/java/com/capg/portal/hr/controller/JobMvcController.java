package com.capg.portal.hr.controller;

import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.service.JobService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/jobs")
public class JobMvcController
{
    private final JobService jobService;

    public JobMvcController(JobService jobService)
    {
        this.jobService = jobService;
    }

    // --- LEVEL 3: THE OPERATIONS MENU ---
    @GetMapping("/operations")
    public String showOperationsMenu()
    {
        return "jobs/job-operations";
    }

    // --- 1. getAllJobs (GET) ---
    @GetMapping("/get-all")
    public String getAllJobs(Model model)
    {
        model.addAttribute("jobs", jobService.getAllJobs());
        model.addAttribute("pageTitle", "All Jobs (GET /jobs)");
        
        return "jobs/job-list";
    }

    // --- 2. getJobById (GET) ---
    @GetMapping("/get-by-id")
    public String requestJobIdForGet(Model model)
    {
        model.addAttribute("actionUrl", "/web/jobs/get-by-id/result");
        model.addAttribute("formTitle", "Execute: getJobById (GET)");
        
        return "jobs/job-id-request";
    }

    @GetMapping("/get-by-id/result")
    public String getJobByIdResult(@RequestParam("id") Short id, Model model)
    {
        model.addAttribute("job", jobService.getJobById(id));
        
        return "jobs/job-details";
    }

    // --- 3. createJob (POST) ---
    @GetMapping("/create")
    public String showCreateForm(Model model)
    {
        model.addAttribute("job", new Job());
        model.addAttribute("formTitle", "Execute: createJob (POST)");
        model.addAttribute("actionUrl", "/web/jobs/create/save");
        model.addAttribute("isUpdate", false);
        
        return "jobs/job-form";
    }

    @PostMapping("/create/save")
    public String createJobSave(@Valid @ModelAttribute("job") Job job, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("formTitle", "Execute: createJob (POST)");
            model.addAttribute("actionUrl", "/web/jobs/create/save");
            model.addAttribute("isUpdate", false);
            return "jobs/job-form";
        }
        jobService.createJob(job);
        
        return "redirect:/web/jobs/get-all";
    }

    // --- 4. updateJob (PUT) ---
    @GetMapping("/update")
    public String requestJobIdForUpdate(Model model)
    {
        model.addAttribute("actionUrl", "/web/jobs/update/form");
        model.addAttribute("formTitle", "Execute: updateJob (PUT) - Enter ID to Update");
        
        return "jobs/job-id-request";
    }

    @GetMapping("/update/form")
    public String showUpdateForm(@RequestParam("id") Short id, Model model)
    {
        model.addAttribute("job", jobService.getJobById(id));
        model.addAttribute("formTitle", "Execute: updateJob (PUT)");
        model.addAttribute("actionUrl", "/web/jobs/update/save");
        model.addAttribute("isUpdate", true);
        
        return "jobs/job-form";
    }

    @PostMapping("/update/save")
    public String updateJobSave(@Valid @ModelAttribute("job") Job job, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("formTitle", "Execute: updateJob (PUT)");
            model.addAttribute("actionUrl", "/web/jobs/update/save");
            model.addAttribute("isUpdate", true);
            return "jobs/job-form";
        }
        jobService.updateJob(job.getJobId(), job);
        
        return "redirect:/web/jobs/get-all";
    }

    // --- 5. patchJob (PATCH) ---
    @GetMapping("/patch")
    public String requestJobIdForPatch(Model model)
    {
        model.addAttribute("actionUrl", "/web/jobs/patch/form");
        model.addAttribute("formTitle", "Execute: patchJob (PATCH) - Enter ID to Patch");
        
        return "jobs/job-id-request";
    }

    @GetMapping("/patch/form")
    public String showPatchForm(@RequestParam("id") Short id, Model model)
    {
        model.addAttribute("job", jobService.getJobById(id));
        model.addAttribute("formTitle", "Execute: patchJob (PATCH) - Partial Update");
        model.addAttribute("actionUrl", "/web/jobs/patch/save");
        model.addAttribute("isUpdate", true);
        
        return "jobs/job-form";
    }

    @PostMapping("/patch/save")
    public String patchJobSave(@ModelAttribute("job") Job job)
    {
        jobService.patchJob(job.getJobId(), job);
        
        return "redirect:/web/jobs/get-all";
    }

    // --- 6. getJobsBetween (GET Filter) ---
    @GetMapping("/filter")
    public String showFilterForm()
    {
        return "jobs/job-filter";
    }

    @GetMapping("/filter/result")
    public String getJobsBetweenResult(@RequestParam("minLvl") Integer minLvl, @RequestParam("maxLvl") Integer maxLvl, Model model)
    {
        model.addAttribute("jobs", jobService.getJobsBetween(minLvl, maxLvl));
        model.addAttribute("pageTitle", "Filtered Jobs (" + minLvl + " to " + maxLvl + ")");
        
        return "jobs/job-list";
    }

    // --- 7. getEmployeesByJobId (GET) ---
    @GetMapping("/employees")
    public String requestJobIdForEmployees(Model model)
    {
        model.addAttribute("actionUrl", "/web/jobs/employees/result");
        model.addAttribute("formTitle", "Execute: getEmployeesByJobId (GET)");
        
        return "jobs/job-id-request";
    }

    @GetMapping("/employees/result")
    public String getEmployeesByJobIdResult(@RequestParam("id") Short id, Model model)
    {
        model.addAttribute("employees", jobService.getEmployeesByJobId(id));
        model.addAttribute("targetId", id);
        
        return "jobs/job-employees";
    }

    // --- 8. getPublishersByJobId (GET) ---
    @GetMapping("/publishers")
    public String requestJobIdForPublishers(Model model)
    {
        model.addAttribute("actionUrl", "/web/jobs/publishers/result");
        model.addAttribute("formTitle", "Execute: getPublishersByJobId (GET)");
        
        return "jobs/job-id-request";
    }

    @GetMapping("/publishers/result")
    public String getPublishersByJobIdResult(@RequestParam("id") Short id, Model model)
    {
        model.addAttribute("publishers", jobService.getPublishersByJobId(id));
        model.addAttribute("targetId", id);
        
        return "jobs/job-publishers";
    }

    // --- 9. assignEmployeeToJob (PATCH/PUT) ---
    @GetMapping("/assign")
    public String showAssignForm()
    {
        return "jobs/job-assign";
    }

    @PostMapping("/assign/save")
    public String assignEmployeeToJobSave(@RequestParam("jobId") Short jobId, @RequestParam("empId") String empId, @RequestParam(value = "jobLvl", required = false) Integer jobLvl, Model model)
    {
        try
        {
            Employee updatedEmp = jobService.assignEmployeeToJob(jobId, empId, jobLvl);
            model.addAttribute("successMessage", "Successfully assigned " + updatedEmp.getFname() + " to Job ID " + jobId);
        }
        catch(Exception e)
        {
            model.addAttribute("errorMessage", e.getMessage());
        }
        
        return "jobs/job-assign";
    }
}