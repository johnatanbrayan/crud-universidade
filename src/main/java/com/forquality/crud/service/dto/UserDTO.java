package com.forquality.crud.service.dto;

import com.forquality.crud.config.Constants;

import com.forquality.crud.domain.Authority;
import com.forquality.crud.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 200)
    private String nome;

    private LocalDate dtNascimento;

    private Integer tipoUsuario;

    @Size(max = 1)
    private String sexo;

    private String telCelular;

    private String telFixo;

    @Size(max = 255)
    private String logradouro;

    @Size(max = 5)
    private String numero;

    @Size(max = 150)
    private String complemento;

    @Size(max = 75)
    private String bairro;

    @Size(max = 150)
    private String cidade;

    @Size(max = 2)
    private String uf;

    private String cep;

    @Size(max = 20)
    private String coord;

    private Double valor;

    @Size(max = 150)
    private String token;

    private String cpf;

    private String cnpj;

    @Size(max = 11)
    private String cnh;

    @Size(max = 100)
    private String nomeFantasia;

    private Integer score;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.nome = user.getNome();
        this.dtNascimento = user.getDtNascimento();
        this.tipoUsuario = user.getTipoUsuario();
        this.sexo = user.getSexo();
        this.telCelular = user.getTelCelular();
        this.telFixo = user.getTelFixo();
        this.logradouro = user.getLogradouro();
        this.numero = user.getNumero();
        this.complemento = user.getComplemento();
        this.bairro = user.getBairro();
        this.cidade = user.getCidade();
        this.uf = user.getUf();
        this.cep = user.getCep();
        this.coord = user.getCoord();
        this.valor =  user.getValor();
        this.cpf = user.getCpf();
        this.cnpj = user.getCnpj();
        this.cnh = user.getCnh();
        this.score = user.getScore();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.imageUrl = user.getImageUrl();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream()
            .map(Authority::getName)
            .collect(Collectors.toSet());
    }

    /*-------------------ID-------------------*/
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    /*----------------------------------------*/

    /*-----------------------LOGIN--------------------------*/
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    /*------------------------------------------------------*/

    /*----------------------NOME------------------------*/
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    /*--------------------------------------------------*/

    /*----------------------------------DATA-NASCIMENTO-----------------------------------*/
    public LocalDate getDtNascimento(){ return this.dtNascimento; }
    public void setDtNascimento(LocalDate dtNascimento){ this.dtNascimento = dtNascimento; }
    /*------------------------------------------------------------------------------------*/

    /*----------------------------------TIPO_USUARIO-----------------------------------*/
    public Integer getTipoUsuario(){ return this.tipoUsuario; }
    public void setTipoUsuario(Integer tipoUsuario){ this.tipoUsuario = tipoUsuario; }
    /*------------------------------------------------------------------------------------*/


    /*--------------------------SEXO-------------------------*/
    public String getSexo(){ return this.sexo; }
    public void setSexo(String sexo){ this.sexo = sexo; }
    /*-------------------------------------------------------*/

    /*-----------------------TEL-CELULAR------------------------*/
    public String getTelCelular(){ return this.telCelular; }
    public void setTelCelular(String telCelular){ this.telCelular = telCelular; }
    /*----------------------------------------------------------*/

    /*------------------------TEL-FIXO-------------------------*/
    public String getTelFixo() { return this.telFixo;}
    public void setTelFixo(String telFixo) {this.telFixo = telFixo;}
    /*---------------------------------------------------------*/

    /*--------------------------------LOGRADOURO----------------------------------*/
    public String getLogradouro(){ return this.logradouro; }
    public void setLogradouro(String logradouro){ this.logradouro = logradouro; }
    /*----------------------------------------------------------------------------*/

    /*----------------------------NUMERO--------------------------*/
    public String getNumero(){ return this.numero; }
    public void setNumero(String numero){ this.numero = numero; }
    /*------------------------------------------------------------*/

    /*-----------------------------------COMPLEMENTO---------------------------------*/
    public String getComplemento(){ return this.complemento; }
    public void setComplemento(String complemento){ this.complemento = complemento; }
    /*-------------------------------------------------------------------------------*/

    /*---------------------------BAIRRO-- -----------------------*/
    public String getBairro(){ return this.bairro; }
    public void setBairro(String bairro){ this.bairro = bairro; }
    /*-----------------------------------------------------------*/

    /*--------------------------CIDADE---------------------------*/
    public String getCidade(){ return this.cidade; }
    public void setCidade(String cidade){ this.cidade = cidade; }
    /*-----------------------------------------------------------*/

    /*-----------------------UF------------------------*/
    public String getUf(){ return this.uf; }
    public void setUf(String uf){ this.uf = uf; }
    /*-------------------------------------------------*/

    /*---------------------CEP------------------------*/
    public String getCep(){ return this.cep; }
    public void setCep(String cep){ this.cep = cep; }
    /*------------------------------------------------*/

    /*------------------------COORD---------------------------*/
    public String getCoord(){ return this.coord; }
    public void setCoord(String coord){ this.coord = coord; }
    /*--------------------------------------------------------*/

    /*------------------------VALOR--------------------------*/
    public Double getValor(){ return this.valor; }
    public void setValor(Double valor){ this.valor = valor; }
    /*-------------------------------------------------------*/

    /*-----------------------TOKEN-----------------------------*/
    public String getToken(){ return this.token; }
    public void setToken(String token){ this.token = token; }
    /*---------------------------------------------------------*/

    /*-------------------------CPF-----------------------------*/
    public String getCpf() { return this.cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    /*---------------------------------------------------------*/

    /*---------------------------CNPJ--------------------------*/
    public String getCnpj(){ return this.cnpj;}
    public void setCnpj(String cnpf) { this.cnpj =  cnpj; }
    /*---------------------------------------------------------*/

    /*--------------------------CNH----------------------------*/
    public String getCnh() { return this.cnh; }
    public void setCnh(String cnh) { this.cnh = cnh; }
    /*---------------------------------------------------------*/

    /*-----------------------NOME-FANTASIA---------------------*/
    public String getNomeFantasia() { return this.nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) {this.nomeFantasia = nomeFantasia;}
    /*---------------------------------------------------------*/

    /*-------------------------SCORE---------------------------*/
    public Integer getScore(){ return this.score; }
    public void setScore(Integer score) { this.score =  score; }
    /*---------------------------------------------------------*/

    /*----------------------EMAIL---------------------------*/
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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

    /*---------------------------------STATUS--------------------------------*/
    public boolean isActivated() {
        return activated;
    }
    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    /*-----------------------------------------------------------------------*/

    /*--------------------------CRIADO_POR----------------------------------*/
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /*----------------------------------------------------------------------*/

    /*--------------------------------DATA_CRIACAO-----------------------------------*/
    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
    /*-------------------------------------------------------------------------------*/

    /*----------------------------------LAST_MODIFIED-------------------------------------------*/
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    /*------------------------------------------------------------------------------------------*/

    /*------------------------------------------LAST_MODIFIED_DATE---------------------------------------*/
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    /*---------------------------------------------------------------------------------------------------*/

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
        return "UserDTO{" +
            "id='" + id + '\'' +
            ", login='" + login + '\'' +
            ", nome='" + nome + '\'' +
            ", dtNascimento= "+ dtNascimento+'\''+
            ", tipo_usuario= "+ tipoUsuario+ '\''+
            ", sexo= "+ sexo+ '\''+
            ", tel_celular= "+ telCelular+ '\''+
            ", telFixo= "+ telFixo+ '\''+
            ", logradouro= "+ logradouro+ '\''+
            ", numero= "+ numero+ '\''+
            ", complemento= "+ complemento+ '\''+
            ", bairro= "+ bairro+ '\''+
            ", cidade= "+ cidade+ '\''+
            ", uf= "+ uf+ '\''+
            ", cep= "+ cep+ '\''+
            ", coord= "+ coord+ '\''+
            ", valor= "+ valor+ '\''+
            ", cpf= "+ cpf+ '\''+
            ", cnpj= "+ cnpj+ '\''+
            ", cnh= "+ cnh+ '\''+
            ", nomeFantasia= "+ nomeFantasia+ '\''+
            ", score= "+ score+ '\''+
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            "}";
    }
}
