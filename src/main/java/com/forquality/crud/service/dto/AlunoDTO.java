package com.forquality.crud.service.dto;

import com.forquality.crud.config.Constants;
import com.forquality.crud.domain.Authority;
import com.forquality.crud.domain.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**admin    admin
 * A DTO representing a aluno, with his authorities.
 */
public class AlunoDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    @Size(max=200)
    private String nome;

    private LocalDate dtNascimento;

    @Size(max=1)
    private String sexo;

    private String telCelular;

    private Boolean activated;

    @Size(max=255)
    private String logradouro;

    @Size(max=5)
    private String numero;

    @Size(max=150)
    private String complemento;

    @Size(max=75)
    private String bairro;

    @Size(max=150)
    private String cidade;

    @Size(max=2)
    private String uf;

    private String cep;

    @Size(max=20)
    private String coord;

    private Double valor;

    @Size(max=150)
    private String token;

    private Set<String>  authorities;

    public AlunoDTO() {
        // Empty constructor needed for Jackson.
    }

    public AlunoDTO(User aluno) {
            this.id = aluno.getId();
            this.login = aluno.getLogin();
            this.email = aluno.getEmail();
            this.imageUrl = aluno.getImageUrl();
            this.nome = aluno.getNome();
            this.dtNascimento = aluno.getDtNascimento();
            this.sexo = aluno.getSexo();
            this.telCelular = aluno.getTelCelular();
            this.activated = aluno.getActivated();
            this.logradouro =  aluno.getLogradouro();
            this.numero = aluno.getNumero();
            this.complemento = aluno.getComplemento();
            this.bairro = aluno.getBairro();
            this.cidade = aluno.getCidade();
            this.uf = aluno.getUf();
            this.cep = aluno.getCep();
            this.coord = aluno.getCoord();
            this.valor = aluno.getValor();
            this.token = aluno.getToken();
            this.authorities = aluno.getAuthorities().stream()
                .map(Authority::getName)
                .collect(Collectors.toSet());
    }

    /*-------------------------ID------------------------*/
    public Long getId(){ return this.id; }
    public void setId(Long id) { this.id = id; }
    /*---------------------------------------------------*/

    /*-----------------------LOGIN--------------------------*/
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    /*------------------------------------------------------*/

    /*--------------------------IMAGE_URL-------------------------------*/
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    /*------------------------------------------------------------------*/

    /*----------------------EMAIL---------------------------*/
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    /*------------------------------------------------------*/

    /*-------------------------NOME------------------------*/
    public String getNome(){ return this.nome; }
    public void setNome(String nome){ this.nome = nome; }
    /*-----------------------------------------------------*/

    /*-------------------------DT_NASCIMENTO------------------------*/
    public LocalDate getDtNascimento(){ return this.dtNascimento; }
    public void setDtNascimento(LocalDate dtNascimento){ this.dtNascimento = dtNascimento; }
    /*--------------------------------------------------------------*/

    /*---------------------------SEXO-------------------------*/
    public String getSexo(){ return this.sexo; }
    public void setSexo(String sexo){ this.sexo = sexo; }
    /*-------------------------------------------------------*/

    /*---------------------------TEL_CELULAR----------------------------*/
    public String getTelCelular(){ return this.telCelular; }
    public void setTelCelular(String celular){ this.telCelular = celular; }
    /*--------------------------------------------------------------*/

    /*---------------------------------ACTIVATED--------------------------------*/
    public boolean isActivated() {
        return activated;
    }
    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    /*-----------------------------------------------------------------------*/

    /*-------------------------LOGRADOURO------------------------*/
    public String getLogradouro(){ return this.logradouro; }
    public void setLogradouro(String logradouro){ this.logradouro =  logradouro; }
    /*-----------------------------------------------------------*/

    /*-------------------------NUMERO------------------------*/
    public String getNumero(){ return this.numero; }
    public void setNumero(String numero) { this.numero = numero; }
    /*-------------------------------------------------------*/

    /*-------------------------COMPLEMENTO------------------------*/
    public String getComplemento() {return this.complemento; }
    public void setComplemento(String complemento){ this.complemento = complemento; }
    /*------------------------------------------------------------*/

    /*--------------------------BAIRRO----------------------------*/
    public String getBairro(){ return this.bairro; }
    public void setBairro(String bairro){ this.bairro = bairro; }
    /*------------------------------------------------------------*/

    /*---------------------------CIDADE---------------------------*/
    public String getCidade(){ return this.cidade; }
    public void setCidade(String cidade){ this.cidade = cidade; }
    /*------------------------------------------------------------*/

    /*---------------------------UF--------------------------*/
    public String getUf(){ return this.uf; }
    public void setUf(String uf){ this.uf = uf; }
    /*-------------------------------------------------------*/

    /*---------------------------CEP--------------------------*/
    public String getCep(){ return this.cep; }
    public void setCep(String cep){ this.cep = cep; }
    /*--------------------------------------------------------*/

    /*-------------------------COORD------------------------*/
    public String getCoord(){ return this.coord; }
    public void setCoord(String coord){ this.coord = coord; }
    /*------------------------------------------------------*/

    /*-------------------------VALOR------------------------*/
    public Double getValor(){ return this.valor; }
    public void setValor(Double valor){ this.valor = valor; }
    /*------------------------------------------------------*/

    /*---------------------------TOKEN--------------------------*/
    public String getToken(){ return this.token; }
    public void setToken(String token){ this.token = token; }
    /*----------------------------------------------------------*/

    /*-------------------------------------PEFIL-----------------------------------------*/
    public Set<String> getAuthorities() {
        return authorities;
    }
    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
    /*-----------------------------------------------------------------------------------*/

    @Override
    public String toString() {
        return "AlunoDTO{" +
            ", login='" + login + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", nome='" + nome + '\'' +
            ", dtNascimento= "+ dtNascimento+'\''+
            ", sexo= "+ sexo+ '\''+
            ", telCelular= "+ telCelular+ '\''+
            ", activated= "+ activated+ '\''+
            ", logradouro= "+ logradouro+ '\''+
            ", numero= "+ numero+ '\''+
            ", complemento= "+ complemento+ '\''+
            ", bairro= "+ bairro+ '\''+
            ", cidade= "+ cidade+ '\''+
            ", uf= "+ uf+ '\''+
            ", cep= "+ cep+ '\''+
            ", coord= "+ coord+ '\''+
            ", valor= "+ valor+ '\''+
            ", token= "+ token+ '\''+
            ", authorities= "+ authorities+ '\''+
            "}";
    }
}
