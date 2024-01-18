package cloudproject.cloudproject.controller;

import cloudproject.cloudproject.entity.FormData;
import cloudproject.cloudproject.repository.FormDataDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "https://frontend.mantalepersonal.com")
@RequestMapping(path = "")
public class FormController {

    @Autowired
    private final FormDataDAO formDataDAO;

    public FormController(FormDataDAO formDataDAO) {
        this.formDataDAO = formDataDAO;
    }

    @GetMapping("/")
    public String displayForm(Model model) {
        System.out.println("inside displayForm");
        model.addAttribute("formData", new FormData());
        return "index";
    }

    @PostMapping("/submit")
    public String saveForm(@ModelAttribute FormData formData) {
        try {
            System.out.println("inside saveForm");
            formDataDAO.saveFormData(formData.getFirstName(), formData.getLastName(), formData.getReason(), formData.getSelectedDate());
            System.out.println("inside saveForm after save");
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();  // Print the exception details for debugging
            return "error";
        }
    }

    @GetMapping("/approvedRequests")
    public String showApprovedRequests(Model model) {
        System.out.println("inside showApprovedRequests");
        List<FormData> approvedRequestsList = formDataDAO.findByApproved(true);
        model.addAttribute("approvedRequests", approvedRequestsList);
        return "approvedRequests";
    }

    @GetMapping("/awaitingApproval")
    public String showAwaitingApproval(Model model) {
        System.out.println("inside showAwaitingApproval");
        List<FormData> awaitingApprovalList = formDataDAO.findByApproved(false);
        model.addAttribute("awaitingApprovalList", awaitingApprovalList);
        return "awaitingApproval";
    }

    @PostMapping("/approveRequest")
    public String approveRequest(@ModelAttribute FormData formData) {
        try {
            System.out.println("inside approveRequest");
            formDataDAO.approveFormData(formData.getId());
            System.out.println("inside approveRequest after approval");
            return "redirect:/awaitingApproval";
        } catch (Exception e) {
            e.printStackTrace();  // Print the exception details for debugging
            return "error";
        }
    }

    @GetMapping("/createRequest")
    public String showCreateRequest(Model model) {
        System.out.println("inside showCreateRequest");
        model.addAttribute("formData", new FormData());
        return "createRequest";
    }

}

