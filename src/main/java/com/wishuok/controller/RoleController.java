package com.wishuok.controller;

import com.wishuok.pojo.Role;
import com.wishuok.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    // 调用url 示例：http://localhost/role/getRoleById/216e8f7c28a846a78de10d7b8e1acf34
    @RequestMapping(value = "/getRoleById/{id}", method = RequestMethod.GET)
    public String findById(@PathVariable("id") String id, Model model){
        Role role = roleService.findRoleById(id);
        model.addAttribute("role", role);
        return "showRole";
    }
}
