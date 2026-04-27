package com.example.miniprj.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SessionController {

    // TASK 3 - SESSION: Hiển thị trang nhập tên chủ sở hữu
    @GetMapping("/welcome")
    public String showWelcomePage(HttpSession session) {
        // Nếu đã có tên trong session rồi thì redirect thẳng về danh sách
        if (session.getAttribute("ownerName") != null) {
            return "redirect:/";
        }
        return "welcome";
    }

    // TASK 3 - SESSION: Nhận tên, lưu vào HttpSession, redirect về /
    @PostMapping("/welcome")
    public String saveOwnerName(@RequestParam("ownerName") String ownerName,
                                HttpSession session,
                                Model model) {

        if (ownerName == null || ownerName.trim().isEmpty()) {
            model.addAttribute("error", "nameRequired");
            return "welcome";
        }

        session.setAttribute("ownerName", ownerName.trim());

        return "redirect:/";
    }

    // TASK 3 - SESSION: Đăng xuất / Xóa session
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/welcome";
    }
}
