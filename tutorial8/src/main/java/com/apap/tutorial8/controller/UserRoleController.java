package com.apap.tutorial8.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apap.tutorial8.model.PasswordModel;
import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user, RedirectAttributes redirectAttrs) {
		String message = "";
		if(validatePassword(user.getPassword())) {
			userService.addUser(user);
			message = "User Berhasil ditambah";	
		}
		else {
			message = "Password tidak sesuai ketentuan";
		}
		redirectAttrs.addFlashAttribute("message", message);
		return "redirect:/";
	}
	
	@RequestMapping("/updatePassword")
	public String updatePassword() {
		return "update-password";
	}
	
	public boolean validatePassword(String password) {
		if (password.length()>=8 && Pattern.compile("[0-9]").matcher(password).find() &&  Pattern.compile("[a-zA-Z]").matcher(password).find())  {
			return true;
		}
		else {
			return false;
		}
	}
	
	@RequestMapping(value="/passwordSubmit",method=RequestMethod.POST)
	public String updatePasswordSubmit(@ModelAttribute PasswordModel pass, Model model, RedirectAttributes redirectAttrs) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserRoleModel user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		String message = "";
		
		if (pass.getConPassword().equals(pass.getNewPassword())) {
			
			if (passwordEncoder.matches(pass.getOldPassword(), user.getPassword())) {
				if (validatePassword(pass.getNewPassword())) {
					userService.changePassword(user, pass.getNewPassword());
					message = "password berhasil diubah";
				}
				else {
					message = "password baru anda belum sesuai ketentuan: lebih dari 8 karakter, mengandung minimal 1 huruf dan 1 angka";
				}
				
			}
			else {
				message = "password lama anda salah";
			}
			
		}
		else {
			message = "password baru tidak sesuai";
		}
		
		redirectAttrs.addFlashAttribute("message", message);
		return "redirect:/user/updatePassword";
	}
	
}