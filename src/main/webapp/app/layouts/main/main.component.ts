import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';

import { Title } from '@angular/platform-browser';
import { AppService } from '../services/app.service';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit {
    title = 'pro-dashboard-angular';

    constructor(private titleService: Title, private router: Router, private appService: AppService) {}

    getClasses() {
        const classes = {
            'pinned-sidebar': this.appService.getSidebarStat().isSidebarPinned,
            'toggeled-sidebar': this.appService.getSidebarStat().isSidebarToggeled
        };
        return classes;
    }

    toggleSidebar() {
        this.appService.toggleSidebar();
    }

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'horaCertaApp';

        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }

        return title;
    }

    ngOnInit() {
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.titleService.setTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
            if (event instanceof NavigationError && event.error.status === 404) {
                this.router.navigate(['/404']);
            }
        });
    }
}
