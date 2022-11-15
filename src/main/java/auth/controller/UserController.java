package auth.controller;

import auth.model.UserEntity;
import auth.model.dto.UserDataDTO;
import auth.model.dto.UserResponseDTO;
import auth.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  public final UserService userService;

  public final ModelMapper modelMapper;

  @PostMapping("/signing")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong!"), @ApiResponse(code = 422, message = "Invalid username/password supplied!")})
  public String login(@ApiParam("Username") @RequestParam String username, @ApiParam("Password") @RequestParam String password) {
    return userService.signIn(username, password);
  }

  @PostMapping("/signup")
  @ApiOperation(value = "${UserController.signup}")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"), @ApiResponse(code = 403, message = "Access denied"), @ApiResponse(code = 422, message = "Username is already in use")})
  public String signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
    return userService.signUp(modelMapper.map(user, UserEntity.class));
  }

  @DeleteMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.delete}", authorizations = {@Authorization(value = "apiKey")})
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), @ApiResponse(code = 403, message = "Access denied"), @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("Username") @PathVariable String username) {
    userService.delete(username);
    return username;
  }

  @GetMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.search}", response = UserResponseDTO.class, authorizations = {@Authorization(value = "apiKey")})
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), @ApiResponse(code = 403, message = "Access denied"), @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
    return modelMapper.map(userService.search(username), UserResponseDTO.class);
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
  @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = {@Authorization(value = "apiKey")})
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), @ApiResponse(code = 403, message = "Access denied"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO whoAmI(HttpServletRequest req) {
    return modelMapper.map(userService.whoAmI(req), UserResponseDTO.class);
  }

  @GetMapping("/refresh")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
  public String refresh(HttpServletRequest req) {
    return userService.refresh(req.getRemoteUser());
  }

}
