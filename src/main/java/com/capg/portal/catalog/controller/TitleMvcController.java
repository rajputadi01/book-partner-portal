package com.capg.portal.catalog.controller;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.catalog.service.TitleService;
import com.capg.portal.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/web/titles")
public class TitleMvcController
{
    private final TitleService titleService;

    public TitleMvcController(TitleService titleService)
    {
        this.titleService = titleService;
    }

    // --- LEVEL 3: OPERATIONS MENU ---
    @GetMapping("/operations")
    public String showOperationsMenu()
    {
        return "titles/title-operations";
    }

    // --- 1. getAllTitles (GET) ---
    @GetMapping("/get-all")
    public String getAllTitles(Model model)
    {
        model.addAttribute("titles", titleService.getAllTitles());
        model.addAttribute("pageTitle", "All Titles (GET /titles)");
        return "titles/title-list";
    }

    // --- 2. getTitleById (GET) ---
    @GetMapping("/get-by-id")
    public String requestTitleIdForGet(Model model)
    {
        model.addAttribute("actionUrl", "/web/titles/get-by-id/result");
        model.addAttribute("formTitle", "Execute: getTitleById (GET)");
        return "titles/title-id-request";
    }

    @GetMapping("/get-by-id/result")
    public String getTitleByIdResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("title", titleService.getTitleById(id));
        return "titles/title-details";
    }

    // --- 3. createTitle (POST) ---
    @GetMapping("/create")
    public String showCreateForm(Model model)
    {
        Title title = new Title();
        title.setPublisher(new Publisher()); // Prevent Thymeleaf NullPointer

        model.addAttribute("title", title);
        model.addAttribute("formTitle", "Execute: createTitle (POST)");
        model.addAttribute("actionUrl", "/web/titles/create/save");
        model.addAttribute("isUpdate", false);
        return "titles/title-form";
    }

    @PostMapping("/create/save")
    public String createTitleSave(@Valid @ModelAttribute("title") Title title, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("formTitle", "Execute: createTitle (POST)");
            model.addAttribute("actionUrl", "/web/titles/create/save");
            model.addAttribute("isUpdate", false);
            return "titles/title-form";
        }
        
        applyDefaultsAndClean(title);

        try 
        {
            titleService.createTitle(title);
        } 
        catch (Exception e) 
        {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("formTitle", "Execute: createTitle (POST)");
            model.addAttribute("actionUrl", "/web/titles/create/save");
            model.addAttribute("isUpdate", false);
            return "titles/title-form";
        }
        return "redirect:/web/titles/get-all";
    }

    // --- 4. updateTitle (PUT) ---
    @GetMapping("/update")
    public String requestTitleIdForUpdate(Model model)
    {
        model.addAttribute("actionUrl", "/web/titles/update/form");
        model.addAttribute("formTitle", "Execute: updateTitle (PUT) - Enter ID");
        return "titles/title-id-request";
    }

    @GetMapping("/update/form")
    public String showUpdateForm(@RequestParam("id") String id, Model model)
    {
        Title existingTitle = titleService.getTitleById(id);
        if (existingTitle.getPublisher() == null) existingTitle.setPublisher(new Publisher());

        model.addAttribute("title", existingTitle);
        model.addAttribute("formTitle", "Execute: updateTitle (PUT)");
        model.addAttribute("actionUrl", "/web/titles/update/save");
        model.addAttribute("isUpdate", true);
        return "titles/title-form";
    }

    @PostMapping("/update/save")
    public String updateTitleSave(@Valid @ModelAttribute("title") Title title, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("formTitle", "Execute: updateTitle (PUT)");
            model.addAttribute("actionUrl", "/web/titles/update/save");
            model.addAttribute("isUpdate", true);
            return "titles/title-form";
        }

        applyDefaultsAndClean(title);
        titleService.updateTitle(title.getTitleId(), title);
        return "redirect:/web/titles/get-all";
    }

    // --- 5. patchTitle (PATCH) ---
    @GetMapping("/patch")
    public String requestTitleIdForPatch(Model model)
    {
        model.addAttribute("actionUrl", "/web/titles/patch/form");
        model.addAttribute("formTitle", "Execute: patchTitle (PATCH) - Enter ID");
        return "titles/title-id-request";
    }

    @GetMapping("/patch/form")
    public String showPatchForm(@RequestParam("id") String id, Model model)
    {
        Title existingTitle = titleService.getTitleById(id);
        if (existingTitle.getPublisher() == null) existingTitle.setPublisher(new Publisher());

        model.addAttribute("title", existingTitle);
        model.addAttribute("formTitle", "Execute: patchTitle (PATCH)");
        model.addAttribute("actionUrl", "/web/titles/patch/save");
        model.addAttribute("isUpdate", true);
        return "titles/title-form";
    }

    @PostMapping("/patch/save")
    public String patchTitleSave(@ModelAttribute("title") Title title)
    {
        applyDefaultsAndClean(title);
        titleService.patchTitle(title.getTitleId(), title);
        return "redirect:/web/titles/get-all";
    }

    // --- 6, 7, 8, 9. Filter Methods (GET) ---
    @GetMapping("/filter/{type}")
    public String requestFilterParam(@PathVariable("type") String type, Model model)
    {
        model.addAttribute("actionUrl", "/web/titles/filter/" + type + "/result");
        
        String inputType = "text";
        String paramName = type;
        if (type.equals("price")) { paramName = "maxPrice"; inputType = "number"; model.addAttribute("step", "0.01"); }
        if (type.equals("publisher")) { paramName = "pubId"; }
        if (type.equals("date")) { paramName = "beforeDate"; inputType = "datetime-local"; }

        model.addAttribute("paramName", paramName);
        model.addAttribute("inputType", inputType);
        model.addAttribute("formTitle", "Filter Titles by " + type.toUpperCase());
        return "titles/title-single-param-request";
    }

    @GetMapping("/filter/price/result")
    public String filterPriceResult(@RequestParam("maxPrice") Double maxPrice, Model model)
    {
        model.addAttribute("titles", titleService.getTitlesByPriceLessThan(maxPrice));
        model.addAttribute("pageTitle", "Titles Priced Below $" + maxPrice);
        return "titles/title-list";
    }

    @GetMapping("/filter/type/result")
    public String filterTypeResult(@RequestParam("type") String type, Model model)
    {
        model.addAttribute("titles", titleService.getTitlesByType(type));
        model.addAttribute("pageTitle", "Titles of Genre: " + type);
        return "titles/title-list";
    }

    @GetMapping("/filter/publisher/result")
    public String filterPublisherResult(@RequestParam("pubId") String pubId, Model model)
    {
        model.addAttribute("titles", titleService.getTitlesByPublisher(pubId));
        model.addAttribute("pageTitle", "Titles from Publisher ID: " + pubId);
        return "titles/title-list";
    }

    @GetMapping("/filter/date/result")
    public String filterDateResult(@RequestParam("beforeDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeDate, Model model)
    {
        model.addAttribute("titles", titleService.getTitlesPublishedBefore(beforeDate));
        model.addAttribute("pageTitle", "Titles Published Before: " + beforeDate.toLocalDate().toString());
        return "titles/title-list";
    }

    // --- 10 - 15. Relational Data (GET) ---
    @GetMapping("/relational/{endpoint}")
    public String requestTitleIdForRelational(@PathVariable("endpoint") String endpoint, Model model)
    {
        String endpointPath = endpoint.replace("-", "/"); // e.g. title-authors
        model.addAttribute("actionUrl", "/web/titles/" + endpointPath + "/result");
        model.addAttribute("formTitle", "Execute: get" + endpoint.substring(0, 1).toUpperCase() + endpoint.substring(1).replace("-", "") + "ByTitleId");
        return "titles/title-id-request";
    }

    @GetMapping("/publisher/result")
    public String getPublisherResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("targetId", id);
        try {
            model.addAttribute("publisher", titleService.getPublisherByTitleId(id));
        } catch (ResourceNotFoundException e) {
            model.addAttribute("publisher", null); // Safe handling for Self-Published / Null pub_id
        }
        return "titles/title-publisher";
    }

    @GetMapping("/sales/result")
    public String getSalesResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("sales", titleService.getSalesByTitleId(id));
        model.addAttribute("targetId", id);
        return "titles/title-sales";
    }

    @GetMapping("/royalties/result")
    public String getRoyaltiesResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("royalties", titleService.getRoyaltiesByTitleId(id));
        model.addAttribute("targetId", id);
        return "titles/title-royalties";
    }

    @GetMapping("/title-authors/result")
    public String getTitleAuthorsResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("titleAuthors", titleService.getTitleAuthorsByTitleId(id));
        model.addAttribute("targetId", id);
        return "titles/title-titleauthors";
    }

    @GetMapping("/authors/result")
    public String getAuthorsResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("authors", titleService.getAuthorsByTitleId(id));
        model.addAttribute("targetId", id);
        return "titles/title-authors";
    }

    @GetMapping("/stores/result")
    public String getStoresResult(@RequestParam("id") String id, Model model)
    {
        model.addAttribute("stores", titleService.getStoresByTitleId(id));
        model.addAttribute("targetId", id);
        return "titles/title-stores";
    }

    // Helper for Business Defaults and Foreign Key safety
    private void applyDefaultsAndClean(Title title)
    {
        if (title.getType() == null || title.getType().trim().isEmpty()) {
            title.setType("UNDECIDED");
        }
        if (title.getPubdate() == null) {
            title.setPubdate(LocalDateTime.now());
        }
        if (title.getPublisher() != null && (title.getPublisher().getPubId() == null || title.getPublisher().getPubId().trim().isEmpty())) {
            title.setPublisher(null);
        }
    }
}