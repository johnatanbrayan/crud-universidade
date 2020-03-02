package com.forquality.crud.service;

import com.forquality.crud.domain.Authority;
import com.forquality.crud.domain.User;
import com.forquality.crud.repository.AuthorityRepository;
import com.forquality.crud.repository.UserRepository;
import com.forquality.crud.security.AuthoritiesConstants;
import com.forquality.crud.security.SecurityUtils;
import com.forquality.crud.service.dto.AlunoDTO;
import com.forquality.crud.service.util.RandomUtil;
import com.forquality.crud.web.rest.errors.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing alunos.
 */
@Service
@Transactional
public class AlunoService {

    private final Logger log = LoggerFactory.getLogger(AlunoService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public AlunoService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registererAluno(AlunoDTO alunoDTO, String password) {
        userRepository.findOneByLogin(alunoDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(alunoDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newAluno = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newAluno.setLogin(alunoDTO.getLogin().toLowerCase());
        // new aluno gets initially a generated password
        newAluno.setPassword(encryptedPassword);
        newAluno.setNome(alunoDTO.getNome());
        newAluno.setEmail(alunoDTO.getEmail().toLowerCase());
        newAluno.setImageUrl(alunoDTO.getImageUrl());
        // new aluno is not active
        newAluno.setActivated(false);
        // new aluno gets registration key
        newAluno.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newAluno.setAuthorities(authorities);
        userRepository.save(newAluno);
        this.clearUserCaches(newAluno);
        log.debug("Created Information for User: {}", newAluno);
        return newAluno;
    }

    private boolean removeNonActivatedUser(User existingUser){
        if (existingUser.getActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createAluno(AlunoDTO alunoDTO) {
        User aluno = new User();
        aluno.setLogin(alunoDTO.getLogin().toLowerCase());
        aluno.setNome(alunoDTO.getNome());
        aluno.setTipoUsuario(3);
        aluno.setEmail(alunoDTO.getEmail().toLowerCase());
        aluno.setDtNascimento(alunoDTO.getDtNascimento());
        aluno.setSexo(alunoDTO.getSexo());
        aluno.setTelCelular(alunoDTO.getTelCelular());
        aluno.setLogradouro(alunoDTO.getLogradouro());
        aluno.setNumero(alunoDTO.getNumero());
        aluno.setComplemento(alunoDTO.getComplemento());
        aluno.setBairro(alunoDTO.getBairro());
        aluno.setCidade(alunoDTO.getCidade());
        aluno.setUf(alunoDTO.getUf());
        aluno.setCep(alunoDTO.getCep());
        aluno.setCoord(alunoDTO.getCoord());
        aluno.setValor(alunoDTO.getValor());
        aluno.setToken(alunoDTO.getToken());
        aluno.setImageUrl(alunoDTO.getImageUrl());
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        aluno.setPassword(encryptedPassword);
        aluno.setResetKey(RandomUtil.generateResetKey());
        aluno.setResetDate(Instant.now());
        aluno.setActivated(true);
        if (alunoDTO.getAuthorities() != null) {
            Set<Authority> authorities = alunoDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            aluno.setAuthorities(authorities);
        }
        userRepository.save(aluno);
        this.clearUserCaches(aluno);
        log.debug("Created Information for User: {}", aluno);
        return aluno;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current aluno.
     *
     * @param nome first name of aluno
     * @param email email id of aluno
     * @param imageUrl image URL of aluno
     */

    public void updateAluno(String nome, String email, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(aluno -> {
                aluno.setNome(nome);
                aluno.setEmail(email.toLowerCase());
                aluno.setImageUrl(imageUrl);
                this.clearUserCaches(aluno);
                log.debug("Changed Information for Aluno: {}", aluno);
            });
    }

    /**
     * Update all information for a specific aluno, and return the modified aluno.
     *
     * @param alunoDTO aluno to update
     * @return updated aluno
     */
    public Optional<AlunoDTO> updateAluno(AlunoDTO alunoDTO) {
        return Optional.of(userRepository
            .findById(alunoDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(aluno -> {
                this.clearUserCaches(aluno);
                aluno.setLogin(alunoDTO.getLogin().toLowerCase());
                aluno.setNome(alunoDTO.getNome());
                aluno.setEmail(alunoDTO.getEmail().toLowerCase());
                aluno.setImageUrl(alunoDTO.getImageUrl());
                aluno.setDtNascimento(alunoDTO.getDtNascimento());
                aluno.setSexo(alunoDTO.getSexo());
                aluno.setTelCelular(alunoDTO.getTelCelular());
                aluno.setLogradouro(alunoDTO.getLogradouro());
                aluno.setNumero(alunoDTO.getNumero());
                aluno.setComplemento(alunoDTO.getComplemento());
                aluno.setBairro(alunoDTO.getBairro());
                aluno.setCidade(alunoDTO.getCidade());
                aluno.setUf(alunoDTO.getUf());
                aluno.setCep(alunoDTO.getCep());
                aluno.setCoord(alunoDTO.getCoord());
                aluno.setValor(alunoDTO.getValor());
                aluno.setToken(alunoDTO.getToken());
                aluno.setActivated(alunoDTO.isActivated());
                Set<Authority> managedAuthorities = aluno.getAuthorities();
                managedAuthorities.clear();
                alunoDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(aluno);
                log.debug("Changed Information for Aluno: {}", aluno);
                return aluno;
            })
            .map(AlunoDTO::new);
    }

    public void deleteAluno(String login) {
        userRepository.findOneByLogin(login).ifPresent(aluno -> {
            userRepository.delete(aluno);
            this.clearUserCaches(aluno);
            log.debug("Deleted Aluno: {}", aluno);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(aluno -> {
                String currentEncryptedPassword = aluno.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                aluno.setPassword(encryptedPassword);
                this.clearUserCaches(aluno);
                log.debug("Changed password for Aluno: {}", aluno);
            });
    }

    @Transactional(readOnly = true)
    public Page<User> getManagedAlunos(Pageable pageable) {
        return userRepository.buscarPorTipoUsuario(3, pageable);

    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated alunos should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedAlunos() {
        userRepository
            .findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(aluno -> {
                log.debug("Deleting not activated aluno {}", aluno.getLogin());
                userRepository.delete(aluno);
                this.clearUserCaches(aluno);
            });
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User aluno) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(aluno.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(aluno.getEmail());
    }
}
