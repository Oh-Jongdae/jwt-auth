package auth.controller;

import auth.model.UserEntity;
import auth.model.dto.UserDataDTO;
import auth.model.dto.UserResponseDTO;
import auth.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sso")
@RequiredArgsConstructor
@Log4j2
public class UserController {

  public final UserService userService;

  public final ModelMapper modelMapper;

  @PostMapping("/signing")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong!"), @ApiResponse(code = 422, message = "Invalid username/password supplied!")})
  public String login(@ApiParam("Username") @RequestParam String username, @ApiParam("Password") @RequestParam String password) {
    log.info("Signing authenticated user!");
    return userService.signIn(username, password);
  }

  @PostMapping("/signup")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"), @ApiResponse(code = 403, message = "Access denied"), @ApiResponse(code = 422, message = "Username is already in use")})
  public String signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
    log.info("Signup authenticated user!");
    return userService.signUp(modelMapper.map(user, UserEntity.class));
  }

  @DeleteMapping(value = "/delete/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong"), @ApiResponse(code = 403, message = "Access denied"), @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("Username") @PathVariable String username) {
    log.info("Delete authenticated user!");
    userService.delete(username);
    return username;
  }

  @GetMapping(value = "/search/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong"), @ApiResponse(code = 403, message = "Access denied"), @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
    log.info("Searching authenticated user!");
    return modelMapper.map(userService.search(username), UserResponseDTO.class);
  }

  @GetMapping("/refresh")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
  public String refresh(HttpServletRequest req) {
    log.info("Refreshing JWT token!");
    return userService.refresh(req.getRemoteUser());
  }

}
