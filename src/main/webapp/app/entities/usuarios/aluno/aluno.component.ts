import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared';
// import { User } from '../../../core/user/user.model';
// import { UserService } from '../../../core/user/user.service';
import { AccountService } from '../../../core/auth/account.service';
import { AlunoDeleteDialogComponent } from './delete/aluno-delete-dialog.component';
import { Aluno } from '../../../shared/model/usuarios/aluno.model';
import { AlunoService } from './aluno.service';

@Component({
    selector: 'jhi-aluno',
    templateUrl: './aluno.component.html'
})
export class AlunoComponent implements OnInit, OnDestroy {
    /*Objetos*/
    alunos: Aluno[];

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
        private alunoService: AlunoService,
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
            this.registerChangeInAlunos();
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    /*------------CARREGAMENTO----------*/
    registerChangeInAlunos() {
        this.eventManager.subscribe('alunoListModification', response => this.loadAll());
    }
    trackIdentity(index, item: Aluno) {
        return item.id;
    }
    setActive(aluno, isActivated) {
        aluno.activated = isActivated;

        this.alunoService.update(aluno).subscribe(response => {
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
        this.alunoService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<Aluno[]>) => this.onSuccess(res.body, res.headers),
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
        // this.router.navigate(['/admin/user-management'], {
        this.router.navigate(['/aluno'], {
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
        this.alunos = data;
    }
    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }
    deleteAluno(aluno: Aluno) {
        const modalRef = this.modalService.open(AlunoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.aluno = aluno;
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
