package br.com.challange.service;

import br.com.challange.models.User;
import br.com.challange.repository.UserRepository;
import br.com.challange.response.ApiResponse;
import br.com.challange.security.jwt.JwtResponse;
import br.com.challange.security.jwt.JwtUtils;
import br.com.challange.security.services.UserDetailsImpl;
import br.com.challange.service.criteria.UserCriteria;
import br.com.challange.util.Constants;
import br.com.challange.util.MsgSystem;
import br.com.challange.util.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public ApiResponse<User> saveUser(User user){
        return verification(user, HttpStatus.CREATED);
    }

    public ApiResponse<User> getUserById(Integer id){
        ApiResponse<User> userApiResponse = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it ->
                        userApiResponse.of(HttpStatus.OK, MsgSystem.sucGet(Constants.USUARIO),it),
                () ->
                        userApiResponse.of(HttpStatus.NOT_FOUND, MsgSystem.errGet(Constants.USUARIO))
        );

        return userApiResponse;

    }


    public ApiResponse<JwtResponse> login(User loginRequest) {
        ApiResponse<JwtResponse> userApiResponse = new ApiResponse<>();
        Optional<User> user = repository.findUserByUsername(loginRequest.getUsername());

        if (user.isEmpty())
            return userApiResponse.of(HttpStatus.BAD_REQUEST, MsgSystem.errLogin());

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return userApiResponse.of(HttpStatus.OK, tokenResponse(authentication, user.get()));
        } catch (Exception e) {
            return userApiResponse.of(HttpStatus.BAD_REQUEST, MsgSystem.errLogin());
        }

    }

    public ApiResponse<User> getUserLogged() {
        ApiResponse<User> response = new ApiResponse<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ANONYMOUS")){
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            response.of(HttpStatus.OK, "Usuário logado encontrado com sucesso", repository.findUserByUsername(userDetails.getUsername()).get());
            return response;
        }
        return response.of(HttpStatus.NOT_FOUND, "Usuário não está logado");
    }

    private Specification<User> createSpecification(UserCriteria userCriteria){
        Specification<User> specification = Specification.where(null);

        if(userCriteria.getKeyword() != null)
            specification = specification
                    .or(UserCriteria.searchByUsername(userCriteria.getKeyword()));

        return specification;
    }

    public ApiResponse<User> verification(User user, HttpStatus status) {
        ApiResponse<User> response = new ApiResponse<>();
        String msgSuc = MsgSystem.sucCreate(Constants.USUARIO);

        Object[][] args = {
            {user.getUsername(), "Usuário"}
        };
        String msgErr = Validation.isEmptyFields(args);
        if(!msgErr.isEmpty())
            return response.of(HttpStatus.BAD_REQUEST, msgErr);
        if (repository.findUserByUsername(user.getUsername()).isPresent())
            return response.of(HttpStatus.BAD_REQUEST,
                    "Usuário "+user.getUsername()+" já encontra-se cadastrado!");

        if (status == HttpStatus.OK)
            if (Validation.isEmptyOrNull(user.getId()) || !repository.existsById(user.getId()))
                return response.of(HttpStatus.NOT_FOUND,
                        msgSuc = MsgSystem.sucCreate(Constants.USUARIO));
        user.setPassword(passwordEncoder.encode(user.getUsername()));
        User userSaved = repository.save(user);
        return response.of(status, msgSuc, userSaved);
    }


    private JwtResponse tokenResponse(Authentication authentication, User user) {
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                user.getPermission()
        );
    }
}
