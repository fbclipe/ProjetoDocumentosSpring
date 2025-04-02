package com.extraidados.challenge.service;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.exception.MessageException;
import com.extraidados.challenge.model.LoginDto;
import com.extraidados.challenge.model.RegisterDto;
import com.extraidados.challenge.repository.UserRepository;
import com.extraidados.challenge.response.LoginResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service //registeruser
public class AuthTokenService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository; //auth repository
    //private final PasswordEncoder passwordEncoder;


   //public AuthTokenService(User/this.passwordEncoder = passwordEncoder;

    public String extractUUIDToken(String token) {
        String[] uuidtoken = token.split("-");
        token = uuidtoken[0] +"-"+ uuidtoken[1] +"-"+ uuidtoken[2] +"-"+ uuidtoken[3] +"-"+ uuidtoken[4];
        return token;
     }
   //extrai o id do token e devovle //extrai a data de expiração e valida devolve um bolean;
   public Long extractIdToken(String token) { //parsear p long
        String[] authtoken = token.split("-");
        token = authtoken[5];
        return Long.parseLong(token); 
   }

   public Boolean tokenExpired(String token){
        LocalDateTime time = LocalDateTime.now();
        String[] authtoken = token.split("-");
        //int variavel = Integer.parseInt(variavel.substring(valor1 , valor2));

        int anoexpiracao = Integer.parseInt(authtoken[6]);
        int mesexpiracao = Integer.parseInt(authtoken[7]);
        int diaexpiracao = Integer.parseInt(authtoken[8].substring(0, 2)); 
        int horaexpiracao = Integer.parseInt(authtoken[8].substring(3, 5));
        int minutoexpiracao = Integer.parseInt(authtoken[8].substring(6, 8)); 
        int segundoexpiracao = Integer.parseInt(authtoken[8].substring(9, 11)); 
        int milesimosexpiracao = Integer.parseInt(authtoken[8].substring(12, 21));

        LocalDateTime expirationtime = LocalDateTime.of(anoexpiracao, mesexpiracao, diaexpiracao, horaexpiracao, minutoexpiracao, segundoexpiracao, milesimosexpiracao);
        //System.out.println("token" +expirationtime);

        //System.out.println("time "+ time);
        //System.out.println("cade mostra aqui" +time.isBefore(expirationtime));
        //if(time.isBefore(expirationtime)){
            //return true;
        //}
        return time.isBefore(expirationtime);
   }

    public String generateToken(Long userid) {
        String expiration = LocalDateTime.now().plusHours(5).toString();
        String token = UUID.randomUUID().toString() + "-" + userid + "-" + expiration;
           // token2.setTokenExpiration(LocalDateTime.now().plusMinutes(5)); 
       // userRepository.save(user);
        return token;
    }
    //find by id CHECK
    //validar o uuid token
    //usergettoken extrair o uuid e validar se ta batendo os dois
    public boolean isTokenValid(String token) {
            boolean verification = tokenExpired(token);
            if(!verification) {
                //nao quebra aqui
                return false;
            }

            Long tokenid = extractIdToken(token);

            User user = getUserById(tokenid).get();
            if(!getUserById(tokenid).isPresent()){
                //nao quebra aqui
                return false;
            }

            String extractedTokenUUID = extractUUIDToken(token);
            String tokenUUID = user.getAuthToken();
           if(tokenUUID.contains(extractedTokenUUID)){
                return true;
            }
            //System.out.println(tokenUUID.contains(extractedTokenUUID));
            
        return false;
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserbyUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public LoginResponse login(LoginDto loginDto) {
        Optional<User> userOpt = userRepository.findByUsername(loginDto.getUsername());

        if (!userOpt.isPresent()) {
            throw new MessageException("Username or password invalid");
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User or password invalid");
        }else if (userOpt.get().getPassword().equals(loginDto.getPassword())) {
            userOpt.get().getAuthToken();
            String newtoken = generateToken(userOpt.get().getId());
            userOpt.get().setAuthToken(newtoken);
            userRepository.save(userOpt.get());
            return new LoginResponse(newtoken);
        }
        throw new MessageException("User or password invalid.");
        //return new LoginResponse(userOpt.get().getAuthToken());
        //return ResponseEntity.ok(token);
    }
    


    public LoginResponse logout(String token) {
        Optional<User> userOpt = userRepository.findByAuthToken(token);

        if (userOpt.isEmpty()) {
            throw new MessageException("User not found.");
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or invalid");
        }
        

        User user = userOpt.get();
        user.setAuthToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);
            return new LoginResponse(userOpt.get().getAuthToken());

        //return ResponseEntity.ok("Sucessful logout");
    }

    public LoginResponse validateToken(String token) {
        boolean isValid = isTokenValid(token);
        //return ResponseEntity.ok(isValid);
                return new LoginResponse(isValid);
    }


    public User registerUser(RegisterDto registerDto) {
        User user = userService.createUserAdmin(registerDto);
        return user;
    }
}

