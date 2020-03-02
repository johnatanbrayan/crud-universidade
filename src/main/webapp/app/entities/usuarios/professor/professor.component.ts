import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared';
import { AccountService } from '../../../core/auth/account.service';
import { ProfessorDeleteDialogComponent } from './delete/professor-delete-dialog.component';
import { ProfessorService } from './professor.service';
import { Professor } from 'app/shared/model/usuarios/professor.model';

@Component({
    selector: 'jhi-professor',
    templateUrl: './professor.component.html'
})
export class ProfessorComponent implements OnInit, OnDestroy {
    /*Objetos*/
    professores: Professor[];

    /*Configuracao*/
    error: any;
    success: any;
    routeData: any;
    currentAccount: any;

    /*Paginacao*/
    links: any;
    totalItems: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private professorService: ProfessorService,
        private alertService: JhiAlertService,
        private accountService: AccountService,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.accountService.identity().then(account => {
            this.currentAccount = account;
            this.loadAll();
            this.registerChangeInProfessores();
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    /*------------CARREGAMENTO----------*/
    registerChangeInProfessores() {
        this.eventManager.subscribe('professorListModification', response => this.loadAll());
    }
    trackIdentity(index, item: Professor) {
        return item.id;
    }
    setActive(professor, isActivated) {
        professor.activated = isActivated;

        this.professorService.update(professor).subscribe(response => {
            if (response.status === 200) {
                this.error = null;
                this.success = 'OK';
                this.loadAll();
            } else {
                this.success = null;
                this.error = 'ERROR';
            }
        });
    }
    /*----------------------------------*/

    /*----------PAGINACAO-------*/
    loadAll() {
        this.professorService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<Professor[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }
    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/professor'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }
    /*---------------------------*/

    /*-------------CONFIGURACAO---------*/
    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.professores = data;
    }
    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }
    deleteProfessor(professor: Professor) {
        const modalRef = this.modalService.open(ProfessorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.professor = professor;
        modalRef.result.then(
            result => {
                // Left blank intentionally, nothing to do here
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
    }
    /*----------------------------------*/
}
