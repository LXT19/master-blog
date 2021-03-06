package com.blog.controller.admin;

import com.blog.bean.*;
import com.blog.service.BlogService;
import com.blog.service.TagService;
import com.blog.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogsController {

    @Autowired
    private BlogService blogService;


    @Autowired
    private TypeService typeService;


    @Autowired
    private TagService tagService;

    public void setTypeAndTag(Model model) {

        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());

    }

    @GetMapping("/blogs")
    public String list_Blog(Model model, @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum) {

        PageHelper.startPage(pageNum, 5);

        List<BlogWithType> allBlog = blogService.getAllBlog();

        PageInfo<BlogWithType> pageInfo = new PageInfo<>(allBlog);

        model.addAttribute("pageInfo", pageInfo);

        setTypeAndTag(model);

        return "admin/blogs";

    }

    @PostMapping("/blogs/search")
    public String search_Blog(BlogQuery blogQuery, Model model, @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum) {

        PageHelper.startPage(pageNum, 5);

        blogQuery.setIsRecomend();

        List<BlogWithType> allBlog = blogService.searchBlog(blogQuery);

        PageInfo<BlogWithType> pageInfo = new PageInfo<>(allBlog);

        model.addAttribute("pageInfo", pageInfo);

        setTypeAndTag(model);

        return "admin/blogs";

    }

    @GetMapping("/blogs/input")
    public String input(Model model){

        setTypeAndTag(model);

        return "admin/blogs-input";

    }

    @PostMapping("/saveBlogs")
    public String saveBlog(Blog blog, RedirectAttributes attributes, HttpSession session){

        blog.setUser((User) session.getAttribute("user"));

        //??????blog???type??????
        blog.setType(typeService.getType(blog.getType().getId()));
        //??????blog???typeID
        blog.setTypeId(blog.getType().getId());
        //????????????id
        blog.setUserId(blog.getUser().getId());
        System.out.println(blog);
        //????????????
        int i=blogService.saveBlog(blog);
        if(i==1){
            attributes.addFlashAttribute("message","????????????");
        }
        else
            attributes.addFlashAttribute("message","????????????");

        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/edit")
    public String editBlog(@PathVariable Long id,Model model){
        System.out.println("???????????????");
        ShowBlog blog=blogService.showblog(id);
        System.out.println(blog.getDescription());
        setTypeAndTag(model);
        model.addAttribute("blog",blog);
        return "admin/blogs-update";
    }


    @PostMapping("/blogs/update")
    public String updateBlog(ShowBlog blog,RedirectAttributes redirectAttributes){

        int i=blogService.updateBlog(blog);
        if(i==1)
            redirectAttributes.addFlashAttribute("message","????????????");
        else
            redirectAttributes.addFlashAttribute("message","????????????");
        return "redirect:/admin/blogs";

    }

    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable Long id,RedirectAttributes redirectAttributes){
        int i=blogService.deleteBlog(id);
        if(i==1){
            redirectAttributes.addFlashAttribute("message","????????????");
        }
        else
            redirectAttributes.addFlashAttribute("message","????????????");
        return "redirect:/admin/blogs";
    }





}
