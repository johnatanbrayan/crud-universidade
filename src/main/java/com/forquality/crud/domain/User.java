package com.forquality.crud.domain;

import com.forquality.crud.config.Constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.validation.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.time.Instant;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends com.forquality.crud.domain.AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 200)
    @Column(name = "nome", length = 200)
    private String nome;

    @Column(name="dt_nascimento")
    private LocalDate dtNascimento;

    @Column(name="tipo_usuario")
    private Integer tipoUsuario;

    @Size(max = 1)
    @Column(name="sexo")
    private String sexo;

    @Column(name="tel_celular")
    private String telCelular;

    @Column(name="tel_fixo")
    private String telFixo;

    @Size(max = 255)
    @Column(name="logradouro")
    private String logradouro;

    @Size(max = 5)
    @Column(name="numero")
    private String numero;

    @Size(max = 150)
    @Column(name="complemento")
    private String complemento;

    @Size(max = 75)
    @Column(name="bairro")
    private String bairro;

    @Size(max = 150)
    @Column(name="cidade")
    private String cidade;

    @Size(max = 2)
    @Column(name="uf")
    private String uf;

    @Size(max = 11)
    @Column(name="cep")
    private String cep;

    @Size(max = 20)
    @Column(name="coord")
    private String coord;

    @Column(name="valor")
    private Double valor;

    @Size(max = 150)
    @Column(name="token")
    private String token;

    @Column(name="cpf")
    private String cpf;

    @Size(max = 18)
    @Column(name="cnpj")
    private String cnpj;

    @Size(max = 11)
    @Column(name="cnh")
    private String cnh;

    @Column(name="score")
    private Integer score;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<com.forquality.crud.domain.Authority> authorities = new HashSet<>();

    /*--------------------ID------------------*/
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    /*----------------------------------------*/

    /*------------------------------------------LOGIN----------------------------------------------*/
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }
    /*---------------------------------------------------------------------------------------------*/

    /*------------------------------PASSWORD----------------------------*/
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    /*------------------------------------------------------------------*/

    /*----------------------NOME------------------------*/
    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    /*--------------------------------------------------*/

    /*----------------------------------DATA_NASCIMENTO-----------------------------------*/
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

    /*-----------------------TEL_CELULAR------------------------*/
    public String getTelCelular(){ return this.telCelular; }
    public void setTelCelular(String telCelular){ this.telCelular = telCelular; }
    /*----------------------------------------------------------*/

    /*------------------------TEL_FIXO-------------------------*/
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

    /*---------------------------BAIRRO--------------------------*/
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
    public void setCnpj(String cnpj) { this.cnpj =  cnpj; }
    /*---------------------------------------------------------*/

    /*--------------------------CNH----------------------------*/
    public String getCnh() { return this.cnh; }
    public void setCnh(String cnh) { this.cnh = cnh; }
    /*---------------------------------------------------------*/

    /*-------------------------SCORE---------------------------*/
    public Integer getScore(){ return this.score; }
    public void setScore(Integer score) { this.score =  score; }
    /*---------------------------------------------------------*/

    /*------------------------EMAIL-------------------------*/
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    /*------------------------------------------------------*/

    /*---------------------------IMAGE_URL------------------------------*/
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    /*------------------------------------------------------------------*/

    /*------------------------------STATUS-----------------------------------*/
    public boolean getActivated() {
        return activated;
    }
    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    /*-----------------------------------------------------------------------*/

    /*-----------------------------ATIVACAO_SENHA-------------------------------------------*/
    public String getActivationKey() {
        return activationKey;
    }
    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }
    /*--------------------------------------------------------------------------------------*/

    /*-----------------------ALTERAR_SENHA------------------------------*/
    public String getResetKey() {
        return resetKey;
    }
    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }
    /*------------------------------------------------------------------*/

    /*----------------------------DATA_REST----------------------------------*/
    public Instant getResetDate() {
        return resetDate;
    }
    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }
    /*-----------------------------------------------------------------------*/

    /*-----------------------------------------PERFIL---------------------------------------*/
    public Set<com.forquality.crud.domain.Authority> getAuthorities() {
        return authorities;
    }
    public void setAuthorities(Set<com.forquality.crud.domain.Authority> authorities) {
        this.authorities = authorities;
    }
    /*--------------------------------------------------------------------------------------*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", nome='" + nome + '\'' +
            ", dt_nascimento= "+ dtNascimento+'\''+
            ", sexo= "+ sexo+ '\''+
            ", tipo_usuario= "+ tipoUsuario+ '\''+
            ", tel_celular= "+ telCelular+ '\''+
            ", tel_fixo= "+ telFixo+ '\''+
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
            ", score= "+ score+ '\''+
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated='" + activated + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
