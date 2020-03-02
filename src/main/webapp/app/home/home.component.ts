import { Component, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

import { AccountService } from 'app/core';
import { StateStorageService } from '../core/auth/state-storage.service';
import { Router } from '@angular/router';
import { LoginService } from '../core/login/login.service';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    account: Account;

    constructor(
        private accountService: AccountService,
        private eventManager: JhiEventManager,
        private stateStorageService: StateStorageService,
        private router: Router,
        private loginService: LoginService
    ) {}

    ngOnInit() {
        this.accountService.identity().then((account: Account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.loginService
            .login({
                username: this.username,
                password: this.password,
                rememberMe: this.rememberMe
            })
            .then(() => {
                this.authenticationError = false;
                if (this.router.url === '/' || /^\/activate\//.test(this.router.url) || /^\/reset\//.test(this.router.url)) {
                    this.router.navigate(['/tela-inicial']);
                }

                this.eventManager.broadcast({
                    name: 'authenticationSuccess',
                    content: 'Sending Authentication Success'
                });

                const redirect = this.stateStorageService.getUrl();

                if (redirect) {
                    this.stateStorageService.storeUrl(null);
                    this.router.navigate([redirect]);
                }
            })
            .catch(() => {
                this.authenticationError = true;
            });

        this.username = '';
        this.password = '';
        this.rememberMe = false;
    }

    requestResetPassword() {
        this.router.navigate(['/reset', 'request']);
    }
}
