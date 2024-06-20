package com.shubham.controller;
import com.shubham.exception.ResourceNotFoundException;
import com.shubham.model.User;
import com.shubham.service.UserService;
import com.shubham.userImpliments.UserImpliments;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired

    private UserService userService;

    @Autowired

    private UserImpliments userImpliments;

    @RequestMapping(value = "/Callbackcheck", method = RequestMethod.POST)
    public ResponseEntity<Map<String , String>> checkFunction(@RequestBody  Map<String, Object> callbackRequest)
    {
        System.out.println(callbackRequest);
        String message =userImpliments.findStatusMessage(callbackRequest);
        Map<String, String> response = new HashMap<>();
        response.put("statusMessage", message);
        response.put("statusCode", "200");
        response.put("version", "v1");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
   @RequestMapping(value = "/metadata", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> checkmetadata(@RequestBody Map<String, Map<String, Object>> comboApplicationAttributes) {
       System.out.println("metedata");
        Map<String, Object> response=this.userImpliments.patchComboAttributes(comboApplicationAttributes);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        try{
            System.out.println(user);
            User user1 = userService.createUser(user);
            return new ResponseEntity<>(user1, HttpStatus.CREATED);
        }catch (Exception e)
        {
            System.out.println("message "+ e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/findby/{userID}", method = RequestMethod.GET)
    public ResponseEntity<User> findByUserID(@PathVariable String userID) throws ResourceNotFoundException {
        User user = userService.findByUserID(userID);
        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

    @RequestMapping(value = "/findlAllUser", method = RequestMethod.GET)
    public ResponseEntity<List<User>> findAll() {
        List<User> allUser = userService.findAllUser();
        if (allUser != null) {
            return new ResponseEntity<>(allUser, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(allUser, HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/update/{userID}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable String userID, @RequestBody User user) {
        User user1 = userService.updateUserByID(userID, user);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/delete/{userID}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, String>> deleteUserByID(@PathVariable String userID) throws ResourceNotFoundException {
        Map<String, String> response = userService.deleteUserByID(userID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

