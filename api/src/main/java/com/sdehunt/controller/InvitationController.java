//package com.sdehunt.controller;
//
//import com.sdehunt.security.CurrentUser;
//import com.sdehunt.security.UserPrincipal;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/invitation")
//public class InvitationController {
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @RequestMapping(method = RequestMethod.PUT, path = "/{inviter}/{status}")
//    public void acceptOrDecline(
//            @CurrentUser UserPrincipal currentUser,
//            @PathVariable("inviter") String inviter,
//            @PathVariable("status") String status
//    ) {
//        // TODO implement
//    }
//}
