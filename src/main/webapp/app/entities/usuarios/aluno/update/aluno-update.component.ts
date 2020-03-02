import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxViacepService, Endereco, ErroCep } from '@brunoc/ngx-viacep';
import { IAluno } from '../../../../shared/model/usuarios/aluno.model';
import { AlunoService } from '../aluno.service';

@Component({
    selector: 'jhi-aluno-update',
    templateUrl: './aluno-update.component.html'
})
export class AlunoUpdateComponent implements OnInit {
    /*Objetos*/
    aluno: IAluno;
    authorities: any[];

    /*Configuracao*/
    isSaving: boolean;

    /*Carregamento*/
    ufs: string[];

    /*-CEP-*/
    validador: boolean;

    /*Máscara*/
    public maskCep;
    public maskCpf;
    public maskCnpj;
    public maskFoneFix;
    public maskFoneTel;

    constructor(
        private alunoService: AlunoService,
        private route: ActivatedRoute,
        private router: Router,
        private viacep: NgxViacepService
    ) {
        this.maskCep = {
            mask: [/\d/, /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/],
            guide: true
        };
        this.maskCpf = {
            mask: [/\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '-', /\d/, /\d/],
            guide: true
        };
        this.maskCnpj = {
            mask: [/\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/],
            guide: true
        };
        this.maskFoneFix = {
            mask: ['(', /[1-9]/, /[1-9]/, ')', ' ', /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/],
            guide: true
        };
        this.maskFoneTel = {
            mask: ['(', /[1-9]/, /[1-9]/, ')', ' ', /\d/, /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/],
            guide: true
        };
    }

    ngOnInit() {
        this.route.data.subscribe(({ aluno }) => {
            this.aluno = aluno.body ? aluno.body : aluno;
        });

        this.status();

        this.authorities = [];
        this.alunoService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });

        this.carregarListaUfs();
        this.validador = false;
        this.isSaving = false;
    }

    /*--------------CARREGMAENTO------------*/
    previousState() {
        window.history.back();
    }
    carregarListaUfs() {
        this.ufs = Array<string>();
        this.ufs.push('AC');
        this.ufs.push('AL');
        this.ufs.push('AM');
        this.ufs.push('AP');
        this.ufs.push('BA');
        this.ufs.push('CE');
        this.ufs.push('DF');
        this.ufs.push('ES');
        this.ufs.push('GO');
        this.ufs.push('MA');
        this.ufs.push('MG');
        this.ufs.push('MS');
        this.ufs.push('MT');
        this.ufs.push('PA');
        this.ufs.push('PB');
        this.ufs.push('PE');
        this.ufs.push('PI');
        this.ufs.push('PR');
        this.ufs.push('RJ');
        this.ufs.push('RN');
        this.ufs.push('RO');
        this.ufs.push('RR');
        this.ufs.push('RS');
        this.ufs.push('SC');
        this.ufs.push('SE');
        this.ufs.push('SP');
        this.ufs.push('TO');
    }
    /*--------------------------------------*/

    /*-----------CONFIGURACAO-----------*/
    save() {
        this.aluno.authorities = new Array();
        this.aluno.authorities[0] = 'ALUNO';
        this.isSaving = true;
        if (this.aluno.id !== undefined) {
            this.alunoService.update(this.aluno).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.alunoService.create(this.aluno).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }
    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
        this.router.navigate(['/aluno']);
    }
    private onSaveError() {
        this.isSaving = false;
    }
    /*--------------------------------*/

    /*-------------------------------------CEP------------------------------------*/
    buscarCep() {
        let cep = this.aluno.cep;

        if (cep !== undefined) {
            cep = this.aluno.cep.replace(/\D/g, '');
        }
        if (this.isCEPvalido(cep)) {
            this.viacep
                .buscarPorCep(cep)
                .then((endereco: Endereco) => {
                    this.trataResultadoEndereco(endereco);
                    this.validador = false;
                })
                .catch((error: ErroCep) => {
                    this.validador = true;
                    this.trataTrocaCep();
                });
        } else {
            alert('cep invalido');
            this.validador = true;
            this.trataTrocaCep();
        }
    }
    /*Função para validação de CEP.*/
    isCEPvalido(strCEP) {
        // Caso o CEP não esteja nesse formato ele é inválido!
        const objER = /^[0-9]{8}$/;

        strCEP = this.trim(strCEP);
        if (strCEP.length > 0) {
            console.log('Entron validacao de lenght' + objER.test(strCEP));
            if (objER.test(strCEP)) {
                return true;
            }
        } else {
            return false;
        }
    }
    /*Passa os atributos da Classe Endereco da biblioteca para a sua classe*/
    trataResultadoEndereco(endereco: Endereco) {
        this.aluno.cidade = endereco.localidade;
        this.aluno.uf = endereco.uf;
        this.aluno.logradouro = endereco.logradouro;
        this.aluno.bairro = endereco.bairro;
    }
    /*Substitúi os espaços vazios no inicio e no fim da string por vazio.*/
    trim(strTexto) {
        return strTexto.replace(/^s+|s+$/g, '');
    }
    trataTrocaCep() {
        this.aluno.cidade = null;
        this.aluno.uf = null;
        this.aluno.logradouro = null;
        this.aluno.bairro = null;
    }
    /*---------------------------------------------------------------------------*/

    /*-----------------status_true--------------------*/
    status() {
        if (this.aluno.id == null) {
            this.aluno.activated = true;
        }
    }
    /*------------------------------------------------*/
}
