package br.com.challange.security.jwt;


import br.com.challange.models.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private Integer id;
    private String username;
    private Permission permission;
}
