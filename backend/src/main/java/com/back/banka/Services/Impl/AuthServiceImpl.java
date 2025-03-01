package com.back.banka.Services.Impl;
import com.back.banka.Dtos.RequestDto.LoginRequestDto;
import com.back.banka.Dtos.ResponseDto.GetAllUsersResponseDto;
import com.back.banka.Dtos.ResponseDto.TokenResponseDto;
import com.back.banka.Enums.TokenType;
import com.back.banka.Exceptions.Custom.CustomAuthenticationException;
import com.back.banka.Exceptions.Custom.InvalidCredentialExceptions;
import com.back.banka.Model.Tokens;
import com.back.banka.Model.User;
import com.back.banka.Repository.ITokenRepository;
import com.back.banka.Repository.UserRepository;
import com.back.banka.Services.IServices.IUserService;
import com.back.banka.Utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements IUserService {


    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtService;
    private final ITokenRepository tokenRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtUtil jwtService, ITokenRepository tokenRepository, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.userDetailsService = userDetailsService;
    }


    /**
     * Método para autenticar un cliente.
     * Este método realiza los siguientes pasos:
     * Se crea el objeto de autenticación a partir de las credenciales proporcionadas (email y password)
     * Se autentica el usuario usando el AuthenticationManager
     * Se guarda la autenticación en el contexto de seguridad
     * Si todo es exitoso, se genera un token JWT que será utilizado para futuras peticiones
     *
     * @param loginRequestDto
     * @return LoginResponseDto
     * @throws InvalidCredentialExceptions   Si las credenciales del usuario son incorrectas o el proceso de autenticación falla.
     * @throws CustomAuthenticationException Si ocurre un error inesperado durante la autenticación.
     */


    @Override
    public TokenResponseDto authenticate(LoginRequestDto loginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword());
        try {
            Authentication authentication =
                    authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(loginRequestDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            revokedUsersToken(user);

            String token = this.jwtService.generateAccessToken(loginRequestDto.getEmail());
            String refreshToken = this.jwtService.generateRefreshToken(loginRequestDto.getEmail());
            this.jwtService.diagnosticCheck(token);
            logger.info("REfresh token generado");
            saveUserToken(user, token);
            return TokenResponseDto.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
        } catch (InvalidCredentialExceptions exceptions) {
            throw new InvalidCredentialExceptions("Usuario o contraseña incorrectos");
        } catch (CustomAuthenticationException e) {
            throw new CustomAuthenticationException("Error de autenticacion");
        } catch (AccessDeniedException exception) {
            throw new AccessDeniedException(" acceso denegado");
        }

    }

    /**
     * Metodo para cambiar el estado de expired  y revoked a true
     * busca estado false en las variable usando  con el id del usuario
     * y lo cambia a true si esta en false
     */
    private void revokedUsersToken(User user) {
        List<Tokens> validations =
                this.tokenRepository.findAllIExpiredIsFalseOrRevokedIsFalseByUserId(user.getId());
        if (!validations.isEmpty()) {
            validations.forEach(token -> {
                token.setRevoked(true);
                token.setExpired(true);
            });
            tokenRepository.saveAll(validations);
        }
    }

    /**
     * guarda el token de un usurio en la base de datos
     */
    private void saveUserToken(User user, String token) {
        Tokens tokenSaved = Tokens.builder()
                .user(user)
                .expired(false)
                .revoked(false)
                .token(token)
                .tokenType(TokenType.BEARER)
                .build();
        this.tokenRepository.save(tokenSaved);
    }

    /**
     * Metodo para refrescar un token
     * valida si el refresh token es igual al que esta en la base de datos
     * si es asi usa los metodos para generar un token, y refresh token
     */
    public TokenResponseDto refreshToken(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido");
        }

        String refreshToken = authHeader.substring(7);
        String username = jwtService.extractEmail(refreshToken);
        logger.info("Usuario extraído del token: " + username);

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido");
        }

        String accessToken = jwtService.generateAccessToken(userDetails.getUsername());
        String refreshTokenNew = jwtService.generateRefreshToken(userDetails.getUsername());

        return TokenResponseDto.builder()
                .token(accessToken)
                .refreshToken(refreshTokenNew)
                .build();
    }


    @Override
    public List<GetAllUsersResponseDto> getAllUsers() {
        try {
            List<User> allUsers = this.userRepository.findAll();

            return allUsers.stream()
                    .map(user -> GetAllUsersResponseDto.builder()
                            .name(user.getName())
                            .DNI(user.getDNI())
                            .birthday(String.valueOf(user.getDateBirthDay()))
                            .email(user.getEmail())
                            .country(user.getCountry())
                            .status(user.isStatus())
                            .build())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al traer usuarios", e);
        }
    }
}