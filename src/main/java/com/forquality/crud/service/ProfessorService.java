package com.forquality.crud.service;

import com.forquality.crud.domain.Authority;
import com.forquality.crud.domain.User;
import com.forquality.crud.repository.AuthorityRepository;
import com.forquality.crud.repository.UserRepository;
import com.forquality.crud.security.AuthoritiesConstants;
import com.forquality.crud.security.SecurityUtils;
import com.forquality.crud.service.dto.ProfessorDTO;
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
 * Service class for managing professores.
 */
@Service
@Transactional
public class ProfessorService {

    private final Logger log = LoggerFactory.getLogger(ProfessorService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public ProfessorService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
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

    public User registererProfessor(ProfessorDTO professorDTO, String password) {
        userRepository.findOneByLogin(professorDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(professorDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newProfessor = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newProfessor.setLogin(professorDTO.getLogin().toLowerCase());
        // new professor gets initially a generated password
        newProfessor.setPassword(encryptedPassword);
        newProfessor.setNome(professorDTO.getNome());
        newProfessor.setEmail(professorDTO.getEmail().toLowerCase());
        newProfessor.setImageUrl(professorDTO.getImageUrl());
        // new professor is not active
        newProfessor.setActivated(false);
        // new professor gets registration key
        newProfessor.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newProfessor.setAuthorities(authorities);
        userRepository.save(newProfessor);
        this.clearUserCaches(newProfessor);
        log.debug("Created Information for User: {}", newProfessor);
        return newProfessor;
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

    public User createProfessor(ProfessorDTO professorDTO) {
        User professor = new User();
        professor.setLogin(professorDTO.getLogin().toLowerCase());
        professor.setNome(professorDTO.getNome());
        professor.setTipoUsuario(4);
        professor.setEmail(professorDTO.getEmail().toLowerCase());
        professor.setDtNascimento(professorDTO.getDtNascimento());
        professor.setSexo(professorDTO.getSexo());
        professor.setTelCelular(professorDTO.getTelCelular());
        professor.setLogradouro(professorDTO.getLogradouro());
        professor.setNumero(professorDTO.getNumero());
        professor.setComplemento(professorDTO.getComplemento());
        professor.setBairro(professorDTO.getBairro());
        professor.setCidade(professorDTO.getCidade());
        professor.setUf(professorDTO.getUf());
        professor.setCep(professorDTO.getCep());
        professor.setCoord(professorDTO.getCoord());
        professor.setValor(professorDTO.getValor());
        professor.setToken(professorDTO.getToken());
        professor.setImageUrl(professorDTO.getImageUrl());
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        professor.setPassword(encryptedPassword);
        professor.setResetKey(RandomUtil.generateResetKey());
        professor.setResetDate(Instant.now());
        professor.setActivated(true);
        if (professorDTO.getAuthorities() != null) {
            Set<Authority> authorities = professorDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            professor.setAuthorities(authorities);
        }
        userRepository.save(professor);
        this.clearUserCaches(professor);
        log.debug("Created Information for User: {}", professor);
        return professor;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current professor.
     *
     * @param nome first name of professor
     * @param email email id of professor
     * @param imageUrl image URL of professor
     */

    public void updateProfessor(String nome, String email, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(professor -> {
                professor.setNome(nome);
                professor.setEmail(email.toLowerCase());
                professor.setImageUrl(imageUrl);
                this.clearUserCaches(professor);
                log.debug("Changed Information for Professor: {}", professor);
            });
    }

    /**
     * Update all information for a specific professor, and return the modified professor.
     *
     * @param professorDTO professor to update
     * @return updated professor
     */
    public Optional<ProfessorDTO> updateProfessor(ProfessorDTO professorDTO) {
        return Optional.of(userRepository
            .findById(professorDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(professor -> {
                this.clearUserCaches(professor);
                professor.setLogin(professorDTO.getLogin().toLowerCase());
                professor.setNome(professorDTO.getNome());
                professor.setEmail(professorDTO.getEmail().toLowerCase());
                professor.setImageUrl(professorDTO.getImageUrl());
                professor.setDtNascimento(professorDTO.getDtNascimento());
                professor.setSexo(professorDTO.getSexo());
                professor.setTelCelular(professorDTO.getTelCelular());
                professor.setLogradouro(professorDTO.getLogradouro());
                professor.setNumero(professorDTO.getNumero());
                professor.setComplemento(professorDTO.getComplemento());
                professor.setBairro(professorDTO.getBairro());
                professor.setCidade(professorDTO.getCidade());
                professor.setUf(professorDTO.getUf());
                professor.setCep(professorDTO.getCep());
                professor.setCoord(professorDTO.getCoord());
                professor.setValor(professorDTO.getValor());
                professor.setToken(professorDTO.getToken());
                professor.setActivated(professorDTO.isActivated());
                Set<Authority> managedAuthorities = professor.getAuthorities();
                managedAuthorities.clear();
                professorDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(professor);
                log.debug("Changed Information for Professor: {}", professor);
                return professor;
            })
            .map(ProfessorDTO::new);
    }

    public void deleteProfessor(String login) {
        userRepository.findOneByLogin(login).ifPresent(professor -> {
            userRepository.delete(professor);
            this.clearUserCaches(professor);
            log.debug("Deleted Professor: {}", professor);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(professor -> {
                String currentEncryptedPassword = professor.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                professor.setPassword(encryptedPassword);
                this.clearUserCaches(professor);
                log.debug("Changed password for Professor: {}", professor);
            });
    }

    @Transactional(readOnly = true)
    public Page<User> getManagedProfessores(Pageable pageable) {
        return userRepository.buscarPorTipoUsuario(4, pageable);
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
     * Not activated professores should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedProfessores() {
        userRepository
            .findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(professor -> {
                log.debug("Deleting not activated professor {}", professor.getLogin());
                userRepository.delete(professor);
                this.clearUserCaches(professor);
            });
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User professor) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(professor.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(professor.getEmail());
    }
}
