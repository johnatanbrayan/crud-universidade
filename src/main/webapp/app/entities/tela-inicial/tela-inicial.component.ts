import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-tela-condutor',
    templateUrl: './tela-inicial.component.html',
    styleUrls: ['./tela-inicial.scss']
})
export class TelaInicialComponent implements OnInit {
    /*Configuracao*/
    routeData: any;
    currentAccount: any;

    constructor() {}
    ngOnInit() {}
}
