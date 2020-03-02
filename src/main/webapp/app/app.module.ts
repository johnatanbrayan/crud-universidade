import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';
import { NgJhipsterModule } from 'ng-jhipster';
import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { CrudUniversidadeSharedModule } from 'app/shared';
import { CrudUniversidadeCoreModule } from 'app/core';
import { CrudUniversidadeAppRoutingModule } from './app-routing.module';
import { CrudUniversidadeHomeModule } from './home/home.module';
import { CrudUniversidadeAccountModule } from './account/account.module';
import { CrudUniversidadeEntityModule } from './entities/entity.module';
import { JhiMainComponent, NavbarComponent, FooterComponent, PageRibbonComponent, ErrorComponent } from './layouts';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
import * as moment from 'moment';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        BsDatepickerModule.forRoot(),
        FormsModule,
        BrowserAnimationsModule,
        BrowserModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: false,
            alertTimeout: 5000
        }),
        CrudUniversidadeSharedModule.forRoot(),
        CrudUniversidadeCoreModule,
        CrudUniversidadeHomeModule,
        CrudUniversidadeAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        CrudUniversidadeEntityModule,
        CrudUniversidadeAppRoutingModule
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent, SidebarComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class CrudUniversidadeAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
