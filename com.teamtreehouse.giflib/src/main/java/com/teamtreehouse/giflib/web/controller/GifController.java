package com.teamtreehouse.giflib.web.controller;

import com.teamtreehouse.giflib.model.Gif;
import com.teamtreehouse.giflib.service.CategoryService;
import com.teamtreehouse.giflib.service.GifService;
import com.teamtreehouse.giflib.validator.GifValidator;
import com.teamtreehouse.giflib.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GifController {

    @Autowired
    private GifService gifService;

    @Autowired
    private CategoryService categoryService;

    @InitBinder("gif")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new GifValidator());
    }


    @RequestMapping("/")
    public String listGifs(Model model){
        List<Gif> gifs = gifService.findAll();

        model.addAttribute("gifs",gifs);

        return "gif/index";
    }

    @RequestMapping("/gifs/{gifId}")
    public String gifDetails(@PathVariable Long gifId, Model model){

        Gif gif = gifService.findById(gifId);

        model.addAttribute("gif",gif);
        return "gif/details";
    }
    @RequestMapping("/gifs/{gifId}.gif")
    @ResponseBody
    public byte[] gifImage(@PathVariable Long gifId){
        return gifService.findById(gifId).getBytes();
    }

    @RequestMapping("/favorites")
    public String favorites(Model model) {
        // TODO: Get list of all GIFs marked as favorite
        List<Gif> faves = new ArrayList<>();
        for(Gif gif: gifService.findAll()){
            if (gif.isFavorite()){
                faves.add(gif);
            }
        }

        model.addAttribute("gifs",faves);
        model.addAttribute("username","Chris Ramacciotti"); // Static username
        return "gif/favorites";
    }

    @RequestMapping(value = "gif/{gifId}/favorite", method = RequestMethod.POST)
    public String toggleFavorites(@PathVariable Long gifId, HttpServletRequest request){
        Gif gif = gifService.findById(gifId);
        gifService.toggleFavorite(gif);
        return String.format("redirect:%s", request.getHeader("referer"));
    }


    //Upload a new Gif
    @RequestMapping(value = "/gifs", method = RequestMethod.POST)
    public String addGif(@Valid Gif gif, BindingResult result, RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.gif",result);
            redirectAttributes.addFlashAttribute("gif",gif);
            return "redirect:/upload";
        }
        gifService.save(gif,gif.getFile());

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Gif succesfully uploaded", FlashMessage.Status.SUCCESS));

        return String.format("redirect:/gifs/%s",gif.getId());
    }

    @RequestMapping(value = "/gifs/{gifId}", method = RequestMethod.POST)
    public String updateGif(@Valid Gif gif, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.gif",result);
            redirectAttributes.addFlashAttribute("gif",gif);
            return String.format("redirect:/gifs/%s/edit",gif.getId());
        }
        gifService.save(gif,gif.getFile());
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("GIF updated!", FlashMessage.Status.SUCCESS));
        return String.format("redirect:/gifs/%s",gif.getId());
    }

    //Form for uploading a new GIF
    @RequestMapping(value = "/upload")
    public String formNewGif(Model model){
        if(!model.containsAttribute("gif")) {
            model.addAttribute("gif",new Gif());
        }
        model.addAttribute("categories",categoryService.findAll());
        model.addAttribute("action","/gifs");
        model.addAttribute("heading","Upload");
        model.addAttribute("submit","Add");

        return "gif/form";
    }

    //Form for editing anexisting GIF
    @RequestMapping(value = "/gifs/{gifId}/edit")
    public String formEditGif(@PathVariable Long gifId, Model model){
        if(!model.containsAttribute("gif")){
            model.addAttribute("gif",gifService.findById(gifId));
        }

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("action",String.format("/gifs/%s", gifId));
        model.addAttribute("heading", "Edit gif");
        model.addAttribute("submit", "Update");
        return "gif/form";
    }

    @RequestMapping(value = "/gifs/{gifId}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable Long gifId, RedirectAttributes redirectAttributes){
        Gif gif = gifService.findById(gifId);
        gifService.delete(gif);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Gif deleted successfully", FlashMessage.Status.SUCCESS));
        return "redirect:/";
    }
}
