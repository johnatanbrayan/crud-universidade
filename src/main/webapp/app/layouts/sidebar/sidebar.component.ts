import { Component, OnInit } from '@angular/core';

import { AccountService } from 'app/core';
import { IUser } from '../../core/user/user.model';

@Component({
    selector: 'jhi-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
    // public isCollapsed = false;
    user: IUser;
    constructor(private accountService: AccountService) {}

    ngOnInit() {
        this.accountService.identity().then(account => {
            this.user = account;
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }
}
