package com.article.controller;

import com.article.model.UserDetails;
import com.article.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<UserDetails>> userDetails() {
		List<UserDetails> userDetails = userService.getUserDetails();
		return new ResponseEntity<List<UserDetails>>(userDetails, HttpStatus.OK);
	}

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity < String > persistPerson(@RequestBody UserDetails userDetails) {
        userService.addUserDetails(userDetails);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login")
    public ResponseEntity < String > persistMessage(@RequestBody UserDetails userDetails, HttpServletRequest request) {
        UserDetails userDetails1 = userService.getUserByEmail(userDetails.getEmail());

        if(userDetails1 != null && userDetails1.getPassword().equals(userDetails.getPassword())){
            @SuppressWarnings("unchecked")
            List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
            if (messages == null) {
                messages = new ArrayList<String>();
                request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
            }
            messages.add(userDetails.getEmail());
            request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);

            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/destroy")
    public ResponseEntity < String >  destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
