package com.sdehunt.controller;

import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.User;
import com.sdehunt.dto.CreateUserDTO;
import com.sdehunt.exception.UserNotFoundException;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private SolutionRepository solutions;

    @Autowired
    private UserRepository users;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<User> getAll() {
        return users.getAll();
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public User create(@RequestBody CreateUserDTO createUserRequest) {
        User user = new User()
                .setEmail(createUserRequest.getEmail())
                .setGithub(createUserRequest.getGithub())
                .setLinkedIn(createUserRequest.getLinkedIn())
                .setNickname(createUserRequest.getNickname());

        return users.create(user);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public User get(@PathVariable("userId") String userId) {
        return users.get(userId).orElseThrow(UserNotFoundException::new);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public User update(@PathVariable("userId") String userId, @RequestBody CreateUserDTO updateUserRequest) {
        User user = new User()
                .setId(userId)
                .setEmail(updateUserRequest.getEmail())
                .setGithub(updateUserRequest.getGithub())
                .setLinkedIn(updateUserRequest.getLinkedIn())
                .setNickname(updateUserRequest.getNickname());

        return users.update(user);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("userId") String userId) {
        users.delete(userId);
    }

    @RequestMapping(path = "/{userId}/solutions", method = RequestMethod.GET)
    public List<Solution> userSolutions(@PathVariable("userId") String userId) {
        return solutions.forUser(userId);
    }

}
