package com.capg.portal.creator.controller;

import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/publishers")
public class PublisherMvcController
{
    private final PublisherService publisherService;

    public PublisherMvcController(PublisherService publisherService)
    {
        this.publisherService = publisherService;
    }

    
    @GetMapping("/operations")
    public String showOperationsMenu()
    {
        return "publishers/publisher-operations";
    }

    // --- 1. getAllPublishers (GET) ---
    @GetMapping("/get-all")
    public String getAllPublishers(Model model)
    {
        model.addAttribute("publishers", publisherService.getAllPublishers());
        model.addAttribute("pageTitle", "All Publishers (GET /publishers)");
        
        return "publishers/publisher-list";
    }

    // --- 2. getPublisherById (GET) ---
    @GetMapping("/get-by-id")
    public String requestPubIdForGet(Model model)
    {
        model.addAttribute("actionUrl", "/web/publishers/get-by-id/result");
        model.addAttribute("formTitle", "Execute: getPublisherById (GET)");
        
        return "publishers/publisher-id-request";
    }

    @GetMapping("/get-by-id/result")
    public String getPublisherByIdResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("publisher", publisherService.getPublisherById(id));
        
        return "publishers/publisher-details";
    }

    // --- 3. createPublisher (POST) ---
    @GetMapping("/create")
    public String showCreateForm(Model model)
    {
        model.addAttribute("publisher", new Publisher());
        model.addAttribute("formTitle", "Execute: createPublisher (POST)");
        model.addAttribute("actionUrl", "/web/publishers/create/save");
        model.addAttribute("isUpdate", false);
        
        return "publishers/publisher-form";
    }

    @PostMapping("/create/save")
    public String createPublisherSave(@Valid @ModelAttribute("publisher") Publisher publisher, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("formTitle", "Execute: createPublisher (POST)");
            model.addAttribute("actionUrl", "/web/publishers/create/save");
            model.addAttribute("isUpdate", false);
            return "publishers/publisher-form";
        }
        try 
        {
            publisherService.createPublisher(publisher);
        } 
        catch (Exception e) 
        {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("formTitle", "Execute: createPublisher (POST)");
            model.addAttribute("actionUrl", "/web/publishers/create/save");
            model.addAttribute("isUpdate", false);
            return "publishers/publisher-form";
        }
        
        return "redirect:/web/publishers/get-all";
    }

    // --- 4. updatePublisher (PUT) ---
    @GetMapping("/update")
    public String requestPubIdForUpdate(Model model)
    {
        model.addAttribute("actionUrl", "/web/publishers/update/form");
        model.addAttribute("formTitle", "Execute: updatePublisher (PUT) - Enter ID");
        
        return "publishers/publisher-id-request";
    }

    @GetMapping("/update/form")
    public String showUpdateForm(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("publisher", publisherService.getPublisherById(id));
        model.addAttribute("formTitle", "Execute: updatePublisher (PUT)");
        model.addAttribute("actionUrl", "/web/publishers/update/save");
        model.addAttribute("isUpdate", true);
        
        return "publishers/publisher-form";
    }

    @PostMapping("/update/save")
    public String updatePublisherSave(@Valid @ModelAttribute("publisher") Publisher publisher, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("formTitle", "Execute: updatePublisher (PUT)");
            model.addAttribute("actionUrl", "/web/publishers/update/save");
            model.addAttribute("isUpdate", true);
            return "publishers/publisher-form";
        }
        publisherService.updatePublisher(publisher.getPubId(), publisher);
        
        return "redirect:/web/publishers/get-all";
    }

    // --- 5. patchPublisher (PATCH) ---
    @GetMapping("/patch")
    public String requestPubIdForPatch(Model model)
    {
        model.addAttribute("actionUrl", "/web/publishers/patch/form");
        model.addAttribute("formTitle", "Execute: patchPublisher (PATCH) - Enter ID");
        
        return "publishers/publisher-id-request";
    }

    @GetMapping("/patch/form")
    public String showPatchForm(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("publisher", publisherService.getPublisherById(id));
        model.addAttribute("formTitle", "Execute: patchPublisher (PATCH)");
        model.addAttribute("actionUrl", "/web/publishers/patch/save");
        model.addAttribute("isUpdate", true);
        
        return "publishers/publisher-form";
    }

    @PostMapping("/patch/save")
    public String patchPublisherSave(@ModelAttribute("publisher") Publisher publisher)
    {
        publisherService.patchPublisher(publisher.getPubId(), publisher);
        
        return "redirect:/web/publishers/get-all";
    }

    // --- 6, 7, 8. Filter Methods (GET) ---
    @GetMapping("/filter/{type}")
    public String requestStringFilter(@PathVariable("type") String type, Model model)
    {
        model.addAttribute("actionUrl", "/web/publishers/filter/" + type + "/result");
        model.addAttribute("paramName", type);
        model.addAttribute("formTitle", "Filter by " + type.toUpperCase());
        
        return "publishers/publisher-string-request";
    }

    @GetMapping("/filter/city/result")
    public String filterCityResult(@RequestParam("city") String city, Model model)
    {
        model.addAttribute("publishers", publisherService.getPublishersByCity(city));
        model.addAttribute("pageTitle", "Filtered by City: " + city);
        
        return "publishers/publisher-list";
    }

    @GetMapping("/filter/state/result")
    public String filterStateResult(@RequestParam("state") String state, Model model)
    {
        model.addAttribute("publishers", publisherService.getPublishersByState(state));
        model.addAttribute("pageTitle", "Filtered by State: " + state);
        
        return "publishers/publisher-list";
    }

    @GetMapping("/filter/country/result")
    public String filterCountryResult(@RequestParam("country") String country, Model model)
    {
        model.addAttribute("publishers", publisherService.getPublishersByCountry(country));
        model.addAttribute("pageTitle", "Filtered by Country: " + country);
        
        return "publishers/publisher-list";
    }

    // --- 9, 10, 11, 12. Relational Hops (GET) ---
    @GetMapping("/relational/{endpoint}")
    public String requestPubIdForRelational(@PathVariable("endpoint") String endpoint, Model model)
    {
        model.addAttribute("actionUrl", "/web/publishers/" + endpoint + "/result");
        model.addAttribute("formTitle", "Execute: get" + endpoint.substring(0, 1).toUpperCase() + endpoint.substring(1) + "ByPublisherId");
        
        return "publishers/publisher-id-request";
    }

    @GetMapping("/employees/result")
    public String getEmployeesResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("employees", publisherService.getEmployeesByPublisherId(id));
        model.addAttribute("targetId", id);
        
        return "publishers/publisher-employees";
    }

    @GetMapping("/titles/result")
    public String getTitlesResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("titles", publisherService.getTitlesByPublisherId(id));
        model.addAttribute("targetId", id);
        
        return "publishers/publisher-titles";
    }

    @GetMapping("/authors/result")
    public String getAuthorsResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("authors", publisherService.getAuthorsByPublisherId(id));
        model.addAttribute("targetId", id);
        
        return "publishers/publisher-authors";
    }

    @GetMapping("/stores/result")
    public String getStoresResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("stores", publisherService.getStoresByPublisherId(id));
        model.addAttribute("targetId", id);
        
        return "publishers/publisher-stores";
    }
}